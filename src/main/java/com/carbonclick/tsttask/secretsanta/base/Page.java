package com.carbonclick.tsttask.secretsanta.base;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.List;

@Getter
@ApiModel("Default page response")
public class Page<D> {
    private final List<D> content;
    private final int pageSize;
    private final int pageNumber;

    public Page(List<D> content, int pageSize, int elementsNumber) {
        this.content = content;
        this.pageSize = pageSize;
        this.pageNumber = elementsNumber / pageSize + 1;
    }
}
