package uk.co.codemonkey.concordion;


import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.internal.listener.AssertResultRenderer;

import uk.co.codemonkey.concordion.matrix.CheckMatrix;
import uk.co.codemonkey.concordion.matrix.PopulateMatrix;

public class ConcordionExtensions implements ConcordionExtension {

	public static final String CUSTOM_NS = "http://codemonkey.co.uk/concordion-extensions";

	@Override
	public void addTo(ConcordionExtender extender) {
		PopulateMatrix myCommand = new PopulateMatrix();
		extender.withCommand(CUSTOM_NS, "setMatrix", myCommand);

		CheckMatrix checkMatrix = new CheckMatrix();
		checkMatrix.setAssertEqualsListener(new AssertResultRenderer());
		extender.withCommand(CUSTOM_NS, "assertMatrixEquals", checkMatrix);		
	}
	
}