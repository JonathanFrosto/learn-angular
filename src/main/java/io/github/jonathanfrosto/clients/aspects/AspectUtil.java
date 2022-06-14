package io.github.jonathanfrosto.clients.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;

public class AspectUtil {

    private AspectUtil() {}

    public static <A extends Annotation> A getAnnotation(JoinPoint joinPoint, Class<A> tClass) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(tClass);
    }
}
