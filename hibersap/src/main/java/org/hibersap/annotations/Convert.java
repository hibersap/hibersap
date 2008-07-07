package org.hibersap.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import org.hibersap.conversion.Converter;

@Retention(RUNTIME)
public @interface Convert {

  Class<? extends Converter> converter();

}
