package uk.co.codemonkey.concordion.specLinker;

import java.io.File;
import java.io.FileFilter;

public class DirectoryWalker {

	private static final String HTML = ".html";

	void processDirectory(String directory, FileListener listener) {
		processDirectory(new File(directory), listener);
	}
		
	private void processDirectory(File directory, FileListener listener) {
		File index = findIndex(directory, listener);
		if(index != null) {
			listener.indexFound(index);
		}
		
		for (File file : directory.listFiles(filter(directory))) {
			if (file.isDirectory()) {
				processDirectory(file, listener);
			}
			else {
			    listener.fileFound(file);
			}
		}
		
		if (index != null) {
			listener.leftScopeOfIndex();
		}
	}

	private File findIndex(File directory, FileListener listener) {
		File index = new File(directory, indexName(directory));
		if (index.exists() && !index.isDirectory()) {
			return index;
		}
		return null;
	}

	private FileFilter filter(final File directory) {
		return new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory() 
				   || (file.getName().endsWith(HTML) && !isIndexFile(directory, file));
			}
		};
	}

	private boolean isIndexFile(File directory, File file) {
		return file.getName().equals(indexName(directory));
	}

	private String indexName(File directory) {
		char[] name = directory.getName().toCharArray();
		name[0] = Character.toUpperCase(name[0]);
		return new String(name) + HTML;
	}

	static interface FileListener {

		void indexFound(File file);

		void fileFound(File file);

		void leftScopeOfIndex();
	};

}
