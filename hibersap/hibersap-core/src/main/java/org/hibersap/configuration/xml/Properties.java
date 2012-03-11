package org.hibersap.configuration.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {"properties"} )
public final class Properties implements Serializable
{
    @XmlElement( name = "property" )
    protected List<Property> properties = new ArrayList<Property>();

    public List<Property> getProperties()
    {
        return this.properties;
    }

    public boolean contains( Property property )
    {
        return properties.contains( property );
    }

    public boolean remove( Property property )
    {
        return properties.remove( property );
    }

    public void add( Property property )
    {
        removePropertyWithName( property.getName() );
        properties.add( property );
    }

    public int size()
    {
        return properties.size();
    }

    public void setProperties( List<Property> properties )
    {
        this.properties = properties;
    }

    public void setProperty( String name, String value )
    {
        removePropertyWithName( name );
        properties.add( new Property( name, value ) );
    }

    private void removePropertyWithName( String name )
    {
        for ( Property property : properties )
        {
            if ( property.getName().equals( name ) )
            {
                properties.remove( property );
                break;
            }
        }
    }

    public String getPropertyValue( String name )
    {
        for ( Property property : properties )
        {
            if ( property.getName().equals( name ) )
            {
                return property.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        Properties that = ( Properties ) o;

        if ( properties != null ? !properties.equals( that.properties ) : that.properties != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return properties != null ? properties.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "Properties{" +
                "properties=" + properties +
                '}';
    }
}
