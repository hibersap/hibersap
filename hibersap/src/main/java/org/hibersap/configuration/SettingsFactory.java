package org.hibersap.configuration;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.execution.Executor;
import org.hibersap.execution.jco.JCoExecutor;

public class SettingsFactory implements Serializable
{
  private static final Log LOG = LogFactory.getLog(SettingsFactory.class);

  public static Settings create(Properties props)
  {
    Settings settings = new Settings();

    Class<? extends Executor> executorClass = getExecutorClass(props);
    settings.setExecutorClass(executorClass);

    return settings;
  }

  private static Class<? extends Executor> getExecutorClass(Properties props)
  {
    String executorClassName = props.getProperty(Environment.EXECUTOR);
    Class<? extends Executor> executorClass;

    if (StringUtils.isEmpty(executorClassName))
    {
      Class<JCoExecutor> defaultExecutor = JCoExecutor.class;
      LOG.info("No executor class specified in properties. Default executor "
          + defaultExecutor.getName() + " will be used");
      executorClass = defaultExecutor;
    }
    else
    {
      executorClass = getExecutorClassForName(executorClassName);
    }
    return executorClass;
  }

  @SuppressWarnings("unchecked")
  private static Class<? extends Executor> getExecutorClassForName(String executorClassName)
  {
    Class<? extends Executor> executorClass;
    try
    {
      Class<?> clazz = Class.forName(executorClassName);
      executorClass = (Class<? extends Executor>) clazz;
    }
    catch (ClassNotFoundException e)
    {
      throw new HibersapException("Executor class " + executorClassName
          + " not found in classpath.", e);
    }
    catch (ClassCastException e)
    {
      throw new HibersapException("The executor class specified with property "
          + Environment.EXECUTOR + " must implement " + Executor.class.getName(), e);
    }
    return executorClass;
  }
}
