package io.github.sam42r.reindeer.layer.simple;

import io.github.sam42r.reindeer.canvas.Canvas;
import io.github.sam42r.reindeer.layer.AbstractMissionPatchLayer;
import io.github.sam42r.reindeer.layer.MissionPatchLayerProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RectangleLayer extends AbstractMissionPatchLayer {

    @Builder.Default
    private int x = (int) (DEFAULT_CANVAS_WIDTH * 0.1);
    @Builder.Default
    private int y = (int) (DEFAULT_CANVAS_HEIGHT * 0.1);
    @Builder.Default
    private int width = (int) (DEFAULT_CANVAS_WIDTH * 0.8);
    @Builder.Default
    private int height = (int) (DEFAULT_CANVAS_HEIGHT * 0.8);
    @Builder.Default
    private double stroke = 1.0;
    @Builder.Default
    private String color = "Black";
    @Builder.Default
    private String fill = null;
    @Builder.Default
    private int rotation = 0;

    @Override
    public Category getCategory() {
        return Category.SIMPLE;
    }

    @Override
    public List<MissionPatchLayerProperty<?>> properties() {
        return List.of(
                new MissionPatchLayerProperty<>("x", Integer.class, this::getX, this::setX),
                new MissionPatchLayerProperty<>("y", Integer.class, this::getY, this::setY),
                new MissionPatchLayerProperty<>("width", Integer.class, this::getWidth, this::setWidth),
                new MissionPatchLayerProperty<>("height", Integer.class, this::getHeight, this::setHeight),
                new MissionPatchLayerProperty<>("stroke", Double.class, this::getStroke, this::setStroke),
                new MissionPatchLayerProperty<>("color", String.class, this::getColor, this::setColor),
                new MissionPatchLayerProperty<>("background-color", String.class, this::getFill, this::setFill),
                new MissionPatchLayerProperty<>("rotation", Integer.class, this::getRotation, this::setRotation)
        );
    }

    @Override
    public void draw(Canvas canvas) {
        var ctx = canvas.getContext();

        ctx.save();
        ctx.translate(x + (double) width / 2, y + (double) height / 2);
        ctx.rotate(rotation * Math.PI / 180);
        ctx.translate(-(x + (double) width / 2), -(y + (double) height / 2));

        ctx.beginPath();
        ctx.setStrokeStyle(color);
        ctx.setLineWidth(stroke);
        ctx.rect(x, y, width, height);
        ctx.stroke();

        if (fill != null) {
            ctx.setFillStyle(fill);
            ctx.fill();
        }

        ctx.restore();
    }
}
