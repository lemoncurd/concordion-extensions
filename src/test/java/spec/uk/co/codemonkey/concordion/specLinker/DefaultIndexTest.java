package spec.uk.co.codemonkey.concordion.specLinker;

import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpec;
import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpecDirectory;
import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.exampleSpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import uk.co.codemonkey.concordion.specLinker.SpecLinker;

@RunWith(ConcordionRunner.class)
public class DefaultIndexTest {

	private String fileString;

	public void runLinker(String rootIndex) throws Exception {
		File dir = createSpecDirectory("example");
		exampleSpec(dir, rootIndex, "customRootIndex");
		createSpec(dir, "tests/parent.html");
		createSpec(dir, "tests/child.html");
		
		String testDir = dir.getCanonicalPath();
		new SpecLinker().link(testDir, testDir);
		
		fileString = fileAsString(new File(dir, rootIndex));
		
	}
	
	public boolean isSameIndexFile() throws IOException {
	    return fileString.contains("customRootIndex");
	}
	
	public boolean containsChildLinks() {
		return fileString.contains("tests/parent.html");
	}
	

	private String fileAsString(File file) throws IOException {
		InputStream in = new FileInputStream(file);
		try {
		    return IOUtils.toString(in);
		}
		finally {
			if(in != null) { in.close(); }
		}
		
	}
	
}
