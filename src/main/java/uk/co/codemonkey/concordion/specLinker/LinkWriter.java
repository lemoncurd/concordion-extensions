package uk.co.codemonkey.concordion.specLinker;

import static org.apache.commons.io.FilenameUtils.normalize;

import java.io.File;
import java.io.IOException;
import java.net.URI;
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

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.co.codemonkey.concordion.specLinker.JunitResults.JunitTestResults;
import uk.co.codemonkey.concordion.specLinker.LinkCollector.FileTreeNode;

public class LinkWriter {

	private static final String BREADCRUMBS_ID = "breadcrumbs";
	private static final String SPAN = "span";
	private static final String DIV = "div";
	private static final String LINKS_ID = "childSpecifications";

	private static final Comparator<Element> SORT_BY_TEXT_CONTENTS = new Comparator<Element>() {
		@Override
		public int compare(Element o1, Element o2) {
			return o1.getTextContent().compareTo(o2.getTextContent());
		}
	};

	private final JunitResults resultParser;

	LinkWriter(JunitResults resultParser) throws Exception {
		this.resultParser = resultParser;
	}

	public boolean addLinks(FileTreeNode fileTree) {
		return proceesTree(fileTree, fileTree);
	}

	boolean proceesTree(FileTreeNode root, FileTreeNode current) {
		JunitTestResults resultsFor = resultParser.resultsFor(current.getFile()
				.getPath());
		boolean passed = resultsFor == null ? true : resultsFor.isSuccess();

		List<FileLink> links = new ArrayList<FileLink>();
		List<FileTreeNode> children = current.getChildren();
		for (FileTreeNode child : children) {
			boolean childPassed = proceesTree(root, child);
			links.add(new FileLink(child.getFile(), childPassed));
			passed &= childPassed;
		}

		processFile(root, current, links);
		return passed;
	}

	private void processFile(FileTreeNode root, FileTreeNode current, List<FileLink> links) {
		File index = current.getFile();
		try {
			HtmlDocument doc = new HtmlDocument(index);
			addRootBreadcrumb(doc, root.getFile());
			addLinks(doc, index, links);
			doc.write();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void addRootBreadcrumb(HtmlDocument doc, File rootFile)
			throws Exception {
		if (doc.isFor(rootFile)) {
			return;
		}

		String title = new HtmlDocument(rootFile).title("Root");
		String path = convertToRelativePath(rootFile.getAbsolutePath(), doc.documentPath()); 
		Element breadcrumbs = doc.findOrInsertTagWithAttribute("span", "class", BREADCRUMBS_ID, doc.body());
		doc.prependTextTo(breadcrumbs, " > ");
		doc.prependNodeTo(breadcrumbs, doc.createAnchor(path, title));
	}

	void addLinks(HtmlDocument doc, File index, List<FileLink> links) {
		if (links.isEmpty()) {
			return;
		}
		try {
			Element footer = doc.findTagWithAttribute(DIV, "class", "footer");
			Element container = doc.findOrInsertTagWithAttribute(DIV, "id", LINKS_ID, doc.body(), footer);
			container =  doc.deleteChildren(container);
			
			doc.appendTitle(container, "Further Details");
			doc.appendList(container, createAnchors(doc, index, links));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private List<Element> createAnchors(HtmlDocument doc, File index,
			List<FileLink> links) throws Exception {
		List<Element> anchors = new ArrayList<Element>();
		for (FileLink link : links) {
			File file = link.file;
			String href = getHref(index, file);
			String title = new HtmlDocument(file).title(file.getName());
			Element anchor = doc.createAnchor(href, title);
			anchor.setAttribute("class", link.isSuccess ? "success" : "failure");
			anchors.add(anchor);
		}
		Collections.sort(anchors, SORT_BY_TEXT_CONTENTS);
		return anchors;
	}

	private String getHref(File page, File link) {
		return convertToRelativePath(link.getAbsolutePath(), page.getParent());
	}

	private static class FileLink {
		final File file;
		final boolean isSuccess;

		public FileLink(File linkedFile, boolean passed) {
			this.file = linkedFile;
			this.isSuccess = passed;
		}
	}

	/**
	 * Converts an absolute path so that it is relative to another one.
	 * This is necessary because URI.relativize() does not work when the path
	 * to be 'relativized' is a parent of the absolute path i.e. you need ../ 
	 * in the result.
	 * 
	 * Thanks to where this code was taken from:
	 * http://mrpmorris.blogspot.com/2007/05/convert-absolute-path-to-relative-path.html
	 */
	private static String convertToRelativePath(String absolutePath,
			                                    String relativeTo) {
		StringBuilder relativePath = null;

		relativeTo = normalize(relativeTo, true);
		absolutePath = normalize(absolutePath, true);

		if (relativeTo.equals(absolutePath) == true) {

		} else {
			String[] absoluteDirectories = relativeTo.split("/");
			String[] relativeDirectories = absolutePath.split("/");

			// Get the shortest of the two paths
			int length = absoluteDirectories.length < relativeDirectories.length ? absoluteDirectories.length
					: relativeDirectories.length;

			// Use to determine where in the loop we exited
			int lastCommonRoot = -1;
			int index;

			// Find common root
			for (index = 0; index < length; index++) {
				if (absoluteDirectories[index]
						.equals(relativeDirectories[index])) {
					lastCommonRoot = index;
				} else {
					break;
					// If we didn't find a common prefix then throw
				}
			}
			if (lastCommonRoot != -1) {
				// Build up the relative path
				relativePath = new StringBuilder();
				// Add on the ..
				for (index = lastCommonRoot + 1; index < absoluteDirectories.length; index++) {
					if (absoluteDirectories[index].length() > 0) {
						relativePath.append("../");
					}
				}
				for (index = lastCommonRoot + 1; index < relativeDirectories.length - 1; index++) {
					relativePath.append(relativeDirectories[index] + "/");
				}
				relativePath
						.append(relativeDirectories[relativeDirectories.length - 1]);
			}
		}
		return relativePath == null ? null : relativePath.toString();
	}
}
