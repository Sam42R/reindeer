package io.github.sam42r.reindeer.layer.text;

import io.github.sam42r.reindeer.layer.MissionPatchLayer;
import io.github.sam42r.reindeer.layer.MissionPatchLayerProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.vaadin.pekkam.Canvas;
import org.vaadin.pekkam.CanvasRenderingContext2D;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractTextLayer implements MissionPatchLayer {

    @Builder.Default
    private int x = DEFAULT_CANVAS_WIDTH / 2;
    @Builder.Default
    private int y = DEFAULT_CANVAS_HEIGHT / 2;
    @Builder.Default
    private String text = "Sample Text";
    @Builder.Default
    private String style = "normal";
    @Builder.Default
    private String variant = "normal";
    @Builder.Default
    private String weight = "normal";
    @Builder.Default
    private Integer size = 16;
    @Builder.Default
    private String family = "Arial";
    @Builder.Default
    private String color = "Black";
    @Builder.Default
    private int rotation = 0;

    public static final List<String> FAMILIES = List.of(
            "Arial",
            "Verdana",
            "Tahoma",
            "Trebuchet MS",
            "Times New Roman",
            "Georgia",
            "Garamond",
            "Courier New",
            "Brush Script MT"
    );

    public static final List<String> STYLES = List.of(
            "normal",
            "italic"
    );

    public static final List<String> VARIANTS = List.of(
            "normal",
            "small-caps"
    );

    public static final List<String> WEIGHTS = List.of(
            "normal",
            "bold"
    );

    @Override
    public Category getCategory() {
        return Category.TEXT;
    }

    @Override
    public List<MissionPatchLayerProperty<?>> properties() {
        return List.of(
                new MissionPatchLayerProperty<>("x", Integer.class, this::getX, this::setX),
                new MissionPatchLayerProperty<>("y", Integer.class, this::getY, this::setY),
                new MissionPatchLayerProperty<>("text", String.class, this::getText, this::setText),
                new MissionPatchLayerProperty<>("style", String.class, this::getStyle, this::setStyle, STYLES),
                new MissionPatchLayerProperty<>("variant", String.class, this::getVariant, this::setVariant, VARIANTS),
                new MissionPatchLayerProperty<>("weight", String.class, this::getWeight, this::setWeight, WEIGHTS),
                new MissionPatchLayerProperty<>("size", Integer.class, this::getSize, this::setSize),
                new MissionPatchLayerProperty<>("family", String.class, this::getFamily, this::setFamily, FAMILIES),
                new MissionPatchLayerProperty<>("color", String.class, this::getColor, this::setColor),
                new MissionPatchLayerProperty<>("rotation", Integer.class, this::getRotation, this::setRotation)
        );
    }

    @Override
    public void draw(Canvas canvas) {
        var ctx = canvas.getContext();

        ctx.save();
        ctx.translate(x, y);
        ctx.rotate(rotation * Math.PI / 180);

        ctx.setFont("%s %s %s %spx %s".formatted(style, variant, weight, size, family));

        drawText(ctx);

        ctx.restore();
    }

    protected abstract void drawText(CanvasRenderingContext2D ctx);
}
