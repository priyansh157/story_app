package com.story.storyapp;

import java.util.List;

public class ParentItem {

    private String title;
    private List<ChildItem> childItems;

    public ParentItem(String title, List<ChildItem> childItems) {
        this.title = title;
        this.childItems = childItems;
    }

    public String getTitle() {
        return title;
    }

    public List<ChildItem> getChildItems() {
        return childItems;
    }
}
