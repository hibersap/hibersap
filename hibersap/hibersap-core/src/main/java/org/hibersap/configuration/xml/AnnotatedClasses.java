package org.hibersap.configuration.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {"annotatedClasses"} )
public final class AnnotatedClasses implements Serializable
{
    @XmlElement( name = "annotated-class" )
    protected List<String> annotatedClasses = new ArrayList<String>();

    public List<String> getAnnotatedClasses()
    {
        return this.annotatedClasses;
    }

    public void add( String annotatedClassName )
    {
        annotatedClasses.add( annotatedClassName );
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

        AnnotatedClasses that = ( AnnotatedClasses ) o;

        if ( annotatedClasses != null ? !annotatedClasses.equals( that.annotatedClasses ) :
             that.annotatedClasses != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return annotatedClasses != null ? annotatedClasses.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "AnnotatedClasses{" +
                "annotatedClasses=" + annotatedClasses +
                '}';
    }
}
