package spec.uk.co.codemonkey.concordion.matrix;

import static java.lang.Integer.parseInt;


public class SetMatrixTest extends AbstractExtensionsTest {
	
	private Object[][] matrix;

	public void printMatrix(Object[][] matrix) {
		this.matrix = matrix;
	}
	
	public Object valueAt(String row, String col) {
		return matrix[parseInt(row)][parseInt(col)];
	}
		
}
