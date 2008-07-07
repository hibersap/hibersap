package org.hibersap.bapi;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ThrowExceptionOnError;

@Bapi(name = "BAPI_TRANSACTION_COMMIT")
@ThrowExceptionOnError(returnStructure = "EXPORT/RETURN")
public class BapiTransactionCommit
{
  @Import
  @Parameter(name = "WAIT")
  @SuppressWarnings("unused")
  private final String wait = "X";
}
