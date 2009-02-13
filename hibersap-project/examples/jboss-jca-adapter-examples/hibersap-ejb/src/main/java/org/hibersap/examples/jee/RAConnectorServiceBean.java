package org.hibersap.examples.jee;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.Interaction;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.ResourceAdapterMetaData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.annotation.ejb.RemoteBinding;

@Stateless
@RemoteBinding(jndiBinding = RAConnectorService.JNDI_NAME)
@Remote(RAConnectorService.class)
public class RAConnectorServiceBean
    implements RAConnectorService
{
    private static final Log LOG = LogFactory.getLog( RAConnectorServiceBean.class );

    @Resource(mappedName = RA_JNDI_NAME)
    private ConnectionFactory _sapResourceAdapter;

    public MappedRecord getFlightList( final MappedRecord inputRecord )
        throws ResourceException
    {
        if ( _sapResourceAdapter != null )
        {
            LOG.info( "Found resource adapter at " + RA_JNDI_NAME + ": " + _sapResourceAdapter + " = "
                + _sapResourceAdapter.getClass() );

            final ResourceAdapterMetaData metaData = _sapResourceAdapter.getMetaData();

            LOG.info( "Metadata: " + metaData.getAdapterName() + "/" + metaData.getAdapterVendorName() + "/"
                + metaData.getAdapterVersion() );

            final Connection connection = _sapResourceAdapter.getConnection();
            final Interaction interaction = connection.createInteraction();

            try
            {
                return callSAP( connection, interaction, inputRecord );
            }
            finally
            {
                interaction.close();
                connection.close();
            }
        }

        return null;
    }

    private MappedRecord callSAP( final Connection connection, final Interaction interaction,
                                  final MappedRecord inputRecord )
        throws ResourceException
    {
        assert connection != null : "connection != null";
        assert interaction != null : "interaction != null";

        final MappedRecord outputRecord = _sapResourceAdapter.getRecordFactory().createMappedRecord( "EXPORT" );
        interaction.execute( null, inputRecord, outputRecord );

        return outputRecord;
    }
}
