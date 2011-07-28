package uk.co.codemonkey.concordion.matrix;

import org.concordion.api.AbstractCommand;
import org.concordion.api.CommandCall;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;

public class PopulateMatrix extends AbstractCommand {
	@Override
	public void setUp(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
		Table table = new Table(commandCall.getElement());
		evaluator.setVariable(commandCall.getExpression(), table.toMatrix());
	}
}