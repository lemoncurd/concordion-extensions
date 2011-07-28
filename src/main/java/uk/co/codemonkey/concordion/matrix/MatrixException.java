package uk.co.codemonkey.concordion.matrix;

public class MatrixException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	static void errorIf(boolean condition, String error) {
		if(condition)
			throw new MatrixException(error);
	}
	
	private MatrixException(String error) {
		super(error);
	}
}
