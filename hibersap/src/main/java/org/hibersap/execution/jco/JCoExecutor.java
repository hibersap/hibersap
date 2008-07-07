package org.hibersap.execution.jco;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.execution.Executor;
import org.hibersap.session.Session;
import org.hibersap.session.Transaction;

import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.IRepository;
import com.sap.mw.jco.JCO.Client;
import com.sap.mw.jco.JCO.Function;

public class JCoExecutor implements Executor
{
  private static final Log LOG = LogFactory.getLog(AnnotationConfiguration.class);;

  private JCoDirectConnectionProvider connectionProvider;

  private JCoMapper jcoMapper;

  private JCoTransaction transaction = null;

  private Client client;

  private Session session;

  public void configure(Session session)
  {
    this.session = session;
    connectionProvider = new JCoDirectConnectionProvider();
    connectionProvider.configure(session.getSessionFactory().getProperties());
    jcoMapper = new JCoMapper();
  }

  public void execute(String bapiName, Map<String, Object> functionMap)
  {
    getClientIfNecessary();

    IRepository repository = connectionProvider.getRepository(client);
    IFunctionTemplate template = repository.getFunctionTemplate(bapiName);
    if (template == null)
    {
      throw new HibersapException("The function module '" + bapiName + "' does not exist in SAP");
    }

    Function function = template.getFunction();
    jcoMapper.putFunctionMapValuesToFunction(function, functionMap);
    client.execute(function);
    jcoMapper.putFunctionValuesToFunctionMap(function, functionMap);
  }

  private Client getClientIfNecessary()
  {
    if (client == null)
    {
      client = connectionProvider.getClient();
    }
    return client;
  }

  public Transaction beginTransaction()
  {
    if (transaction != null)
    {
      return transaction;
    }
    transaction = new JCoTransaction(session);
    return transaction;
  }

  public void close()
  {
    connectionProvider.releaseClient(client);
  }

  public Transaction getTransaction()
  {
    return transaction;
  }
}