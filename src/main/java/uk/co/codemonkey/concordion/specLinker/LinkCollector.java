package uk.co.codemonkey.concordion.specLinker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.codemonkey.concordion.specLinker.DirectoryWalker.FileListener;

public class LinkCollector implements FileListener {

	private FileTreeNode root;  
	private FileTreeNode curr;
	
	public LinkCollector(File root) {
		this.root = curr = new FileTreeNode(root);
	}
	
	FileTreeNode getFiles() {
		return root;
	}
	
	@Override
	public void indexFound(File file) {
		if(!file.equals(curr.file)) {
		  curr = curr.add(file);
		}
	}

	@Override
	public void fileFound(File file) {
		if(!file.equals(curr.file)) {
			curr.add(file);
		}
	}

	@Override
	public void leftScopeOfIndex() {
		curr =  curr.getParent();
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
