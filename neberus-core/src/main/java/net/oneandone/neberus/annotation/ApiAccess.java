package net.oneandone.neberus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define access control options. If placed on the resource class this will apply to all contained methods.
 * If placed on a method, it will apply only to this method and override any definition on the class.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface ApiAccess {

    /**
     * List of all possible options to gain access. The options will be shown in the same order as defined here.
     */
    ApiAccessOption[] value() default {};

}
