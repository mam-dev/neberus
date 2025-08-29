package net.oneandone.neberus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define an option to gain access. This may contain multiple entries which are all expected to be fulfilled at the same time.
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiAccessOption {

    /**
     * Title of the access option.
     */
    String title();

    /**
     * More detailed description of the option.
     */
    String description() default "";

    /**
     * Mark the option as deprecated.
     */
    boolean deprecated() default false;

    ApiAccessEntry[] entries();

}
