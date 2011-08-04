package uk.co.codemonkey.concordion.specLinker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.channels.FileChannel;

public class SpecLinker {

	public void link(String resultDirectory, String specDirectory) throws Exception{
		String rootIndex = "index.html";
		
		createRootIndexIfNecessary(specDirectory, rootIndex);
		
		DirectoryWalker walker = new DirectoryWalker();	
		LinkWriter writer = new LinkWriter(new JunitResults(resultDirectory, specDirectory));
	
		LinkCollector collector = new LinkCollector(specDirectory, rootIndex);
		walker.processDirectory(specDirectory, collector);
//		for(FileLinks links : collector.getLinks()) {
//			writer.addLinks(links.index, links.links);
//		}
		writer.addLinks(collector.getFiles());
	}

	private void createRootIndexIfNecessary(String resultDirectory,
			String rootIndex) throws Exception {
		
		File file = new File(resultDirectory,rootIndex);
		if(!file.exists()) {
			FileChannel source= null;
			FileChannel destination= null;
			try {
				URI indexURI = this.getClass().getResource("/defaultIndex.html").toURI();
				source = new FileInputStream(new File(indexURI)).getChannel();
			    destination = new FileOutputStream(file).getChannel();
                destination.transferFrom(source, 0, source.size());
			}
			finally {
				if(source != null) source.close();
				if(destination != null) destination.close();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		SpecLinker specLinker = new SpecLinker();
		specLinker.link("target/test-output",  "target/test-output/concordion");
	}

	
}
