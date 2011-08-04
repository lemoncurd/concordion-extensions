package uk.co.codemonkey.concordion.specLinker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import uk.co.codemonkey.concordion.specLinker.DirectoryWalker.FileListener;

public class LinkCollector implements FileListener {

	private Deque<FileLinks> links = new LinkedList<FileLinks>();
	
	FileTreeNode root;  
	FileTreeNode curr;
	
	public LinkCollector(String rootDirectory, String rootIndex) {
		//links.push(new FileLinks(new File(rootDirectory, rootIndex)));
		root = curr = new FileTreeNode(new File(rootDirectory, rootIndex));
	}
	
	Collection<FileLinks> getLinks() {
		return links;
	}
	
	FileTreeNode getFiles() {
		return root;
	}
	
	@Override
	public void indexFound(File file) {
		if(!file.equals(curr.file)) {
		  curr = curr.add(file);
		}
		
//		fileFound(file);
//		links.push(new FileLinks(file));
	}

	@Override
	public void fileFound(File file) {
		if(!file.equals(curr.file)) {
			curr.add(file);
		}
		
//		FileLinks peek = links.peek();
//		if(links.size() != 1 || !peek.index.equals(file)) {
//			peek.links.add(file);
//		}
	}

	@Override
	public void leftScopeOfIndex() {
		curr =  curr.getParent();
//		FileLinks fileLinks = links.pop();
//		links.addLast(fileLinks);
	}
	
	static class FileLinks {
		final File index;
		final List<File> links = new LinkedList<File>();
		
		public FileLinks(File index) {
			this.index = index;
		}
	}

	static class FileTreeNode {
		private final List<FileTreeNode> children = new ArrayList<FileTreeNode>();
		private final File file;
		private final FileTreeNode parent;
		
		public FileTreeNode(File file) {
			this(null, file);
		}
		
		private FileTreeNode(FileTreeNode parent, File file) {
			this.file = file;
			this.parent = parent;
		}
		
		public FileTreeNode add(File child) {
			FileTreeNode childNode = new FileTreeNode(this,child);
			children.add(childNode);
			return childNode;
		}
		
		public FileTreeNode getParent() {
			return parent;
		}
		
		public File getFile() {
			return file;
		}

		public List<FileTreeNode> getChildren() {
			return children;
		}
		
	}
	
}
