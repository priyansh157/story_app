package com.story.storyapp;

public class ChildItem {
    private String image;
    private String StoryId;

    public ChildItem(String image, String storyId) {
        this.image = image;
        StoryId = storyId;
    }

    public String getImage() {
        return image;
    }

    public String getStoryId() {
        return StoryId;
    }
}
