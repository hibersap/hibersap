package org.hibersap.session;

import java.util.Properties;

import org.hibersap.HibersapException;
import org.hibersap.execution.Connection;

public interface Context
{
    static final String HIBERSAP_JCO_PREFIX = "hibersap.jco";

	void configure( Properties properties )
        throws HibersapException;

    void reset();

    Connection getConnection();
}