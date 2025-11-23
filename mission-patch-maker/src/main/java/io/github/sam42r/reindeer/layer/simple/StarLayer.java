package io.github.sam42r.reindeer.layer.simple;

import io.github.sam42r.reindeer.canvas.Canvas;
import io.github.sam42r.reindeer.layer.MissionPatchLayerProperty;
import io.github.sam42r.reindeer.layer.image.ImageLayer;
import lombok.*;

import java.awt.geom.Point2D;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StarLayer extends AbstractSimpleLayer {

    @Builder.Default
    private int x = DEFAULT_CANVAS_WIDTH / 2;
    @Builder.Default
    private int y = DEFAULT_CANVAS_HEIGHT / 2;
    @Builder.Default
    private int edges = 5;
    @Builder.Default
    private int innerRadius = (int) ((DEFAULT_CANVAS_WIDTH * 0.4) / 2);
    @Builder.Default
    private int outerRadius = (int) ((DEFAULT_CANVAS_WIDTH * 0.9) / 2);
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
                new MissionPatchLayerProperty<>("edges", Integer.class, this::getEdges, this::setEdges, 3, 32),
                new MissionPatchLayerProperty<>("inner-radius", Integer.class, this::getInnerRadius, this::setInnerRadius),
                new MissionPatchLayerProperty<>("outer-radius", Integer.class, this::getOuterRadius, this::setOuterRadius),
                new MissionPatchLayerProperty<>("stroke", Double.class, this::getStroke, this::setStroke),
                new MissionPatchLayerProperty<>("color", String.class, this::getColor, this::setColor),
                new MissionPatchLayerProperty<>("background-color", String.class, this::getFill, this::setFill),
                new MissionPatchLayerProperty<>("background-image", ImageLayer.Image.class, this::getBackgroundImage, this::setBackgroundImage),
                new MissionPatchLayerProperty<>("rotation", Integer.class, this::getRotation, this::setRotation)
        );
    }

    @Override
    public void draw(Canvas canvas) {
        var ctx = canvas.getContext();

        ctx.save();
        ctx.translate(x, y);
        ctx.rotate(rotation * Math.PI / 180);
        ctx.translate(-x, -y);

        double angle = Math.PI / edges;

        ctx.beginPath();
        for (int i = 0; i < 2 * edges; i++) {
            var radius = (i & 1) == 0 ? outerRadius : innerRadius;
            var point = new Point2D.Double(
                    x + Math.cos(i * angle) * radius,
                    y + Math.sin(i * angle) * radius);
            if (i == 0) {
                ctx.moveTo(point.getX(), point.getY());
            } else {
                ctx.lineTo(point.getX(), point.getY());
            }
        }
        ctx.closePath();

        ctx.setStrokeStyle(color);
        ctx.setLineWidth(stroke);
        ctx.stroke();

        if (fill != null) {
            ctx.setFillStyle(fill);
            ctx.fill();
        }

        if (getBackgroundImage() != null) {
            ctx.clip();
            ctx.drawImage(getBackgroundSource(), x - outerRadius, y - outerRadius, outerRadius * 2d, outerRadius * 2d);
        }

        ctx.restore();
    }
}
