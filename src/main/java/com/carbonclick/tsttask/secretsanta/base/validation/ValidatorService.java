package com.carbonclick.tsttask.secretsanta.base.validation;

import java.util.List;

public interface ValidatorService<R> {
    String getMessage();
    boolean isValid(R object);
    List<String> getFieldName();
}
