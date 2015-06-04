package me.zeph.logger;

import com.google.common.base.Joiner;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class ApplicationLogInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationLogInterceptor.class);
	private static final String SEPARATOR = ", ";

	@Before(value = "@annotation(me.zeph.logger.Log)")
	public void logMethodInvoke(JoinPoint joinPoint) {
		Logger logger = getLogger(joinPoint);
		Method method = getMethod(joinPoint);
		String arguments = getArguments(joinPoint);
	}

	@AfterReturning(pointcut = "@annotation(me.zeph.logger.Log)", returning = "returning")
	public void logMethodReturn(JoinPoint joinPoint, Object returning) {
		Logger logger = getLogger(joinPoint);
		Method method = getMethod(joinPoint);
	}

	@AfterThrowing(pointcut = "@annotation(me.zeph.logger.Log)", throwing = "exception")
	public void logException(JoinPoint joinPoint, Throwable exception) throws Throwable {
		Logger logger = getLogger(joinPoint);
		throw exception;
	}

	private String getArguments(JoinPoint joinPoint) {
		return Joiner.on(SEPARATOR).useForNull("null").join(joinPoint.getArgs());
	}

	private Method getMethod(JoinPoint joinPoint) {
		Method method = null;
		try {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			method = joinPoint.getSourceLocation()
					.getWithinType()
					.getMethod(signature.getMethod().getName(), signature.getMethod().getParameterTypes());
		} catch (NoSuchMethodException e) {
			LOGGER.error("");
		}
		return method;
	}

	private Logger getLogger(JoinPoint joinPoint) {
		return LoggerFactory.getLogger(joinPoint.getTarget().getClass());
	}
}
