package org.hibersap.configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionFactory;
import org.hibersap.session.SessionFactoryImpl;

/**
 * Abstract Superclass for different configuration strategies. Implements
 * properties / settings handling.
 * 
 * @author cerker
 */
public abstract class Configuration implements Serializable
{
  protected Properties properties = Environment.getProperties();

  protected final Map<Class<?>, BapiMapping> bapiMappingForClass = new HashMap<Class<?>, BapiMapping>();

  public SessionFactory buildSessionFactory()
  {
    return new SessionFactoryImpl(this, buildSettings(properties));
  }

  public Settings buildSettings(Properties props)
  {
    return SettingsFactory.create(props);
  }

  /**
   * Add or change a property.
   */
  public Configuration setProperty(String key, String value)
  {
    properties.put(key, value);
    return this;
  }

  /**
   * Get a property.
   */
  public String getProperty(String key)
  {
    return properties.getProperty(key);
  }

  /**
   * Specify a completely new set of properties
   */
  public Configuration setProperties(Properties properties)
  {
    this.properties = properties;
    return this;
  }

  public Properties getProperties()
  {
    return properties;
  }

  public Map<Class<?>, BapiMapping> getBapiMappings()
  {
    return bapiMappingForClass;
  }
}
