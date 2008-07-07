package org.hibersap.execution.jco;

import java.util.Properties;

import org.hibersap.HibersapException;
import org.hibersap.configuration.Environment;

import com.sap.mw.jco.JCO;

public class JCoPooledConnectionProvider extends JCoConnectionProvider
{
  private String poolName = null;

  private int poolSize = -1;

  @Override
  public void configure(Properties props) throws HibersapException
  {
    super.configure(props);
    poolName = props.getProperty(Environment.CONNECTION_POOL_NAME);
    poolSize = getMaxPoolSize();
    createPoolIfNecessary();
  }

  public JCO.Client getClient()
  {
    return JCO.getClient(getJcoProperties().getProperty(Environment.CONNECTION_POOL_NAME));
  }

  public void releaseClient(JCO.Client client)
  {
    JCO.releaseClient(client);
  }

  private int getMaxPoolSize()
  {
    String maxSizeStr = getJcoProperties().getProperty(Environment.CONNECTION_POOL_SIZE);
    int maxSize = Integer.parseInt(maxSizeStr);
    return maxSize;
  }

  private void createPoolIfNecessary()
  {
    JCO.Pool pool = JCO.getClientPoolManager().getPool(poolName);
    if (pool == null)
    {
      JCO.addClientPool(poolName, poolSize, getJcoProperties());
    }
  }
}
