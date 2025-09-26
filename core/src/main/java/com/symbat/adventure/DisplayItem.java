package com.symbat.adventure;

import com.badlogic.gdx.graphics.Texture;

public class DisplayItem {
    private Texture image;
    private String description;

    public DisplayItem(Texture image, String description) {
        this.image = image;
        this.description = description;
    }

    public Texture getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
