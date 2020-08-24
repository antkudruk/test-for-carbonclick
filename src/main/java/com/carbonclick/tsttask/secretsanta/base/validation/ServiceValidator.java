package com.carbonclick.tsttask.secretsanta.base.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceValidator implements ConstraintValidator<ServiceValidation, Object> {

    @Autowired
    private ApplicationContext applicationContext;

    private List<ValidatorService> validServices;

    @Override
    public void initialize(ServiceValidation unique) {
        validServices = Arrays.stream(unique.value())
                .map(cl -> applicationContext.getBean(cl))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        boolean valid = true;

        for(ValidatorService<Object> element : validServices) {
            if (!element.isValid(value)) {
                context.disableDefaultConstraintViolation();

                if(element.getFieldName().isEmpty()) {
                    context
                            .buildConstraintViolationWithTemplate(element.getMessage())
                            .addConstraintViolation();
                } else {
                    for (String fieldName : element.getFieldName()) {
                        context
                                .buildConstraintViolationWithTemplate(element.getMessage())
                                .addPropertyNode(fieldName)
                                .addConstraintViolation();
                    }
                }
                valid = false;
            }
        }

        return valid;
    }
}
