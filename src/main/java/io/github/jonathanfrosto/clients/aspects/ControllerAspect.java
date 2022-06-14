package io.github.jonathanfrosto.clients.aspects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.jonathanfrosto.clients.aspects.AspectUtil.getAnnotation;

@Aspect
@Component
@Slf4j
public class ControllerAspect {

    public static final String RECEIVED_REQUEST = "========== RECEIVED REQUEST ==========";
    public static final String BARS = "======================================";
    private final ObjectMapper mapper;

    public ControllerAspect(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object genericMapping(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestMapping annotation = getAnnotation(joinPoint, RequestMapping.class);
        List<String> methods = Arrays.stream(annotation.method())
                .map(Enum::name)
                .collect(Collectors.toList());
        log.debug(RECEIVED_REQUEST);
        logRequest(String.join(", ", methods), annotation.value(), joinPoint);
        Object response = joinPoint.proceed();
        logResponse(response);
        log.debug(BARS);
        return response;
    }

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object getMapping(ProceedingJoinPoint joinPoint) throws Throwable {
        GetMapping annotation = getAnnotation(joinPoint, GetMapping.class);
        log.debug(RECEIVED_REQUEST);
        logRequest("GET", annotation.value(), joinPoint);
        Object response = joinPoint.proceed();
        logResponse(response);
        log.debug(BARS);
        return response;
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object postMapping(ProceedingJoinPoint joinPoint) throws Throwable {
        PostMapping annotation = getAnnotation(joinPoint, PostMapping.class);
        log.debug(RECEIVED_REQUEST);
        logRequest("POST", annotation.value(), joinPoint);
        Object response = joinPoint.proceed();
        logResponse(response);
        log.debug(BARS);
        return response;
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public Object putMapping(ProceedingJoinPoint joinPoint) throws Throwable {
        PutMapping annotation = getAnnotation(joinPoint, PutMapping.class);
        log.debug(RECEIVED_REQUEST);
        logRequest("PUT", annotation.value(), joinPoint);
        Object response = joinPoint.proceed();
        logResponse(response);
        log.debug(BARS);
        return response;
    }

    @Around("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object deleteMapping(ProceedingJoinPoint joinPoint) throws Throwable {
        DeleteMapping annotation = getAnnotation(joinPoint, DeleteMapping.class);
        log.debug(RECEIVED_REQUEST);
        logRequest("DELETE", annotation.value(), joinPoint);
        Object response = joinPoint.proceed();
        logResponse(response);
        log.debug(BARS);
        return response;
    }

    @Around("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public Object errorMapping(ProceedingJoinPoint joinPoint) throws Throwable {
        ResponseStatus annotation = getAnnotation(joinPoint, ResponseStatus.class);
        int httpStatus = annotation.value().value();

        Object response = joinPoint.proceed();
        log.debug("Error - Status code: {}, response: {}.", httpStatus, mapper.writeValueAsString(response));
        log.debug(BARS);
        return response;
    }

    private void logRequest(String httpMethod, String[] uri, JoinPoint joinPoint) {
        Map<String, Object> parameters = getParameters(joinPoint);

        try {

            log.debug("Method: {}, Path: {}, Parameters: {}", httpMethod, uri, mapper.writeValueAsString(parameters));

        } catch (JsonProcessingException exception) {
            log.warn("Can't deserialize parameters");
        }
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        Map<String, Object> map = new HashMap<>();

        String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }

        return map;
    }

    private void logResponse(Object response) {
        try {
            log.debug("Response: {}", mapper.writeValueAsString(response));
        } catch (JsonProcessingException exception) {
            log.warn("Can't deserialize parameters");
        }

    }
}
