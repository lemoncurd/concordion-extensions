package spec.uk.co.codemonkey.concordion.specLinker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SpecTestHelper {

	public static void createSpec(File parent, String child) throws IOException {
		createSpec(parent, child, null);
	}
	
	public static void createSpec(File parent, String child, String title) throws IOException {
		File file = new File(parent, child);
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		FileWriter fw = new FileWriter(file);
		fw.write("<html><head>");
		if(title != null) {
			fw.write("<title>"+ title + "</title>");			
		}
		fw.write("</head><body></body></html>");
		fw.close();
		file.deleteOnExit();
	}
	
	public static File createSpecDirectory(String directory) {
		String tmpDir = System.getProperty("java.io.tmpdir");
		tmpDir = tmpDir + File.separator + "SpecLinkerTest" + File.separator + directory;
		File file = new File(tmpDir);
		deleteDirectory(file);
		file.deleteOnExit();
		file.mkdirs();
		return file;
	}

	public static boolean docContainsLink(File file, ElementTest test) throws Exception {
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = docBuilder.parse(file);

		NodeList anchors = document.getElementsByTagName("a");
		for(int i=0; i< anchors.getLength(); i++ ){
			Element anchor = (Element) anchors.item(i);
			if(test.isTrue(anchor)) {
				return true;
			}
		}
		return false;
	}
	
	public static ElementTest withText(final String text) {
		return new ElementTest() {
			@Override
			public boolean isTrue(Element elem) {
				return text.equals(elem.getTextContent());
			}
		};
	}
	
	public static ElementTest withHref(final String href) {
		return new ElementTest() {
			@Override
			public boolean isTrue(Element elem) {
				return href.equals(elem.getAttribute("href"));
			}
		};
	}
	
	private static void deleteDirectory(File directory){
		if(!directory.exists()) {
			return;
		}
		for(File file : directory.listFiles()) {
			if(file.isDirectory()) {
				deleteDirectory(file);
			}
			file.delete();
		}
		directory.delete();
	}
	
	public static interface ElementTest {
		boolean isTrue(Element elem);
	}
	
	
	
}
