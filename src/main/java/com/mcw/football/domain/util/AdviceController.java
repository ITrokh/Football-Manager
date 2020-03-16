package com.mcw.football.domain.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mcw.football.domain.util.handler.ResponseCodeHandler;
import com.mcw.football.domain.util.handler.ValidatorHandler;
import com.mcw.football.exceptions.ValidationException;
import org.apache.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.mcw.football.domain.util.JacksonUtil.objectToJson;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class AdviceController {
    private static final Logger LOGGER = Logger.getLogger(AdviceController.class);
    private static final HttpHeaders headers = new HttpHeaders();
    private final RequestMappingHandlerMapping mapper;
    private final ValidatorHandler validator;
    private final ResponseCodeHandler codeHandler;
    private static Map<String, HandlerMethod> staticUrl;
    private static List<W> patternUrl;

    public AdviceController(RequestMappingHandlerMapping mapper,
                            ResponseCodeHandler codeHandler,
                            ValidatorHandler validator) {
        this.mapper = mapper;
        this.codeHandler=codeHandler;
        this.validator = validator;
    }

    @PostConstruct
    private void setUp() {
        fill();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private void fill() {
        Map<String, HandlerMethod> staticUrl = new HashMap<>();
        List<W> patternUrl = new ArrayList<>();
        mapper.getHandlerMethods()
                .forEach((k, v) -> {
                    for (String pattern : k.getPatternsCondition().getPatterns()) {
                        for (RequestMethod method : k.getMethodsCondition().getMethods()) {
                            if (pattern.contains("{"))
                                patternUrl.add(new W(new AntPathRequestMatcher(pattern.replaceAll("\\{[^}.]*}", "*"), method.name()), v));
                            else
                                staticUrl.put(String.format("%s:%s", pattern, method.name()), v);
                        }
                    }
                });
        AdviceController.staticUrl = Collections.unmodifiableMap(staticUrl);
        AdviceController.patternUrl = Collections.unmodifiableList(patternUrl);
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }


    @ExceptionHandler(value = {
            ValidationException.class, HttpMessageNotReadableException.class, MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class, MissingServletRequestPartException.class, BindException.class,
            TypeMismatchException.class})
    private Object badRequest(Exception e) throws JsonProcessingException {
        LOGGER.debug(e);
        if (e instanceof ValidationException)
            return new ResponseEntity<>(objectToJson(codeHandler.resolve(((ValidationException) e).getBindingResult())), headers, BAD_REQUEST);
        else return codeHandler.resolve(e, BAD_REQUEST);
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class, NoSuchMethodException.class})
    private Object notFound(Exception e) {
        return codeHandler.resolve(e, NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private Object methodNotAllowed(Exception e) {
        return codeHandler.resolve(e, METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    private Object notAcceptable(Exception e) {
        return codeHandler.resolve(e, NOT_ACCEPTABLE);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    private Object unsupportedMediaType(Exception e) {
        return codeHandler.resolve(e, UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(Exception.class)
    private Object internalServerError(Exception e) {
        LOGGER.error(e);
        return codeHandler.resolve(e, INTERNAL_SERVER_ERROR);
    }

    private HandlerMethod defineMethod(HttpServletRequest httpReq) {
        String path = httpReq.getServletPath().split("\\?")[0];
        HandlerMethod method = staticUrl.get(path + ":" + httpReq.getMethod());
        if (method != null) return method;
        Optional<W> matched = patternUrl
                .stream()
                .filter(w -> w.getMatcher().matches(httpReq))
                .findAny();
        if (matched.isPresent())
            return matched.get().getMethod();
        LOGGER.info(String.format("defineHandlerMethod: no handler found for request=%s method=%s", path, httpReq.getMethod()));
        try {
            return new HandlerMethod(this, this.getClass().getMethod("defineHandlerMethod", HttpServletRequest.class));
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private class W {
        private final AntPathRequestMatcher matcher;
        private final HandlerMethod method;

        private W(AntPathRequestMatcher matcher, HandlerMethod method) {
            this.matcher = matcher;
            this.method = method;
        }

        public AntPathRequestMatcher getMatcher() {
            return matcher;
        }

        public HandlerMethod getMethod() {
            return method;
        }
    }

}
