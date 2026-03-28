package com.voting.util;

import java.util.Collections;
import java.util.List;

public class PageResult<T> {

    private final List<T> items;
    private final int total;
    private final int page;
    private final int pageSize;

    public PageResult(List<T> items, int total, int page, int pageSize) {
        this.items = items != null ? items : Collections.emptyList();
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }

    public List<T> getItems() { return items; }
    public int getTotal() { return total; }
    public int getPage() { return page; }
    public int getPageSize() { return pageSize; }

    public int getTotalPages() {
        if (pageSize <= 0) return 0;
        return (int) Math.ceil((double) total / pageSize);
    }
}
