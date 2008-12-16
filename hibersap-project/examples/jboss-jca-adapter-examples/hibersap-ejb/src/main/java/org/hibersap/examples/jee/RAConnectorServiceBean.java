package org.hibersap.examples.jee;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.Interaction;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.ResourceAdapterMetaData;

import net.sf.sapbapijca.adapter.cci.InteractionSpecImpl;
import net.sf.sapbapijca.adapter.cci.MappedRecordImpl;

import org.apache.log4j.Logger;
import org.hibersap.examples.flightlist.FlightListConstants;
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

        final InteractionSpec iSpec = new InteractionSpecImpl( FlightListConstants.BAPI_NAME );
        final MappedRecord outputRecord = new MappedRecordImpl( "EXPORT" );
        interaction.execute( iSpec, inputRecord, outputRecord );

        return outputRecord;
    }
}
