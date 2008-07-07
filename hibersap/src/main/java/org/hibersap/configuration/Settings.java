package org.hibersap.configuration;

import org.hibersap.execution.Executor;

public final class Settings
{
  Class<? extends Executor> executorClass;

  public Class<? extends Executor> getExecutorClass()
  {
    return executorClass;
  }

  public void setExecutorClass(Class<? extends Executor> executorClass)
  {
    this.executorClass = executorClass;
  }
}
