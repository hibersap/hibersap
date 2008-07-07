package org.hibersap.execution.jco;


import com.sap.mw.jco.JCO;

public class JCoDirectConnectionProvider extends JCoConnectionProvider
{
  public JCO.Client getClient()
  {
    return JCO.createClient(getJcoProperties());
  }

  public void releaseClient(JCO.Client client)
  {
    client.disconnect();
  }
}
