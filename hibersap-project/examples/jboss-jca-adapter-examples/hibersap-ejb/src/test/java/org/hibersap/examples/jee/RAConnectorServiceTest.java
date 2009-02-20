package org.hibersap.examples.jee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.resource.cci.IndexedRecord;
import javax.resource.cci.MappedRecord;

import net.sf.sapbapijca.adapter.cci.MappedRecordImpl;

import org.hibersap.bapi.BapiConstants;
import org.hibersap.examples.flightlist.FlightListConstants;
import org.junit.Test;

public class RAConnectorServiceTest
{
    private static RAConnectorService _service;

    // @BeforeClass
    public static void setUp()
        throws Exception
    {
        final Context naming = new InitialContext();

        _service = (RAConnectorService) naming.lookup( RAConnectorService.JNDI_NAME );
        assertNotNull( _service );
        naming.close();
    }

    @Test
    public void testEmpty()
    {
        // to avoid errors when the other tests are disabled
    }

    // @Test
    public void testEmptyInputRecordCausesError()
        throws Exception
    {
        final MappedRecord resultRecord = _service
            .getFlightList( new MappedRecordImpl( FlightListConstants.BAPI_NAME ) );

        assertNotNull( resultRecord );
        final MappedRecord returnRecord = (MappedRecord) resultRecord.get( BapiConstants.RETURN );
        assertNotNull( returnRecord );

        final String type = (String) returnRecord.get( BapiConstants.TYPE );
        assertEquals( "E", type );
    }

    // @Test
    public void testFlightList()
        throws Exception
    {
        final MappedRecord resultRecord = _service.getFlightList( createInputRecord() );

        assertNotNull( resultRecord );
        final MappedRecord returnRecord = (MappedRecord) resultRecord.get( BapiConstants.RETURN );
        assertNotNull( returnRecord );

        final String type = (String) returnRecord.get( BapiConstants.TYPE );
        assertEquals( returnRecord.toString(), "S", type );

        final IndexedRecord tableDataRecord = (IndexedRecord) resultRecord.get( FlightListConstants.FLIGHTLIST );
        assertNotNull( tableDataRecord );
        assertEquals( 2, tableDataRecord.size() );

        MappedRecord rowRecord = (MappedRecord) tableDataRecord.get( 0 );
        String connid = (String) rowRecord.get( FlightListConstants.CONNID );
        assertEquals( "2402", connid );

        rowRecord = (MappedRecord) tableDataRecord.get( 1 );
        connid = (String) rowRecord.get( FlightListConstants.CONNID );
        assertEquals( "2402", connid );
    }

    @SuppressWarnings("unchecked")
    private MappedRecordImpl createInputRecord()
    {
        final MappedRecordImpl record = new MappedRecordImpl( FlightListConstants.BAPI_NAME );

        record.put( FlightListConstants.FROMCOUNTRYKEY, "DE" );
        record.put( FlightListConstants.FROMCITY, "Frankfurt" );
        record.put( FlightListConstants.TOCOUNTRYKEY, "DE" );
        record.put( FlightListConstants.TOCITY, "Berlin" );
        record.put( FlightListConstants.AIRLINECARRIER, null );
        record.put( FlightListConstants.AFTERNOON, "" ); // Means false, "X" is
        // true
        record.put( FlightListConstants.MAXREAD, 10 );
        return record;
    }
}
