package org.hibersap.configuration;

import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.execution.Connection;
import org.hibersap.session.Context;

public class DummyContext implements Context
{
    private static final long serialVersionUID = 1L;

    public void configure( SessionManagerConfig config )
        throws HibersapException
    {
        // do nothing
    }

    public Connection getConnection()
    {
        return null;
    }

    public void close()
    {
        // do nothing
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( obj instanceof DummyContext )
            return true;
        return false;
    }
}
