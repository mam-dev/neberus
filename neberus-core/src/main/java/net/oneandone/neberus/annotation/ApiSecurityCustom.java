package net.oneandone.neberus.annotation;

import net.oneandone.neberus.model.SecurityIn;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Definition of {@link net.oneandone.neberus.model.SecurityScheme#CUSTOM}.
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiSecurityCustom {

    String title() default "";

    /**
     * Type of the custom security scheme. This can be referenced in e.g. {@link ApiAccessEntry#customType()}.
     */
    String type();

    SecurityIn in() default SecurityIn.UNDEFINED;

    String name() default "";

    String scheme() default "";

    String description() default "";

    ApiSecurityCustomAttribute[] attributes() default {};

}
