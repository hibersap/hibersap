package org.hibersap.conversion;

import static org.junit.Assert.assertEquals;

import org.hibersap.HibersapException;
import org.junit.Test;

public class ConverterCacheTest
{
    private ConverterCache cache = new ConverterCache();

    @Test
    public void createsOneInstanceOfEachClass()
    {
        cache.getConverter( CharConverter.class );
        assertEquals( 1, cache.getSize() );

        cache.getConverter( CharConverter.class );
        assertEquals( 1, cache.getSize() );

        cache.getConverter( BooleanConverter.class );
        assertEquals( 2, cache.getSize() );
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionOnNullArgument()
    {
        cache.getConverter( null );
    }

    @Test(expected = HibersapException.class)
    public void throwsHibersapExceptionIfNotInstantiable()
    {
        cache.getConverter( Converter.class );
    }
}
