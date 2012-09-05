package org.hibersap.it.jco.monitoring;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.execution.jco.util.JCoMonitoringUtil;
import org.hibersap.execution.jco.util.MonitoringData;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class JCoMonitoringUtilTest
{
    private final JCoMonitoringUtil util = new JCoMonitoringUtil();

    @Before
    public void setUp() throws Exception
    {
        // Hibersap is just used here to register the JCo destination, we don't really need the SessionManager
        new AnnotationConfiguration( "A12" ).buildSessionManager();
    }

    @Test
    public void showMonitoringData() throws Exception
    {
        MonitoringData data = util.getMonitoringData( "A12" );

        assertThat( data.getSystemDescription() ).startsWith( "DEST:                  A12" );
    }
}
