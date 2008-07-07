package org.hibersap.session;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.execution.Executor;
import org.hibersap.execution.ExecutorFactory;
import org.hibersap.execution.PojoMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.BapiMapping.ErrorHandling;

public class SessionImpl implements Session
{
  private static final Log LOG = LogFactory.getLog(SessionImpl.class);

  private boolean closed = false;

  private final SessionFactory sessionFactory;

  private final Executor executor;

  private PojoMapper pojoMapper;

  public SessionImpl(SessionFactory sessionFactory)
  {
    this.sessionFactory = sessionFactory;
    pojoMapper = new PojoMapper(sessionFactory.getConverterCache());
    executor = ExecutorFactory.create(this);
  }

  public Transaction beginTransaction()
  {
    errorIfClosed();
    return executor.beginTransaction();
  }

  public void close()
  {
    errorIfClosed();
    executor.close();
    setClosed();
  }

  public void execute(Object bapiObject)
  {
    errorIfClosed();
    Class<?> bapiClass = bapiObject.getClass();
    Map<Class<?>, BapiMapping> bapiMappings = sessionFactory.getBapiMappings();
    if (bapiMappings.containsKey(bapiClass))
    {
      execute(bapiObject, bapiMappings.get(bapiClass));
    }
    else
    {
      throw new HibersapException(bapiClass.getName() + " is not configured as a Bapi class");
    }
  }

  public void execute(Object bapiObject, BapiMapping bapiMapping)
  {
    errorIfClosed();

    String bapiName = bapiMapping.getBapiName();
    LOG.info("Executing " + bapiName);

    Map<String, Object> functionMap = pojoMapper.mapPojoToFunctionMap(bapiObject, bapiMapping);

    executor.execute(bapiName, functionMap);

    // checkForErrors(bapiMapping, functionMap);

    pojoMapper.mapFunctionMapToPojo(bapiObject, functionMap, bapiMapping);
  }

  private void checkForErrors(BapiMapping bapiMapping, Map<String, Object> functionMap)
  {
    ErrorHandling errorHandling = bapiMapping.getErrorHandling();
    if (errorHandling.isThrowExceptionOnError())
    {
      String pathToReturnStructure = errorHandling.getPathToReturnStructure();
      Map<String, Object> returnStructure = getReturnStructure(functionMap, pathToReturnStructure);

      // TODO check type!
      String type = (String) returnStructure.get("TYPE");
      String[] msgTypes = errorHandling.getErrorMessageTypes();

      for (int i = 0; i < msgTypes.length; i++)
      {

      }
    }

  }

  private Map<String, Object> getReturnStructure(Map<String, Object> functionMap,
      String pathToReturnStructure)
  {
    // TODO
    return null;
  }

  public Transaction getTransaction()
  {
    errorIfClosed();
    return executor.getTransaction();
  }

  public boolean isClosed()
  {
    return closed;
  }

  private void errorIfClosed()
  {
    if (isClosed())
    {
      throw new HibersapException("Session is already closed");
    }
  }

  private void setClosed()
  {
    closed = true;
  }

  public SessionFactory getSessionFactory()
  {
    return sessionFactory;
  }
}
