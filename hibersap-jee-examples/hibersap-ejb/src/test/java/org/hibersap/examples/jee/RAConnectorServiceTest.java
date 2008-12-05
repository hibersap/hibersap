package org.hibersap.examples.jee;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.naming.Context;
import javax.naming.InitialContext;

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
    public void testExists()
        throws Exception
    {
        assertTrue( _service.adapterExists() );
    }
}
