package org.hibersap.configuration.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {"bapiInterceptorClasses"} )
public final class BapiInterceptorClasses implements Serializable
{
    @XmlElement( name = "bapi-interceptor-class" )
    protected List<String> bapiInterceptorClasses = new ArrayList<String>();

    public List<String> getBapiInterceptorClasses()
    {
        return this.bapiInterceptorClasses;
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

        BapiInterceptorClasses that = ( BapiInterceptorClasses ) o;

        if ( bapiInterceptorClasses != null ? !bapiInterceptorClasses.equals( that.bapiInterceptorClasses ) :
             that.bapiInterceptorClasses != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return bapiInterceptorClasses != null ? bapiInterceptorClasses.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "BapiInterceptorClasses{" +
                "bapiInterceptorClasses=" + bapiInterceptorClasses +
                '}';
    }
}
