package org.hibersap;

public class MappingException extends HibersapException
{
  public MappingException(String msg, Throwable root)
  {
    super(msg, root);
  }

  public MappingException(Throwable root)
  {
    super(root);
  }

  public MappingException(String s)
  {
    super(s);
  }
}
