package org.hibersap;

public class InternalHiberSapException extends HibersapException {

	public InternalHiberSapException(final String msg) {
		super(msg);
	}

	public InternalHiberSapException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	private static final long serialVersionUID = 1L;

}
