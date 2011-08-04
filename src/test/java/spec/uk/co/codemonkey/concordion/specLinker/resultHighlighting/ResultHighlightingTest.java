package spec.uk.co.codemonkey.concordion.specLinker.resultHighlighting;

import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpec;
import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpecDirectory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import org.w3c.dom.Element;

import spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper;
import spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.ElementTest;
import uk.co.codemonkey.concordion.specLinker.SpecLinker;

@RunWith(ConcordionRunner.class)
public class ResultHighlightingTest {

	SpecLinker specLinker = new SpecLinker();
	
	public String linkClass(String passes, String fails) throws Exception {
		File root = setupSpecHierarchy("ChildTest");
		writeJunitResults(root, "ChildTest", passes,fails);
		String testDir = root.getCanonicalPath();
		specLinker.link(testDir, testDir);
		
		return linkClass(root);
	}

	private String linkClass(File root) throws Exception {
		final String[] container = new String[1];
		SpecTestHelper.docContainsLink(new File(root, "index.html"), new ElementTest() {
			@Override
			public boolean isTrue(Element elem) {
				String className  = elem.getAttribute("class");
				container[0] = className == null ? "" : className;
				return true;
			}
		});
		return container[0];
	}

	private void writeJunitResults(File dir, String testName, String passes, String fails) throws Exception {
		int tests = Integer.parseInt(passes) + Integer.parseInt(fails);
		
		File file = new File(dir, "TEST-" + testName + ".xml");
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		fileWriter.write("<testsuite errors=\"0\" failures=\"" + fails + "\" hostname=\"local\" name=\"" + testName + "\" tests=\"" + tests + "\">");
		fileWriter.write("</testsuite>");
		fileWriter.close();
	}

	private File setupSpecHierarchy(String testName) throws IOException {
		String specFile = testName.replace(".", File.separator).replaceFirst("Test$", ".html");
		File dir = createSpecDirectory("example");
		createSpec(dir, "index.html");
		createSpec(dir, specFile);
		return dir;
	}

}
