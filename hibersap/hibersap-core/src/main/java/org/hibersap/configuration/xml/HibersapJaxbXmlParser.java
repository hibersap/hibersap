package org.hibersap.configuration.xml;

import org.hibersap.ConfigurationException;
import org.hibersap.InternalHiberSapException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HibersapJaxbXmlParser
{
    private final JAXBContext jaxbContext;

    public HibersapJaxbXmlParser()
    {
        try
        {
            jaxbContext = JAXBContext.newInstance( HibersapConfig.class );
        }
        catch ( final JAXBException e )
        {
            throw new ConfigurationException( "Cannot not create a JAXB context.", e );
        }
    }

    public HibersapConfig parseResource( final String resourceName )
            throws HibersapParseException
    {
        final InputStream resourceStream = findResource( resourceName );
        return parseResource( resourceStream, resourceName );
    }

    public HibersapConfig parseResource( final InputStream resourceStream, final String resourceName )
            throws HibersapParseException
    {
        Unmarshaller unmarshaller;
        try
        {
            unmarshaller = jaxbContext.createUnmarshaller();
        }
        catch ( final JAXBException e )
        {
            throw new InternalHiberSapException( "Cannot create an unmarshaller. ", e );
        }

        Object unmarshalledObject;
        try
        {
            unmarshalledObject = unmarshaller.unmarshal( resourceStream );
        }
        catch ( final JAXBException e )
        {
            throw new HibersapParseException( "Cannot parse the resource " + resourceName, e );
        }
        if ( unmarshalledObject == null )
        {
            throw new HibersapParseException( "Resource " + resourceName + " is empty." );
        }
        if ( !( unmarshalledObject instanceof HibersapConfig ) )
        {
            throw new HibersapParseException( "Resource " + resourceName
                    + " does not consist of a hibersap specification. I found a "
                    + unmarshalledObject.getClass().getSimpleName() );
        }
        return ( HibersapConfig ) unmarshalledObject;
    }

    private InputStream findResource( final String resourceName )
    {
        URL resource = Thread.currentThread().getContextClassLoader().getResource( resourceName );
        if ( resource == null )
        {
            resource = getClass().getResource( resourceName );
        }
        if ( resource == null )
        {
            throw new InternalHiberSapException( "Cannot locate resource " + resourceName );
        }
        final InputStream resourceStream;
        try
        {
            resourceStream = resource.openStream();
        }
        catch ( final IOException e )
        {
            throw new InternalHiberSapException( "Cannot open resource " + resourceName, e );
        }
        return resourceStream;
    }
}
