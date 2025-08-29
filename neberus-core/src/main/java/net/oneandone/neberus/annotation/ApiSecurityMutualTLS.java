package net.oneandone.neberus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Definition of {@link net.oneandone.neberus.model.SecurityScheme#MUTUAL_TLS}.
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiSecurityMutualTLS {

    String title() default "";

    String description() default "";

}
