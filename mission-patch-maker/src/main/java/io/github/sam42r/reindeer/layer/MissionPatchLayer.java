package io.github.sam42r.reindeer.layer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;
import io.github.sam42r.reindeer.layer.image.IconLayer;
import io.github.sam42r.reindeer.layer.image.ImageLayer;
import io.github.sam42r.reindeer.layer.simple.CircleLayer;
import io.github.sam42r.reindeer.layer.simple.PolygonLayer;
import io.github.sam42r.reindeer.layer.simple.RectangleLayer;
import io.github.sam42r.reindeer.layer.simple.StarLayer;
import io.github.sam42r.reindeer.layer.template.NasaLayerBundle;
import io.github.sam42r.reindeer.layer.text.CircleTextLayer;
import io.github.sam42r.reindeer.layer.text.FillTextLayer;
import io.github.sam42r.reindeer.layer.text.StrokeTextLayer;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import org.vaadin.pekkam.Canvas;
import org.vaadin.pekkam.event.ImageLoadEvent;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @Type(value = CircleLayer.class),
        @Type(value = PolygonLayer.class),
        @Type(value = RectangleLayer.class),
        @Type(value = StarLayer.class),
        @Type(value = IconLayer.class),
        @Type(value = ImageLayer.class),
        @Type(value = CircleTextLayer.class),
        @Type(value = FillTextLayer.class),
        @Type(value = StrokeTextLayer.class),
        @Type(value = NasaLayerBundle.class)
})
public interface MissionPatchLayer {

    int MIN_CANVAS_WIDTH = 100;
    int DEFAULT_CANVAS_WIDTH = 400;
    int MAX_CANVAS_WIDTH = 800;

    int MIN_CANVAS_HEIGHT = 100;
    int DEFAULT_CANVAS_HEIGHT = 400;
    int MAX_CANVAS_HEIGHT = 800;

    @JsonIgnore
    default String getName() {
        return getClass().getSimpleName();
    }

    @JsonIgnore
    default Category getCategory() {
        return Category.NONE;
    }

    List<MissionPatchLayerProperty<?>> properties();

    default Registration preloadImage(@NonNull Canvas canvas, @NonNull ComponentEventListener<ImageLoadEvent> listener) {
        return null;
    }

    void draw(@Nonnull Canvas canvas);

    enum Category {
        SIMPLE, IMAGE, TEXT, TEMPLATE, NONE;
    }
}
