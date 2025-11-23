package io.github.sam42r.reindeer.layer.text;

import io.github.sam42r.reindeer.canvas.Canvas;
import io.github.sam42r.reindeer.layer.AbstractMissionPatchLayer;
import io.github.sam42r.reindeer.layer.MissionPatchLayerProperty;
import lombok.*;

import java.util.List;

import static io.github.sam42r.reindeer.layer.text.AbstractTextLayer.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CircleTextLayer extends AbstractMissionPatchLayer {

    @Builder.Default
    private int x = DEFAULT_CANVAS_WIDTH / 2;
    @Builder.Default
    private int y = DEFAULT_CANVAS_HEIGHT / 2;
    @Builder.Default
    private int radius = (int) ((DEFAULT_CANVAS_WIDTH * 0.7) / 2);
    @Builder.Default
    private int start = -90;
    @Builder.Default
    private int segment = 180;
    @Builder.Default
    private boolean clockwise = true;
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

    @Override
    public String getName() {
        return "CircleText";
    }

    @Override
    public Category getCategory() {
        return Category.TEXT;
    }

    @Override
    public List<MissionPatchLayerProperty<?>> properties() {
        return List.of(
                new MissionPatchLayerProperty<>("x", Integer.class, this::getX, this::setX),
                new MissionPatchLayerProperty<>("y", Integer.class, this::getY, this::setY),
                new MissionPatchLayerProperty<>("radius", Integer.class, this::getRadius, this::setRadius),
                new MissionPatchLayerProperty<>("start", Integer.class, this::getStart, this::setStart, -360, 360),
                new MissionPatchLayerProperty<>("segment", Integer.class, this::getSegment, this::setSegment, 0, 360),
                new MissionPatchLayerProperty<>("clockwise", Boolean.class, this::isClockwise, this::setClockwise),
                new MissionPatchLayerProperty<>("text", String.class, this::getText, this::setText),
                new MissionPatchLayerProperty<>("style", String.class, this::getStyle, this::setStyle, STYLES),
                new MissionPatchLayerProperty<>("variant", String.class, this::getVariant, this::setVariant, VARIANTS),
                new MissionPatchLayerProperty<>("weight", String.class, this::getWeight, this::setWeight, WEIGHTS),
                new MissionPatchLayerProperty<>("size", Integer.class, this::getSize, this::setSize),
                new MissionPatchLayerProperty<>("family", String.class, this::getFamily, this::setFamily, FAMILIES),
                new MissionPatchLayerProperty<>("color", String.class, this::getColor, this::setColor)
        );
    }

    @Override
    public void draw(Canvas canvas) {
        var ctx = canvas.getContext();

        var numRadsPerLetter = (segment * Math.PI / 180) / (text.length() - 1);
        ctx.save();
        ctx.translate(x, y);
        ctx.rotate(start * Math.PI / 180); // TODO
        if (!clockwise) {
            ctx.rotate(Math.PI);
        }

        ctx.setFont("%s %s %s %spx %s".formatted(style, variant, weight, size, family));
        ctx.setFillStyle(color);

        var chars = clockwise ? text : new StringBuilder(text).reverse().toString();
        for (var i = 0; i < chars.length(); i++) {
            ctx.save();
            ctx.rotate(i * numRadsPerLetter);

            ctx.fillText(String.valueOf(chars.charAt(i)), 0, (clockwise ? -1 : 1) * (double) radius);
            ctx.restore();
        }
        ctx.restore();
    }
}
