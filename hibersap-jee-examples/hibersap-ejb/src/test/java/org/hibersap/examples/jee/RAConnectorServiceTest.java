package org.hibersap.examples.jee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.resource.cci.MappedRecord;

import net.sf.sapbapijca.adapter.cci.MappedRecordImpl;

import org.hibersap.BapiConstants;
import org.hibersap.examples.flightlist.FlightListConstants;
import org.junit.BeforeClass;
import org.junit.Test;

public class RAConnectorServiceTest
{
    private static RAConnectorService _service;

    @BeforeClass
    public static void setUp()
        throws Exception
    {
        final Context naming = new InitialContext();

        _service = (RAConnectorService) naming.lookup( RAConnectorService.JNDI_NAME );
        assertNotNull( _service );
        naming.close();
    }

    @Test
    public void testEmptyInputRecordCausesError()
        throws Exception
    {
        final MappedRecord resultRecord = _service.getFlightList( new MappedRecordImpl( "Input" ) );

        assertNotNull( resultRecord );
        final MappedRecord returnRecord = (MappedRecord) resultRecord.get( BapiConstants.RETURN );
        assertNotNull( returnRecord );

        final String type = (String) returnRecord.get( BapiConstants.TYPE );
        assertEquals( "E", type );
    }

    @Test
    public void testFlightList()
        throws Exception
    {
        final MappedRecord resultRecord = _service.getFlightList( createInputRecord() );

        assertNotNull( resultRecord );
        final MappedRecord returnRecord = (MappedRecord) resultRecord.get( BapiConstants.RETURN );
        assertNotNull( returnRecord );

        final String type = (String) returnRecord.get( BapiConstants.TYPE );
        assertEquals( returnRecord.toString(), "S", type );

    }

    @SuppressWarnings("unchecked")
    private MappedRecordImpl createInputRecord()
    {
        final MappedRecordImpl record = new MappedRecordImpl( "Input" );

        record.put( FlightListConstants.FROMCOUNTRYKEY, "DE" );
        record.put( FlightListConstants.FROMCITY, "Frankfurt" );
        record.put( FlightListConstants.TOCOUNTRYKEY, "DE" );
        record.put( FlightListConstants.TOCITY, "Berlin" );
        record.put( FlightListConstants.AIRLINECARRIER, null );
        record.put( FlightListConstants.AFTERNOON, "" ); // Means false, "X" is true
        record.put( FlightListConstants.MAXREAD, 10 );
        return record;
    }
}
