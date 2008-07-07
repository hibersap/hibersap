package org.hibersap.execution.jco;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.hibersap.HibersapException;
import org.hibersap.configuration.Environment;

import com.sap.mw.jco.IRepository;
import com.sap.mw.jco.JCO;

public abstract class JCoConnectionProvider
{
  private final Properties jcoProperties = new Properties();

  public void configure(Properties props) throws HibersapException
  {
    jcoProperties.clear();
    for (Object key : props.keySet())
    {
      String keyStr = (String) key;
      if (keyStr.startsWith("hibersap.jco.client"))
      {
        String jcoKey = keyStr.substring("hibersap.".length());
        jcoProperties.put(jcoKey, props.getProperty(keyStr));
      }
    }
    String repository = props.getProperty(Environment.REPOSITORY_NAME);
    if (StringUtils.isEmpty(repository))
    {
      throw new HibersapException("A repository name must be specified in property "
          + Environment.REPOSITORY_NAME);
    }
    jcoProperties.put(Environment.REPOSITORY_NAME, repository);
  }

  protected Properties getJcoProperties()
  {
    return this.jcoProperties;
  }

  public IRepository getRepository(JCO.Client client)
  {
    String name = getJcoProperties().getProperty(Environment.REPOSITORY_NAME);
    return JCO.createRepository(name, client);
  }
}
