package uk.co.codemonkey.concordion.matrix;

import static java.lang.String.format;
import static org.concordion.api.Result.SUCCESS;
import static uk.co.codemonkey.concordion.matrix.MatrixException.errorIf;

import org.concordion.api.AbstractCommand;
import org.concordion.api.CommandCall;
import org.concordion.api.Element;
import org.concordion.api.Evaluator;
import org.concordion.api.Result;
import org.concordion.api.ResultRecorder;
import org.concordion.api.listener.AssertEqualsListener;
import org.concordion.api.listener.AssertFailureEvent;
import org.concordion.api.listener.AssertSuccessEvent;

public class CheckMatrix extends AbstractCommand {

	private static final String EXPECT_OBJECT_ARR = "expected %s to return an Object[][]";
	private AssertEqualsListener listener;

    public void setAssertEqualsListener(AssertEqualsListener listener) {
        this.listener = listener;
    }
	
	@Override
	public void verify(CommandCall commandCall, Evaluator evaluator, final ResultRecorder resultRecorder) {
		Object result = evaluator.evaluate(commandCall.getExpression());
		errorIf(!(result instanceof Object[][]), format(EXPECT_OBJECT_ARR, commandCall.getExpression()));
		
		Table expected = new Table(commandCall.getElement());
		expected.verifyMatrix((Object[][]) result, new Table.ResultsListener() {
			
			public void success(Element cell, String expected, String actual) {
				resultRecorder.record(SUCCESS);
				listener.successReported( new AssertSuccessEvent(cell));
			}
			
			public void failure(Element cell, String expected, String actual) {
				resultRecorder.record(Result.FAILURE);
				listener.failureReported(new AssertFailureEvent(cell, expected, actual));
			}
		});
	}
}