package com.mcw.football.domain.util.handler;

import com.mcw.football.domain.util.annotations.DiplomaExceptionCodes;
import com.mcw.football.exceptions.IllegalEnumException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Description;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component("ResponseCodeHandler")
@Description("Scans class path for annotated class and build mapping")
public class ResponseCodeHandler {
    private static final Logger LOGGER = Logger.getLogger(ResponseCodeHandler.class);
    private Map<Class, CodeContainer> exceptions = new HashMap<>();

    @PostConstruct
    private void init() {
        try {
            ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
            provider.addIncludeFilter(new AnnotationTypeFilter(DiplomaExceptionCodes.class));
            Set<BeanDefinition> bds = provider.findCandidateComponents("com.smiddle");
            for (BeanDefinition bd : bds) getMapping(Class.forName(bd.getBeanClassName()));
        } catch (Exception e) {
            LOGGER.error("Initialization failed", e);
        }
    }

    public ResponseEntity resolve(Throwable throwable, HttpStatus defaultHttpStatus) {
        if (throwable == null) {
            throw new IllegalArgumentException("ResponseCodeHandler.resolve throwable is NULL!");
        }
        if (exceptions.containsKey(throwable.getClass())) {
            CodeContainer container = exceptions.get(throwable.getClass());
            return buildResponseEntity(throwable, container.getCode(), resolveHttpCode(container, defaultHttpStatus));
        }
        return buildResponseEntity(throwable, "UNMAPPED_EXCEPTION", defaultHttpStatus);
    }

    private HttpStatus resolveHttpCode(CodeContainer container, HttpStatus defaultHttpStatus) {
        return (container.getStatus() == HttpStatus.CONTINUE) ? defaultHttpStatus : container.getStatus();
    }

    private ResponseEntity buildResponseEntity(Throwable throwable, String code, HttpStatus status) {
        return new ResponseEntity<Map>(Collections.singletonMap(code, throwable.getMessage()), status);
    }

    public Map<String, String> resolve(Errors errors) {
        if (errors == null) throw new IllegalArgumentException("ResponseCodeHandler.resolve errors is NULL!");
        return errors.getAllErrors().stream().collect(Collectors.toMap(ObjectError::getCode, ObjectError::getDefaultMessage));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        exceptions.keySet().forEach(clazz -> sb.append(clazz.getSimpleName()).append(" "));
        return sb.toString();
    }

    private void getMapping(Class<?> clazz) throws IllegalEnumException {
        try {
            if (!clazz.isEnum()) return;
            if (clazz.isAnnotationPresent(DiplomaExceptionCodes.class)) {
                Method clazzMethod = clazz.getMethod("getClazz");
                Method status;
                try {
                    status = clazz.getMethod("getStatus");
                } catch (NoSuchMethodException e) {
                    status = null;
                }
                Object[] enums = clazz.getEnumConstants();
                if (enums != null) for (Object o : enums)
                    exceptions.put((Class) clazzMethod.invoke(o),
                            new CodeContainer(o.toString(), status != null ? (HttpStatus) status.invoke(o) : HttpStatus.CONTINUE));
            } else throw new IllegalEnumException(clazz.getName());
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalEnumException(clazz.getName());
        }
    }

    public static class CodeContainer {
        private final String code;
        private final HttpStatus status;

        public CodeContainer(String code, HttpStatus status) {
            this.code = code;
            this.status = status;
        }

        public String getCode() {
            return code;
        }

        public HttpStatus getStatus() {
            return status;
        }
    }
}