package net.oneandone.neberus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Definition of {@link net.oneandone.neberus.model.SecurityScheme#BASIC}.
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiSecurityBasic {

    String title() default "";

    String description() default "";

}
