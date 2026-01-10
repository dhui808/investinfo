package cftc;

public class InvestInfoException extends RuntimeException {

	private static final long serialVersionUID = -8031051666317869482L;

	public InvestInfoException(String errMsg) {
		super(errMsg);
	}
	
	public InvestInfoException(Exception e) {
		super(e);
	}
}
