package net.oneandone.neberus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Definition of {@link net.oneandone.neberus.model.SecurityScheme#OAUTH2}.
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiSecurityOAuth2 {

    String title() default "";

    String description() default "";

}
