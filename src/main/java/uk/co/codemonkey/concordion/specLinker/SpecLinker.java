package uk.co.codemonkey.concordion.specLinker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.channels.FileChannel;

public class SpecLinker {

	private static final String DEFAULT_INDEX_HTML = "/specLinkerDefaultIndex.html";
	private static final String ROOT_INDEX = "index.html";

	public void link(String resultDirectory, String specDirectory) throws Exception{
		File root = createRootIndexIfNecessary(specDirectory, ROOT_INDEX);
		
		DirectoryWalker walker = new DirectoryWalker();	
		LinkCollector collector = new LinkCollector(root);
		walker.processDirectory(specDirectory, collector);

		JunitResults junitResults = new JunitResults(resultDirectory, specDirectory);
		LinkWriter writer = new LinkWriter(junitResults);
		writer.addLinks(collector.getFiles());
	}

	private File createRootIndexIfNecessary(String resultDirectory, String rootIndex) throws Exception {
		File file = new File(resultDirectory,rootIndex);
		if(!file.exists()) {
			copyDefaultInto(file);
		}
		return file;
	}

	private void copyDefaultInto(File file) throws Exception {
		FileChannel source= null;
		FileChannel destination= null;
		try {
			URI indexURI = this.getClass().getResource(DEFAULT_INDEX_HTML).toURI();
			source = new FileInputStream(new File(indexURI)).getChannel();
		    destination = new FileOutputStream(file).getChannel();
		    destination.transferFrom(source, 0, source.size());
		}
		finally {
			if(source != null) source.close();
			if(destination != null) destination.close();
		}
	}
	
	public static void main(String[] args) throws Exception {
		SpecLinker specLinker = new SpecLinker();
		
		if(args.length < 2) {
			System.err.println(String.format("Usage: java %s <junit result directory> <concordion result directory>", SpecLinker.class.getCanonicalName()));			
		}
		
		specLinker.link(args[0], args[1]);
	}
	
}
