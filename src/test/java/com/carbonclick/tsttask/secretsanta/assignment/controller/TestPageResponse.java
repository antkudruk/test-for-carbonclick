package com.carbonclick.tsttask.secretsanta.assignment.controller;

import java.util.List;

public class TestPageResponse<D> {
    private List<D> content;
    private int pageSize;
    private int pageNumber;

    public List<D> getContent() {
        return content;
    }

    public TestPageResponse<D> setContent(List<D> content) {
        this.content = content;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public TestPageResponse<D> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public TestPageResponse<D> setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }
}
