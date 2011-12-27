package uk.co.codemonkey.concordion.specLinker;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

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
import org.xml.sax.SAXException;

public class HtmlDocument {

	private static Transformer xformer;
	private static DocumentBuilder docBuilder;
	private final Document document;
	private final File file;

	static {
		try {
		    DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		    docBuilder = dbfac.newDocumentBuilder();
		    xformer = TransformerFactory.newInstance().newTransformer();
		    xformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    xformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		}
		catch(Exception e) {
			throw new RuntimeException("Unable to setup XML infrastructure", e);
		}
	}

	public HtmlDocument(File index) throws SAXException, IOException {
		this.file = index;
		document = docBuilder.parse(index);
	}
	
	public boolean isFor(File file) {
		return this.file.equals(file);
	}
	
	public Node body() throws IOException {
		Node body = findTagWithName("body");
		if (body == null) {
			throw new RuntimeException(
					"Expected <body> element in index file ["
							+ file.getCanonicalPath() + "]");
		}
		return body;
	}
	
	public void prependTextTo(Element element, String text){
		prependNodeTo(element, document.createTextNode(" > "));
	}

	public void prependNodeTo(Element target, Node element) {
		target.insertBefore(element, target.getFirstChild());
	}

	public Node findTagWithName(String tagName) {
		return document.getElementsByTagName(tagName).item(0);

	}
	
	public Element findOrInsertTagWithAttribute(String tagName, String attName, String attVal, Node targetNode) {
		return findOrInsertTagWithAttribute(tagName, attName, attVal, targetNode, targetNode.getFirstChild());
	}

	public Element findOrInsertTagWithAttribute(String tagName, String attName, String attVal, Node targetNode, Node insertBefore) {
		Element tag = findTagWithAttribute(tagName, attName, attVal);
		if (tag == null) {
			tag = document.createElement(tagName);
			tag.setAttribute(attName, attVal);
			targetNode.insertBefore(tag, insertBefore);
		}
		return tag;
	}
	
	public Element findTagWithAttribute(String tagName,
			String attribute, String value) {
		NodeList list = document.getElementsByTagName(tagName);
		for (int i = 0; i < list.getLength(); i++) {
			Element item = (Element) list.item(i);
			if (value.equals(item.getAttribute(attribute))) {
				return item;
			}
		}
		return null;
	}
	
	public Element createAnchor(String href, String textValue) {
		Element anchor = document.createElement("a");
		anchor.setAttribute("href", href);
		anchor.setTextContent(textValue);
		return anchor;
	}
	
	public String title(String defaultName) {
		Node title = document.getElementsByTagName("title").item(0);
		return title == null ? defaultName : title.getTextContent();
	}
	
	public String documentPath() {
		return file.getParent();
	}

	public Element deleteChildren(Element node) {
		NodeList children = node.getChildNodes();
		int nodesToDelete = children.getLength();
		for (int i = 0; i < nodesToDelete; i++) {
			node.removeChild(children.item(0));
		}
		return node;
	}

	public void appendTitle(Element container, String title) {
		Element titleElement = document.createElement("h3");
		titleElement.setTextContent(title);
		container.appendChild(titleElement);
	}
	
	public void appendList(Element container, Collection<Element> elements) {
		Element list = document.createElement("ul");
		for (Element link : elements) {
			Element li = document.createElement("li");
			li.appendChild(link);
			list.appendChild(li);
		}
		container.appendChild(list);
	}
	
	public void write() throws Exception{
		Result result = new StreamResult(file);
		xformer.transform(new DOMSource(document), result);
	}
}
