package spec.uk.co.codemonkey.concordion.matrix;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import test.concordion.ProcessingResult;

@RunWith(ConcordionRunner.class)
public class AssertMatrixEqualsTest extends AbstractExtensionsTest {
	
	
	public Object[][] printMatrix() {
		return new Object[][] {{"a",1}, {"b",2}};
	}
	
	public void displayOutput(ProcessingResult result) {
		throw new RuntimeException(result.getOutputFragmentXML());
	}
}
