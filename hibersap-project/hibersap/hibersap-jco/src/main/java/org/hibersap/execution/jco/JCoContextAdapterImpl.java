package org.hibersap.execution.jco;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;

public class JCoContextAdapterImpl
    implements JCoContextAdapter
{
    public void begin( JCoDestination destination )
    {
        JCoContext.begin( destination );
    }

    public void end( JCoDestination destination )
        throws JCoException
    {
        JCoContext.end( destination );
    }

    public boolean isStateful( JCoDestination destination )
    {
        return JCoContext.isStateful( destination );
    }
}
