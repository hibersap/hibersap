package org.hibersap.execution.jco;

import org.hibersap.bapi.BapiTransactionCommit;
import org.hibersap.bapi.BapiTransactionRollback;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.Session;
import org.hibersap.session.Transaction;

public class JCoTransaction implements Transaction
{
  private final Session session;

  private BapiMapping mappingCommit;

  private BapiMapping mappingRollback;

  public JCoTransaction(Session session)
  {
    this.session = session;
    initTransactionBapis();
  }

  public void commit()
  {
    executeBapi(new BapiTransactionCommit(), mappingCommit);
  }

  public void rollback()
  {
    executeBapi(new BapiTransactionRollback(), mappingRollback);
  }

  private void executeBapi(Object bapi, BapiMapping mapping)
  {
    session.execute(bapi, mapping);
  }

  private void initTransactionBapis()
  {
    AnnotationBapiMapper mapper = new AnnotationBapiMapper();
    this.mappingCommit = mapper.mapBapi(BapiTransactionCommit.class);
    this.mappingRollback = mapper.mapBapi(BapiTransactionRollback.class);
  }
}
