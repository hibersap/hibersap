package org.hibersap.session;

import java.util.Properties;

import org.hibersap.HibersapException;
import org.hibersap.execution.Connection;

public interface Context
{
    void configure( Properties properties )
        throws HibersapException;

    void reset();

    Connection getConnection();
}