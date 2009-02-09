package org.hibersap.configuration;

import java.util.Properties;

import org.hibersap.HibersapException;
import org.hibersap.execution.Connection;
import org.hibersap.session.Context;

public class DummyContext
    implements Context
{

    private static final long serialVersionUID = 1L;

    public void configure( Properties properties )
        throws HibersapException
    {
        // do nothing
    }

    public Connection getConnection()
    {
        return null;
    }

    public void reset()
    {
        // do nothing
    }

}
