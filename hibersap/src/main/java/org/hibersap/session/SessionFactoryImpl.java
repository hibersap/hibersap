package org.hibersap.session;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.hibersap.configuration.Configuration;
import org.hibersap.configuration.Settings;
import org.hibersap.mapping.model.BapiMapping;

public class SessionFactoryImpl implements SessionFactory
{
  final Properties properties;

  private final Settings settings;

  private Map<Class<?>, BapiMapping> bapiMappings;

  ConverterCache converterCache;

  public SessionFactoryImpl(Configuration configuration, Settings settings)
  {
    this.settings = settings;
    properties = new Properties();
    properties.putAll(configuration.getProperties());
    bapiMappings = Collections.unmodifiableMap(configuration.getBapiMappings());
    this.converterCache = new ConverterCache();
  }

  public Map<Class<?>, BapiMapping> getBapiMappings()
  {
    return bapiMappings;
  }

  public Session getCurrentSession()
  {
    // TODO implement current session context strategies
    return null;
  }

  public Properties getProperties()
  {
    return properties;
  }

  public Settings getSettings()
  {
    return settings;
  }

  public boolean isClosed()
  {
    // TODO Auto-generated method stub
    return false;
  }

  public Session openSession()
  {
    return new SessionImpl(this);
  }

  public ConverterCache getConverterCache()
  {
    return this.converterCache;
  }
}
