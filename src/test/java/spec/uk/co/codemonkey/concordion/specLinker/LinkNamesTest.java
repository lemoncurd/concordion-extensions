package spec.uk.co.codemonkey.concordion.specLinker;

import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpec;
import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpecDirectory;
import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.docContainsLink;
import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.withText;

import java.io.File;
import java.io.IOException;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.Before;
import org.junit.runner.RunWith;

import uk.co.codemonkey.concordion.specLinker.SpecLinker;

@RunWith(ConcordionRunner.class)
public class LinkNamesTest {

	private static final String INDEX = "index.html";
	private File dir;

	@Before
	public void createExampleDirectory() throws Exception {
		dir = createSpecDirectory("linkNames");		
		createSpec(dir, INDEX, "index");
	}
	
	public void runLinker() throws Exception {
		String testDir = dir.getCanonicalPath();
		new SpecLinker().link(testDir, testDir);
	}

	public void addChild(String name, String title) throws IOException {
		createSpec(dir, name, title);		
	}

	public void addChild(String name) throws IOException {
		createSpec(dir, name);		
	}
	
	public boolean fileHasLinkWithText(String text) throws Exception {
		return docContainsLink(new File(dir,INDEX), withText(text));
	}
	
}
