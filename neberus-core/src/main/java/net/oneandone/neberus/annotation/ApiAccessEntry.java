package net.oneandone.neberus.annotation;

import net.oneandone.neberus.model.SecurityScheme;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Single entry of an option to gain access.
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiAccessEntry {

    /**
     * Scheme of the entry. If the scheme is defined in {@link ApiSecuritySchemes} the data from there is used to
     * enrich the output.
     */
    SecurityScheme type();

    /**
     * Custom type of the entry. MUST be provided if {@link #type()} is {@link SecurityScheme#CUSTOM}.
     */
    String customType() default "";

    /**
     * Values specific to this entry, depends on the scheme. E.g. a list of roles or scopes.
     * <p>
     * For {@link #type()} {@link SecurityScheme#ROLES} this may be left undefined in order to automatically use the roles defined
     * by {@link ApiAllowedRoles} or @Secured.
     */
    String[] values() default {};

}
