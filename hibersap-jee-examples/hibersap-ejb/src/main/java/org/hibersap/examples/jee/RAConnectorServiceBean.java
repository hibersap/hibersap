package org.hibersap.examples.jee;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;

@Stateless
@RemoteBinding(jndiBinding = RAConnectorService.JNDI_NAME)
@Remote(RAConnectorService.class)
public class RAConnectorServiceBean
    implements RAConnectorService
{
    private static final Logger LOG = Logger.getLogger( RAConnectorServiceBean.class );

    @Resource(mappedName = RA_JNDI_NAME)
    private Object _sapResourceAdapter;

    public boolean adapterExists()
    {
        if ( _sapResourceAdapter != null )
        {
            LOG.info( "Found resource adapter at " + RA_JNDI_NAME + ": " + _sapResourceAdapter + " = "
                + _sapResourceAdapter.getClass() );
        }

        return _sapResourceAdapter != null;
    }
}
