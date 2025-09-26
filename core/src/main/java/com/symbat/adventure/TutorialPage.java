package com.symbat.adventure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class TutorialPage {
    private final List<DisplayItem> items;

    public TutorialPage(DisplayItem... items) {
        if (items == null || items.length == 0 || items.length > 3) {
            throw new IllegalArgumentException("TutorialPage must contain 1-3 items");
        }
        this.items = Collections.unmodifiableList(Arrays.asList(items));
    }

    public List<DisplayItem> getItems() {
        return items;
    }
}
