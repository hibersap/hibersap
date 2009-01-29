package org.hibersap.generation.bapi;

public class BapiFormatHelper
{
    public static String getCamelCaseSmall( String sapName )
    {
        StringBuffer result = new StringBuffer( "_" );

        if ( sapName == null )
            return result.toString();

        String[] parts = sapName.split( "_" );
        for ( int i = 0; i < parts.length; i++ )
        {
            if ( i == 0 )
            {
                result.append( parts[i].toLowerCase() );
            }
            else
            {
                result.append( parts[i].substring( 0, 1 ).toUpperCase() );
                result.append( parts[i].substring( 1 ).toLowerCase() );
            }
        }
        return result.toString();
    }

    public static String getCamelCaseBig( String sapName )
    {
        StringBuffer result = new StringBuffer( "" );

        if ( sapName == null )
            return result.toString();

        String[] parts = sapName.split( "_" );
        for ( int i = 0; i < parts.length; i++ )
        {
            if ( parts[i].length() > 0 )
            {
                result.append( parts[i].substring( 0, 1 ).toUpperCase() );
                result.append( parts[i].substring( 1 ).toLowerCase() );
            }
        }
        return result.toString();
    }

}
