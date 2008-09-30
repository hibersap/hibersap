package org.hibersap.conversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.hibersap.HibersapException;
import org.junit.Test;

public class ConverterCacheTest
{
    private ConverterCache cache = new ConverterCache();

    @Test
    public void createsOneInstanceOfEachClass()
        throws Exception
    {
        cache.getConverter( CharConverter.class );
        assertEquals( 1, cache.getSize() );

        cache.getConverter( CharConverter.class );
        assertEquals( 1, cache.getSize() );

        cache.getConverter( BooleanConverter.class );
        assertEquals( 2, cache.getSize() );
    }

    @Test
    public void throwsExceptionOnNullArgument()
        throws Exception
    {
        try
        {
            cache.getConverter( null );
            fail();
        }
        catch ( IllegalArgumentException e )
        {
            // expected
        }
    }

    @Test
    public void throwsHibersapExceptionIfNotInstantiable()
        throws Exception
    {
        try
        {
            cache.getConverter( Converter.class );
            fail();
        }
        catch ( HibersapException e )
        {
            // expected
        }
    }
}
