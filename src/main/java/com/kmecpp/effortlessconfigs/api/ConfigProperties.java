package com.kmecpp.effortlessconfigs.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigProperties {

	String path();

	String header() default "";

	boolean allowKeyRemoval() default false;

}
