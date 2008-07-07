package org.hibersap.session;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import org.hibersap.configuration.Settings;
import org.hibersap.mapping.model.BapiMapping;

/**
 * The client's interface to the SessionFactory. A SessionFactory is used to
 * create Hibersap sessions.
 * 
 * @author cerker
 */
public interface SessionFactory extends Serializable
{

  /**
   * Open a Session using a newly created connection to SAP.
   * 
   * @return Session
   */
  Session openSession();

  /**
   * Gets a Session using the Current Session Context strategy.
   * 
   * @return
   */
  Session getCurrentSession();

  /**
   * Is this SessionFactory already closed?
   * 
   * @return true, if the SessionFactory is closed.
   */
  boolean isClosed();

  /**
   * Gets this SessionFactory's BapiMappings.
   * 
   * @return A BapiMappings for Classes map.
   */
  Map<Class<?>, BapiMapping> getBapiMappings();

  /**
   * Get Properties.
   * 
   * @return
   */
  Properties getProperties();

  Settings getSettings();

  ConverterCache getConverterCache();
}
