package org.hibersap.session;

public interface Transaction
{
  void commit();

  void rollback();
}
