package org.hibersap.session;

import org.hibersap.mapping.model.BapiMapping;

public interface Session
{
  /**
   * executes a funtion module in SAP.
   * 
   * @param bapi
   *            The BAPI class
   */
  void execute(Object bapi);

  /**
   * starts a transaction. Semantic depends on the type of
   * 
   * @return The Hibersap transaction
   */
  Transaction beginTransaction();

  /**
   * returns the transaction.
   * 
   * @return The hibersap transaction or null if no transaction started.
   */
  Transaction getTransaction();

  /**
   * releases all resources.
   */
  void close();

  /**
   * is session already closed?
   * 
   * @return
   */
  boolean isClosed();

  // TODO move to SessionImplementor interface cf Hibernate
  SessionFactory getSessionFactory();

  void execute(Object bapi, BapiMapping bapiMapping);
}
