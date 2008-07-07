package org.hibersap;

public class HibersapException extends RuntimeException {
	
	public HibersapException(Throwable root) {
		super(root);
	}

	public HibersapException(String string, Throwable root) {
		super(string, root);
	}

	public HibersapException(String s) {
		super(s);
	}
}
