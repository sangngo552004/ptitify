package com.sangngo552004.musicapp.dto.response;

import java.util.List;

public class CursorResult<T> {

    private List<T> items;
    private Long nextCursor;

    public CursorResult() {
    }

    public CursorResult(List<T> items, Long nextCursor) {
        this.items = items;
        this.nextCursor = nextCursor;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Long getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(Long nextCursor) {
        this.nextCursor = nextCursor;
    }
}
