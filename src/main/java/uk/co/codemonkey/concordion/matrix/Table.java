package uk.co.codemonkey.concordion.matrix;

import static java.lang.String.format;
import static uk.co.codemonkey.concordion.matrix.MatrixException.errorIf;

import org.concordion.api.Element;

class Table {
	
	private static final String EXPECT_N_COLS = "expected %s cols, got %s";
	private static final String EXPECT_N_ROWS = "expected %s rows, got %s";
	private static final String EXPECT_TABLE_ELEM = "expecting to be called on a <table> element";
	private static final String EXPECT_TR_ELEMENTS = "expected at least one <tr> element";
	private static final String EXPECT_UNIFORM_TABLE = "expected a uniform table with equal numbers of <td> on each row";

	private Element[] rows;

	Table(Element element) {
		errorIf(!element.isNamed("table"), EXPECT_TABLE_ELEM);
		rows = element.getChildElements("tr");
		errorIf(rows.length == 0, EXPECT_TR_ELEMENTS);
	}
	
	Object[][] toMatrix() {
		int numCols = rows[0].getChildElements("td").length;
		Object[][] output = new Object[rows.length][numCols];

		for (int i = 0; i < rows.length; i++) {
			Element[] cols = rows[i].getChildElements("td");
			errorIf(cols.length != numCols, EXPECT_UNIFORM_TABLE);
			
			for (int j = 0; j < cols.length; j++) {
				output[i][j] = cols[j].getText();
			}
		}
		return output;			
	}
	
	void verifyMatrix(Object[][] actuals, Table.ResultsListener listener) {
		errorIf(rows.length != actuals.length, format(EXPECT_N_ROWS, rows.length, actuals.length));
		int numCols = actuals[0].length;

		for (int i = 0; i < rows.length; i++) {
			Element[] cols = rows[i].getChildElements("td");
			errorIf(cols.length != numCols, format(EXPECT_N_COLS, cols.length, actuals[i].length));
			
			for (int j = 0; j < cols.length; j++) {
				assertEquals(cols[j], actuals[i][j], listener);
			}
		}
	}
		
	private void assertEquals(Element cell, Object actual, Table.ResultsListener listener) {
		String expectedText = cell.getText();
		String actualText = actual.toString();
		if (actualText.equals(expectedText)) {
			listener.success(cell, expectedText, actualText);
		} else {
			listener.failure(cell, expectedText, actualText);
		}
	}
	
    static interface ResultsListener {
		void success(Element cell, String expected, String actual);
		void failure(Element cell, String expected, String actual);
	}
	
}