package org.hibersap.examples.flightdetail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.hibersap.examples.flightlist.Flight;
import org.hibersap.execution.jco.JCoDataProvider;
import org.junit.Test;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.Environment;

public class FlightListJCo
{
    @Test
    public void jcoGetFlightList()
        throws JCoException
    {
        FlightCriteria criteria = new FlightCriteria( "DE", "Frankfurt", "DE", "Berlin", "LH", false, 10 );
        registerDataProvider();

        // Holen einer Verbindung
        JCoDestination destination = JCoDestinationManager.getDestination( "F46" );
        // Holen einer Connection zum SAP-System aus dem Pool
        JCoContext.begin( destination );
        try
        {
            JCoFunction function = destination.getRepository().getFunction( "BAPI_SFLIGHT_GETLIST" );
            if ( function == null )
                throw new RuntimeException( "..." );

            JCoParameterList importParameters = function.getImportParameterList();
            importParameters.setValue( "FROMCOUNTRYKEY", criteria.getFromCountry() );
            importParameters.setValue( "FROMCITY", criteria.getFromCity() );
            importParameters.setValue( "TOCOUNTRYKEY", criteria.getToCountry() );
            importParameters.setValue( "TOCITY", criteria.getToCity() );
            importParameters.setValue( "AIRLINECARRIER", criteria.getCarrier() );
            importParameters.setValue( "AFTERNOON", criteria.isAfternoon() ? "X" : "" );
            importParameters.setValue( "MAXREAD", criteria.getMaxRead() );

            function.execute( destination );

            JCoParameterList exportParameters = function.getExportParameterList();

            // Fehlerbehandlung
            JCoStructure returnStructure = exportParameters.getStructure( "RETURN" );
            String type = returnStructure.getString( "TYPE" );
            if ( "E".equals( type ) || "A".equals( type ) )
            {
                String nachricht = returnStructure.getString( "MESSAGE" );
                throw new RuntimeException( "SAP-Fehler: " + nachricht );
            }

            // Tabelle auslesen
            JCoParameterList tableParameters = function.getTableParameterList();
            JCoTable table = tableParameters.getTable( "FLIGHTLIST" );
            List<Flight> flights = new ArrayList<Flight>();
            for ( int i = 0; i < table.getNumRows(); i++ )
            {
                Flight flight = new Flight();
                table.setRow( i );
                flight.setConnectionId( table.getString( "CONNID" ) );
                Date flightDate = joinDateAndTime( table.getDate( "FLDATE" ), table.getDate( "DEPTIME" ) );
                flight.setFlightDate( flightDate );
                flights.add( flight );
            }

            for ( Iterator<Flight> iterator = flights.iterator(); iterator.hasNext(); )
            {
                Flight flight = iterator.next();
                System.out.println( "Flight: " + flight.getConnectionId() + " " + flight.getFlightDate() );
            }
        }
        finally
        {
            // Zurückgeben der Connection an den Pool
            JCoContext.end( destination );
        }

    }

    private void registerDataProvider()
    {
        JCoDataProvider provider = new JCoDataProvider();
        Properties properties = new Properties();
        properties.setProperty( DestinationDataProvider.JCO_ASHOST, "10.20.80.76" );
        properties.setProperty( DestinationDataProvider.JCO_CLIENT, "800" );
        properties.setProperty( DestinationDataProvider.JCO_USER, "sapuser" );
        properties.setProperty( DestinationDataProvider.JCO_PASSWD, "password" );
        properties.setProperty( DestinationDataProvider.JCO_LANG, "DE" );
        properties.setProperty( DestinationDataProvider.JCO_SYSNR, "00" );
        properties.setProperty( DestinationDataProvider.JCO_POOL_CAPACITY, "1" );
        provider.addDestination( "F46", properties );
        Environment.registerDestinationDataProvider( provider );
    }

    private Date joinDateAndTime( Date date, Date time )
    {
        Calendar calTime = Calendar.getInstance();
        calTime.setTime( time );
        Calendar calDate = Calendar.getInstance();
        calDate.setTime( date );
        calDate.set( Calendar.HOUR, calTime.get( Calendar.HOUR ) );
        calDate.set( Calendar.MINUTE, calTime.get( Calendar.MINUTE ) );
        return calDate.getTime();
    }

    private class FlightCriteria
    {
        private final String fromCountry;

        private final String fromCity;

        private final String toCountry;

        private final String toCity;

        private final String carrier;

        private final boolean afternoon;

        private final int maxRead;

        public String getFromCountry()
        {
            return fromCountry;
        }

        public String getFromCity()
        {
            return fromCity;
        }

        public String getToCountry()
        {
            return toCountry;
        }

        public String getToCity()
        {
            return toCity;
        }

        public String getCarrier()
        {
            return carrier;
        }

        public boolean isAfternoon()
        {
            return afternoon;
        }

        public int getMaxRead()
        {
            return maxRead;
        }

        public FlightCriteria( String fromCountry, String fromCity, String toCountry, String toCity, String carrier,
                               boolean afternoon, int maxRead )
        {
            this.fromCountry = fromCountry;
            this.fromCity = fromCity;
            this.toCountry = toCountry;
            this.toCity = toCity;
            this.carrier = carrier;
            this.afternoon = afternoon;
            this.maxRead = maxRead;
        }

    }
}
