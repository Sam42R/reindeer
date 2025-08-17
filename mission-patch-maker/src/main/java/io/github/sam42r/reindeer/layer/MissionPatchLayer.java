package io.github.sam42r.reindeer.layer;

import jakarta.annotation.Nonnull;
import org.vaadin.pekkam.Canvas;

import java.util.List;

public interface MissionPatchLayer {

    int DEFAULT_CANVAS_WIDTH = 400;
    int DEFAULT_CANVAS_HEIGHT = 400;

    default String getName() {
        return getClass().getSimpleName();
    }

    default Category getCategory() {
        return Category.NONE;
    }

    List<MissionPatchLayerProperty<?>> properties();

    void draw(@Nonnull Canvas canvas);

    enum Category {
        SIMPLE, IMAGE, TEXT, TEMPLATE, NONE;
    }
}
