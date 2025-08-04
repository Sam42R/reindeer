package io.github.sam42r.reindeer.layer.simple;

import io.github.sam42r.reindeer.layer.MissionPatchLayer;
import io.github.sam42r.reindeer.layer.MissionPatchLayerProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vaadin.pekkam.Canvas;

import java.awt.geom.Point2D;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolygonLayer implements MissionPatchLayer {

    @Builder.Default
    private int x = DEFAULT_CANVAS_WIDTH / 2;
    @Builder.Default
    private int y = DEFAULT_CANVAS_HEIGHT / 2;
    @Builder.Default
    private int edges = 8;
    private int size = (int) ((DEFAULT_CANVAS_WIDTH * 0.9) / 2);
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
                new MissionPatchLayerProperty<>("size", Integer.class, this::getSize, this::setSize),
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
        ctx.translate(x, y);
        ctx.rotate(rotation * Math.PI / 180);
        ctx.translate(-x, -y);

        ctx.beginPath();
        for (var i = 0; i < edges; i++) {
            var point = new Point2D.Double(
                    x + size * Math.cos(i * 2 * Math.PI / edges),
                    y + size * Math.sin(i * 2 * Math.PI / edges));
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

        ctx.restore();
    }
}
