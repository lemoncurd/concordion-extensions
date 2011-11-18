package uk.co.codemonkey.concordion.specLinker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.co.codemonkey.concordion.specLinker.JunitResults.JunitTestResults;
import uk.co.codemonkey.concordion.specLinker.LinkCollector.FileTreeNode;

public class LinkWriter {

	private static final String DIV = "div";
	private static final String LINKS_ID = "childSpecifications";

	private static final Comparator<Element> SORT_BY_TEXT_CONTENTS = new Comparator<Element>() {
		@Override
		public int compare(Element o1, Element o2) {
			return o1.getTextContent().compareTo(o2.getTextContent());
		}
	};
	
	private final DocumentBuilder docBuilder;
	private final Transformer xformer;
	private final JunitResults resultParser;

	LinkWriter(JunitResults resultParser) throws Exception {
		this.resultParser = resultParser;
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		docBuilder = dbfac.newDocumentBuilder();
		xformer = TransformerFactory.newInstance().newTransformer();
		xformer.setOutputProperty(OutputKeys.INDENT, "yes");
		xformer.setOutputProperty(OutputKeys.METHOD, "xml");
		xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

	}
	
	public boolean addLinks(FileTreeNode fileTree) {
		
		JunitTestResults resultsFor = resultParser.resultsFor(fileTree.getFile().getPath());
		boolean passed = resultsFor == null ? true : resultsFor.isSuccess();

		List<FileLink> links = new ArrayList<FileLink>();
		List<FileTreeNode> children = fileTree.getChildren();
		for(FileTreeNode child : children) {
			boolean childPassed = addLinks(child);
			links.add(new FileLink(child.getFile(), childPassed));
			passed &= childPassed;
		}
		if(links.size()>0) {
			addLinks(fileTree.getFile(), links);
		}
		return passed;
	}


	void addLinks(File index, List<FileLink> links) {
		if (links.isEmpty()) {
			return;
		}
		try {
			Document indexDoc = docBuilder.parse(index);
			Node body = indexDoc.getElementsByTagName("body").item(0);
			if (body == null) {
				throw new RuntimeException(
						"Expected <body> element in index file ["
								+ index.getCanonicalPath() + "]");
			}
			appendLinks(indexDoc, index, links, body);
			write(index, indexDoc);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void appendLinks(Document indexDoc, File index, List<FileLink> links, Node body) throws Exception {
		Element container= getLinksContainer(indexDoc, body);
		container.appendChild(createTitle(indexDoc, "Further Details"));
		container.appendChild(createList(indexDoc, index, links));
	}


	private Element getLinksContainer(Document indexDoc, Node body) {
		Element container = findTagWithAttribute(indexDoc, DIV, "id", LINKS_ID);
		if(container == null){
			container = indexDoc.createElement(DIV);
			container.setAttribute("id", LINKS_ID);
			body.insertBefore(container, findTagWithAttribute(indexDoc, DIV, "class", "footer"));	
		}
		return deleteChildren(container);
	}


	private Element findTagWithAttribute(Document indexDoc, String tagName, String attribute, String value) {
		NodeList list = indexDoc.getElementsByTagName(tagName);
		for(int i=0; i < list.getLength(); i++) {
			Element item = (Element)list.item(i);
			if(value.equals(item.getAttribute(attribute))){
				return item;
			}
		}
		return null;
	}


	private Element deleteChildren(Element node) {
		NodeList children = node.getChildNodes();
		int nodesToDelete = children.getLength();
		for(int i=0; i < nodesToDelete; i++) {
			node.removeChild(children.item(0));
		}
		return node;
	}
	
	private Element createTitle(Document indexDoc, String docTitle) {
		Element title = indexDoc.createElement("h3");
		title.setTextContent(docTitle);
		return title;
	}

	private Element createList(Document indexDoc, File index, List<FileLink> links) throws Exception {
		Element list = indexDoc.createElement("ul");
		for (Element link : createAnchors(indexDoc, index, links)) {
			Element li = indexDoc.createElement("li");
			li.appendChild(link);
			list.appendChild(li);
		}
		return list;
	}
	
	private List<Element> createAnchors(Document indexDoc, File index, List<FileLink> links) throws Exception {
		List<Element> anchors = new ArrayList<Element>();
		for(FileLink link : links) {
			Element anchor = indexDoc.createElement("a");
			anchor.setAttribute("href", getHref(index, link.file));
			anchor.setAttribute("class", link.isSuccess ? "success" : "failure");
			anchor.setTextContent(getTitle(link.file));
			anchors.add(anchor);			
		}
		Collections.sort(anchors, SORT_BY_TEXT_CONTENTS);
		return anchors;
	}

	private String getTitle(File link) throws Exception {
		return getTitle(docBuilder.parse(link), link.getName());
	}


	private String getTitle(Document doc, String defaultName) {
		Node title = doc.getElementsByTagName("title").item(0);
		return title == null ? defaultName : title.getTextContent();
	}

	private String getHref(File index, File link) {
		return index.getParentFile().toURI().relativize(link.toURI()).getPath();
	}
	
	private void write(File index, Document indexDoc) throws Exception {
		Result result = new StreamResult(index);
		xformer.transform(new DOMSource(indexDoc), result);
	}

	private static class FileLink {
		final File file;
		final boolean isSuccess;
		
		public FileLink(File linkedFile, boolean passed) {
			this.file = linkedFile;
			this.isSuccess = passed;
		}
	}

}
