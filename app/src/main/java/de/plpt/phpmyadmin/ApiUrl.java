package de.plpt.phpmyadmin;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Pascal on 09.08.2016.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface ApiUrl {
    String URL();
    String BaseUrl() default "";
}

