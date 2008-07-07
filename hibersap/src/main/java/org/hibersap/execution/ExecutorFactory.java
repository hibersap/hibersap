package org.hibersap.execution;

import org.hibersap.HibersapException;
import org.hibersap.session.Session;

public class ExecutorFactory
{
  public static Executor create(Session session)
  {
    Class<? extends Executor> executorClass = session.getSessionFactory().getSettings()
        .getExecutorClass();
    Executor executor;
    try
    {
      executor = executorClass.newInstance();
      executor.configure(session);
    }
    catch (InstantiationException e)
    {
      throw new HibersapException("Executor class can not be instantiated", e);
    }
    catch (IllegalAccessException e)
    {
      throw new HibersapException("Executor class can not be instantiated", e);
    }
    return executor;
  };
}
