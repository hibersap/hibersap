package org.hibersap.annotations;

public @interface ThrowExceptionOnError {

  String returnStructure() default "EXPORT/RETURN";

  String[] errorMessageTypes() default
  { "E", "A" };
}
