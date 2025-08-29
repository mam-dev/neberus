package net.oneandone.neberus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Definition of {@link net.oneandone.neberus.model.SecurityScheme#OPEN_ID}.
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiSecurityOpenId {

    String title() default "";

    String description() default "";

    String openIdConnectUrl() default "";

}
