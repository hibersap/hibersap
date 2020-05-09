package org.hibersap.execution.neo;

import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.execution.Connection;
import org.hibersap.session.Context;

public class NeoContext implements Context {
	
	private String destinationName;

	@Override
	public void configure(SessionManagerConfig config) throws HibersapException {
		this.destinationName = config.getName();
	}

	@Override
	public void close() {
	}

	@Override
	public Connection getConnection() {
		return new NeoConnection(destinationName);
	}
}
