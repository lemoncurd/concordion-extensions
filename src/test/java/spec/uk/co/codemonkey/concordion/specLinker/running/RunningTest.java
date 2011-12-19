package spec.uk.co.codemonkey.concordion.specLinker.running;

import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpec;
import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpecDirectory;

import java.io.File;
import java.io.IOException;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import uk.co.codemonkey.concordion.specLinker.SpecLinker;

@RunWith(ConcordionRunner.class)
public class RunningTest {

	private String specDirectory;
	private File dir;

	public void createDir(String parentDir, String directory)
			throws IOException {
		String testDir = parentDir + File.separator + directory;
		dir = createSpecDirectory(testDir);
		createSpec(dir, "index");
		createSpec(dir, "child");

		rootDirectory(parentDir, dir);
	}

	public boolean runWithPath(String directory) throws Exception {
		try {
			String resultDirectory = specDirectory + File.separator + directory;
			new SpecLinker().link(resultDirectory,dir.getCanonicalPath());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void rootDirectory(String parentDir, File dir) throws IOException {
		String createdPath = dir.getCanonicalPath();
		int dirStart = createdPath.indexOf(parentDir);
		specDirectory = createdPath.substring(0, dirStart);
	}

}
