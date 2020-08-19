package com.carbonclick.tsttask.secretsanta.base.page;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.List;

@Getter
@ApiModel("Default page response")
public class Page<D> {
    private final List<D> content;
    private final int pageSize;
    private final int pageNumber;
    private final int total;

    public Page(List<D> content, int pageSize, int total) {
        this.content = content;
        this.pageSize = pageSize;
        this.pageNumber = total / pageSize + 1;
        this.total = total;
    }
}
