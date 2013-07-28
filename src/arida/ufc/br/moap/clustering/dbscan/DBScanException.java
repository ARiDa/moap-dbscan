package arida.ufc.br.moap.clustering.dbscan;

public class DBScanException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DBScanException() {
	}

	public DBScanException(String msg) {
		super(msg);
	}

	public DBScanException(Throwable cause) {
		super(cause);
	}

	public DBScanException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
