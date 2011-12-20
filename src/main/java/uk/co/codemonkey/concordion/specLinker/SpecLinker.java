package uk.co.codemonkey.concordion.specLinker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.channels.FileChannel;

import org.apache.commons.io.IOUtils;

public class SpecLinker {

	private static final String DEFAULT_INDEX_HTML = "/specLinkerDefaultIndex.html";
	private static final String ROOT_INDEX = "index.html";

	public void link(String resultDirectory, String specDirectory) throws Exception{
		File root = createRootIndexIfNecessary(specDirectory);
		
		DirectoryWalker walker = new DirectoryWalker();	
		LinkCollector collector = new LinkCollector(root);
		walker.processDirectory(specDirectory, collector);

		JunitResults junitResults = new JunitResults(resultDirectory, specDirectory);
		LinkWriter writer = new LinkWriter(junitResults);
		writer.addLinks(collector.getFiles());
	}

	private File createRootIndexIfNecessary(String resultDirectory) throws Exception {
		File file = new File(resultDirectory,ROOT_INDEX);
		if(!file.exists()) {
			copyFile(this.getClass().getResource(DEFAULT_INDEX_HTML),file);
		}
		return file;
	}

    private void copyFile(URL sourceUrl, File dest) throws IOException {
    	if(!dest.exists()) {
            dest.createNewFile();
        }
        InputStream in = sourceUrl.openStream();
        OutputStream out = new FileOutputStream(dest);
        try {
        	IOUtils.copy(in,out);
        }
        finally {
            if(in != null) { in.close(); }
            if(out != null) { out.close(); }
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
