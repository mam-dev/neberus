package net.oneandone.neberus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Definition of {@link net.oneandone.neberus.model.SecurityScheme#ROLES}.
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiSecurityRoles {

    String title() default "";

    String description() default "";

    ApiRoleDescription[] roles() default {};

}
