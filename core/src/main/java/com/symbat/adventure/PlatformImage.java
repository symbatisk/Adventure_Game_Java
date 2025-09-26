package com.symbat.adventure;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum PlatformImage {
    LEVEL1("data/platform_1/start.png", "data/platform_1/middle.png", "data/platform_1/end.png"),
    LEVEL2("data/platform_2/start.png", "data/platform_2/middle.png", "data/platform_2/end.png"),
    LEVEL3("data/platform_3/start.png", "data/platform_3/middle.png", "data/platform_3/end.png");

    private final TextureRegion startRegion;
    private final TextureRegion middleRegion;
    private final TextureRegion endRegion;

    PlatformImage(String startPath, String middlePath, String endPath) {
        this.startRegion  = new TextureRegion(new Texture(startPath));
        this.middleRegion = new TextureRegion(new Texture(middlePath));
        this.endRegion    = new TextureRegion(new Texture(endPath));
    }

    public TextureRegion getStart()  { return startRegion; }
    public TextureRegion getMiddle() { return middleRegion; }
    public TextureRegion getEnd()    { return endRegion; }
}
