package io.github.sam42r.reindeer.layer.simple;

import io.github.sam42r.reindeer.canvas.Canvas;
import io.github.sam42r.reindeer.layer.MissionPatchLayerProperty;
import io.github.sam42r.reindeer.layer.image.ImageLayer;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CircleLayer extends AbstractSimpleLayer {

    @Builder.Default
    private int x = DEFAULT_CANVAS_WIDTH / 2;
    @Builder.Default
    private int y = DEFAULT_CANVAS_HEIGHT / 2;
    @Builder.Default
    private int radius = (int) ((DEFAULT_CANVAS_WIDTH * 0.9) / 2);
    @Builder.Default
    private double stroke = 1.0;
    @Builder.Default
    private String color = "Black";
    @Builder.Default
    private String fill = null;

    @Override
    public Category getCategory() {
        return Category.SIMPLE;
    }

    @Override
    public List<MissionPatchLayerProperty<?>> properties() {
        return List.of(
                new MissionPatchLayerProperty<>("x", Integer.class, this::getX, this::setX),
                new MissionPatchLayerProperty<>("y", Integer.class, this::getY, this::setY),
                new MissionPatchLayerProperty<>("radius", Integer.class, this::getRadius, this::setRadius),
                new MissionPatchLayerProperty<>("stroke", Double.class, this::getStroke, this::setStroke),
                new MissionPatchLayerProperty<>("color", String.class, this::getColor, this::setColor),
                new MissionPatchLayerProperty<>("background-color", String.class, this::getFill, this::setFill),
                new MissionPatchLayerProperty<>("background-image", ImageLayer.Image.class, this::getBackgroundImage, this::setBackgroundImage)
        );
    }

    @Override
    public void draw(Canvas canvas) {
        var ctx = canvas.getContext();

        ctx.save();

        ctx.beginPath();
        ctx.setStrokeStyle(color);
        ctx.setLineWidth(stroke);
        ctx.arc(x, y, radius, 0, 2 * Math.PI, false);
        ctx.stroke();

        if (fill != null) {
            ctx.setFillStyle(fill);
            ctx.fill();
        }

        if (getBackgroundImage() != null) {
            ctx.clip();
            ctx.drawImage(getBackgroundSource(), x - radius, y - radius, radius * 2d, radius * 2d);
        }

        ctx.restore();
    }
}
