package spec.uk.co.codemonkey.concordion.matrix;

import static java.lang.System.setProperty;
import static uk.co.codemonkey.concordion.ConcordionExtensions.CUSTOM_NS;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.After;
import org.junit.runner.RunWith;

import test.concordion.ProcessingResult;
import test.concordion.TestRig;
import uk.co.codemonkey.concordion.ConcordionExtensions;

@RunWith(ConcordionRunner.class)
public abstract class AbstractExtensionsTest {

	public void addExtension() {
		setProperty("concordion.extensions", ConcordionExtensions.class.getCanonicalName());
	}
    
    @After
    public void tearDown() throws Exception {
        System.clearProperty("concordion.extensions");
    }
	
    public ProcessingResult process(String fragment) {
        return new TestRig()
            .withFixture(this)
            .processFragment(prependWithNamespace(fragment));
    }

	private String prependWithNamespace(String fragment) {
		return "<div xmlns:myext=\"" + CUSTOM_NS + "\">" + fragment + "</div>";
	}

}
