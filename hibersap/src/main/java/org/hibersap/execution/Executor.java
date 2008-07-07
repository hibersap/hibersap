package org.hibersap.execution;

import java.util.Map;

import org.hibersap.session.Session;
import org.hibersap.session.Transaction;

/**
 * Implementations of this interface define the functionality how to communicate
 * with SAP, using for example the SAP Java Connector or a JCA resource adapter.
 * The implementation to be used by a session factory is specified by the
 * property <code>hibersap.executor_class</code>. The default implementation
 * is org.hibersap.execution.jco.JCoExecutor. Implementations must provide a
 * default constructor.
 * 
 * @author cerker
 */
public interface Executor
{
  Transaction beginTransaction();

  void close();

  Transaction getTransaction();

  /**
   * configure the Executor. The method is called by the Session after the
   * Executor's creation.
   * 
   * @param sessionFactory
   *            The SessionFactory
   */
  void configure(Session session);

  void execute(String bapiName, Map<String, Object> functionMap);
}
