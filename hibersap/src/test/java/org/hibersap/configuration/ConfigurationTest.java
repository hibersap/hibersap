package org.hibersap.configuration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Test;

public class ConfigurationTest
{
  private Configuration config = new Configuration()
  {
  };

  @Test
  public void getProperties() throws Exception
  {
    // initializes with system properties
    assertNotNull(config.getProperty("java.runtime.name"));

    // overwrites property
    config.setProperty("java.runtime.name", "test");
    assertEquals("test", config.getProperty("java.runtime.name"));

    // overwrites all properties
    Properties properties = new Properties();
    properties.setProperty("testkey", "testvalue");
    config.setProperties(properties);
    assertEquals(1, config.getProperties().size());
    assertEquals("testvalue", config.getProperty("testkey"));
  }
}
