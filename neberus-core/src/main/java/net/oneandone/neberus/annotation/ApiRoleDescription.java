package net.oneandone.neberus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provide additional description to roles/authorities used throughout the class. If provided this will be shown in a tooltip when
 * the role is used.
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiRoleDescription {

    String name();

    String description();

}
