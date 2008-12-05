package org.hibersap.examples.jee;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.resource.ResourceException;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.ResourceAdapterMetaData;

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
    private ConnectionFactory _sapResourceAdapter;

    public boolean adapterExists()
        throws ResourceException
    {
        if ( _sapResourceAdapter != null )
        {
            LOG.info( "Found resource adapter at " + RA_JNDI_NAME + ": " + _sapResourceAdapter + " = "
                + _sapResourceAdapter.getClass() );

            final ResourceAdapterMetaData metaData = _sapResourceAdapter.getMetaData();

            LOG.info( "Metadata: " + metaData.getAdapterName() + "/" + metaData.getAdapterVendorName() + "/"
                + metaData.getAdapterVersion() );
        }

        return _sapResourceAdapter != null;
    }
}
