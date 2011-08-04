package spec.uk.co.codemonkey.concordion.specLinker;

import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpec;
import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpecDirectory;
import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.docContainsLink;
import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.withHref;

import java.io.File;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import uk.co.codemonkey.concordion.specLinker.SpecLinker;

@RunWith(ConcordionRunner.class)
public class ChildSpecificationsTest {

	private File dir;

	public void runLinker(String directory, String index, String child) throws Exception {
		dir = createSpecDirectory(directory);
		createSpec(dir, index);
		createSpec(dir, child);
		
		String testDir = dir.getCanonicalPath();
		new SpecLinker().link(testDir, testDir);
	}

	public boolean fileHasLinkTo(String index, String child) throws Exception {
		return docContainsLink(new File(dir, index), withHref(child));
	}
	
}
