package org.hibersap.jbossdeployer.metadata;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name = "baseType")
public class BaseElement implements Serializable
{
   private static final long serialVersionUID = 1;

   private Object value;

   public Object getValue()
   {
      return value;
   }

   @XmlValue
   public void setValue(Object value)
   {
      this.value = value;
   }
}