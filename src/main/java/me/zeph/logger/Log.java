package me.zeph.logger;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static me.zeph.logger.LogLevel.DEBUG;

@Retention(value = RUNTIME)
@Target(value = {METHOD})
public @interface Log {
	public String format() default "";
	public LogLevel level() default DEBUG;
}
