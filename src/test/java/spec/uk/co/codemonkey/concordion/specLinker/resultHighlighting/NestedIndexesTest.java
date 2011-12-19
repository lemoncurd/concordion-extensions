package spec.uk.co.codemonkey.concordion.specLinker.resultHighlighting;

import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpec;
import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpecDirectory;
import static spec.uk.co.codemonkey.concordion.specLinker.resultHighlighting.ResultHighlightingHelper.writeJunitResults;

import java.io.File;
import java.io.FileWriter;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.w3c.dom.Element;

import spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper;
import spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.ElementTest;
import uk.co.codemonkey.concordion.specLinker.SpecLinker;

@RunWith(ConcordionRunner.class)
public class NestedIndexesTest {

	private File dir;

	@Before
	public void createDirectory() {
		dir = createSpecDirectory("example");
	}
	
	public void specFile(String filename) throws Exception{
		createSpec(dir, filename);		
	}
	
	public void failSpec(String spec) throws Exception {
		String testName = spec.replace("/", ".").replaceFirst(".html$", "Test");
		writeJunitResults(dir, testName, "0", "1");
	}
	
	public String linkClass() throws Exception {
		String testDir = dir.getCanonicalPath();
		new SpecLinker().link(testDir, testDir);
		return linkClass(dir);
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
	
}
