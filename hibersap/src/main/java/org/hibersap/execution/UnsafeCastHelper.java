package org.hibersap.execution;

import java.util.Collection;
import java.util.Map;

public class UnsafeCastHelper
{
    private UnsafeCastHelper()
    {
        // use static methods
    }

    @SuppressWarnings("unchecked")
    public static Collection<Object> castToCollection( Object value )
    {
        return (Collection<Object>) value;
    }

    @SuppressWarnings("unchecked")
    public static Collection<Map<String, Object>> castToCollectionOfMaps( Object value )
    {
        return (Collection<Map<String, Object>>) value;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> castToMap( Object value )
    {
        return Map.class.cast( value );
    }
}
