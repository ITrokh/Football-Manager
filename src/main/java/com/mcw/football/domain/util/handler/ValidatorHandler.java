package com.mcw.football.domain.util.handler;

import com.mcw.football.domain.util.annotations.DiplomaValidating;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Description;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component("ValidatorHandler")
@Description("Scans class path for annotated class and build mapping")

public class ValidatorHandler implements Validator {
    final static Logger LOGGER = Logger.getLogger(ValidatorHandler.class);
    @Autowired
    private ApplicationContext context;
    private final List<Validator> validators = new ArrayList<>();

    @PostConstruct
    private void setUp() throws Exception {
        try {
            ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
            provider.addIncludeFilter(new AnnotationTypeFilter(DiplomaValidating.class));
            Set<BeanDefinition> bds = provider.findCandidateComponents("com.smiddle");
            for (BeanDefinition bd : bds) {
                Class<?> validatorClass = Class.forName(bd.getBeanClassName());
                try {
                    Object validator = context.getBean(validatorClass);
                    validators.add((Validator) validator);
                } catch (NoSuchBeanDefinitionException e) {
                    validators.add((Validator) validatorClass.newInstance());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Initialization failed "+e);
            throw e;
        }
    }

    @Override
    public boolean supports(final Class clazz) {
        // We do not want to get IllegalStateException if no validator found for a parameter without @Validated annotation
        return true;
    }

    // Is invoked only if there is @Validated annotation on the target
    @Override
    public void validate(final Object obj, final Errors errors) {
        boolean validated = false;
        for (Validator validator : validators) {
            if (validator.supports(obj.getClass())) {
                validator.validate(obj, errors);
                validated = true;
            }
        }
        // Throws exception if target is annotated with @Validated and no validator found
        if (!validated) {
            throw new IllegalStateException("No validator found for target: " + obj.getClass().getName());
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        validators.forEach(validator -> sb.append(validator.getClass().getSimpleName()).append(" "));
        return sb.toString();
    }
}
