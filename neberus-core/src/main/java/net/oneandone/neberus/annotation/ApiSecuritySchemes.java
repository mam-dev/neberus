package net.oneandone.neberus.annotation;

import net.oneandone.neberus.model.SecurityIn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines security schemes and possible access options for the application.
 * Any used {@link net.oneandone.neberus.model.SecurityScheme} should be added here.
 * This will be shown on the main page of the generated documentation and enriches the output of {@link ApiAccessEntry}s.
 * <p>
 * This may only be defined once for the whole application.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
public @interface ApiSecuritySchemes {

    String description() default "";

    /**
     * Definition of {@link net.oneandone.neberus.model.SecurityScheme#BASIC}.
     */
    ApiSecurityBasic basic() default @ApiSecurityBasic;

    /**
     * Definition of {@link net.oneandone.neberus.model.SecurityScheme#BEARER}.
     */
    ApiSecurityBearer bearer() default @ApiSecurityBearer;

    /**
     * Definition of {@link net.oneandone.neberus.model.SecurityScheme#OAUTH2}.
     */
    ApiSecurityOAuth2 oAuth2() default @ApiSecurityOAuth2;

    /**
     * Definition of {@link net.oneandone.neberus.model.SecurityScheme#OPEN_ID}.
     */
    ApiSecurityOpenId openId() default @ApiSecurityOpenId;

    /**
     * Definition of {@link net.oneandone.neberus.model.SecurityScheme#MUTUAL_TLS}.
     */
    ApiSecurityMutualTLS mutualTLS() default @ApiSecurityMutualTLS;

    /**
     * Definition of {@link net.oneandone.neberus.model.SecurityScheme#API_KEY}.
     */
    ApiSecurityApiKey apiKey() default @ApiSecurityApiKey(in = SecurityIn.HEADER, name = "");

    /**
     * Definition of {@link net.oneandone.neberus.model.SecurityScheme#CUSTOM}.
     */
    ApiSecurityCustom[] custom() default {};

    /**
     * Definition of {@link net.oneandone.neberus.model.SecurityScheme#ROLES}.
     */
    ApiSecurityRoles roles() default @ApiSecurityRoles;

}
