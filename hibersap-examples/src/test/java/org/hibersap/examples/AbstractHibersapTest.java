package org.hibersap.examples;

import java.io.File;

public abstract class AbstractHibersapTest
{
  @org.junit.Before
  public void setUp()
  {
    String libPath = System.getProperty("java.library.path");
    File file = new File("../Sap-Base");
    libPath = libPath + ";" + file.getPath();
    System.setProperty("java.library.path", libPath);
  }

}
