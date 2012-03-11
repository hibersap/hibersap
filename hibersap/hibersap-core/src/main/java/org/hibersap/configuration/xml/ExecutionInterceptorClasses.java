package org.hibersap.configuration.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {"executionInterceptorClasses"} )
public final class ExecutionInterceptorClasses implements Serializable
{
    @XmlElement( name = "execution-interceptor-class" )
    protected List<String> executionInterceptorClasses = new ArrayList<String>();

    public List<String> getExecutionInterceptorClasses()
    {
        return this.executionInterceptorClasses;
    }

    public void add( String interceptorClassName )
    {
        executionInterceptorClasses.add( interceptorClassName );
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

        ExecutionInterceptorClasses that = ( ExecutionInterceptorClasses ) o;

        if ( executionInterceptorClasses != null ?
             !executionInterceptorClasses.equals( that.executionInterceptorClasses ) :
             that.executionInterceptorClasses != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return executionInterceptorClasses != null ? executionInterceptorClasses.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "ExecutionInterceptorClasses{" +
                "executionInterceptorClasses=" + executionInterceptorClasses +
                '}';
    }
}
