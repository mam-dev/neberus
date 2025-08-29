package net.oneandone.neberus.annotation;

import net.oneandone.neberus.model.SecurityIn;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Definition of {@link net.oneandone.neberus.model.SecurityScheme#API_KEY}.
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiSecurityApiKey {

    String title() default "";

    String description() default "";

    SecurityIn in();

    String name();

}
