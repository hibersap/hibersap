package org.hibersap.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface Parameter {

  String name();

  ParameterType type() default ParameterType.FIELD;

}
