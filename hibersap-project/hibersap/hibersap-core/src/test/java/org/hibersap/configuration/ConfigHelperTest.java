package org.hibersap.configuration;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.hibersap.HibersapException;
import org.junit.Test;

public class ConfigHelperTest
{
    @Test
    public void testGetResourceAsStream()
    {
        InputStream stream = ConfigHelper.getResourceAsStream( "META-INF/hibersap.xml" );
        assertNotNull( stream );
    }

    @Test(expected = HibersapException.class)
    public void testGetResourceAsStreamNotFound()
    {
        ConfigHelper.getResourceAsStream( "notExistent" );
    }
}
