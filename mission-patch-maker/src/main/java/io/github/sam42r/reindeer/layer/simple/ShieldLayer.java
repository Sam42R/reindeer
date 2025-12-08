package io.github.sam42r.reindeer.layer.simple;

import io.github.sam42r.reindeer.layer.MissionPatchLayerProperty;
import io.github.sam42r.reindeer.layer.image.ImageLayer;
import lombok.*;
import org.vaadin.pekkam.Canvas;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

import static org.vaadin.pekkam.CanvasRenderingContext2D.degreeToRadian;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ShieldLayer extends AbstractSimpleLayer {

    @Builder.Default
    private int x = DEFAULT_CANVAS_WIDTH / 2;
    @Builder.Default
    private int y = DEFAULT_CANVAS_HEIGHT / 2;
    @Builder.Default
    private int outerRadius = (int) ((DEFAULT_CANVAS_WIDTH * 0.9) / 2);
    @Builder.Default
    private int innerRadius = (int) ((DEFAULT_CANVAS_WIDTH * 0.7) / 2);
    @Builder.Default
    private Type type = Type.TYPE_1;
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
                new MissionPatchLayerProperty<>("type", Type.class, this::getType, this::setType, Arrays.stream(Type.values()).toList()),
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
        ctx.rotate(degreeToRadian(rotation));
        ctx.translate(-x, -y);

        ctx.beginPath();

        switch (type) {
            case TYPE_1 -> type1(canvas);
            case TYPE_2 -> type2(canvas);
            case TYPE_3 -> type3(canvas);
            case TYPE_4 -> type4(canvas);
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

    private void type1(Canvas canvas) {
        var ctx = canvas.getContext();

        var p1 = pointOnCircle(0.0);
        var p2 = pointOnCircle(25.0, innerRadius);
        var p3 = pointOnCircle(45.0);
        var p4 = pointOnCircle(120.0);
        var p5 = pointOnCircle(180.0);
        var p6 = pointOnCircle(240.0);
        var p7 = pointOnCircle(315.0);
        var p8 = pointOnCircle(335.0, innerRadius);
        var p9 = pointOnCircle(360.0);

        ctx.moveTo(p1);
        ctx.quadraticCurveTo(p2, p3);
        ctx.quadraticCurveTo(p4, p5);
        ctx.quadraticCurveTo(p6, p7);
        ctx.quadraticCurveTo(p8, p9);
    }

    private void type2(Canvas canvas) {
        var ctx = canvas.getContext();

        var p1 = pointOnCircle(315.0);
        var p2 = pointOnCircle(0.0, innerRadius);
        var p3 = pointOnCircle(45.0);
        var p4 = pointOnCircle(110.0);
        var p5 = pointOnCircle(155.0);
        var p6 = pointOnCircle(180.0);
        var p7 = pointOnCircle(205.0);
        var p8 = pointOnCircle(250.0);

        ctx.moveTo(p1);
        ctx.quadraticCurveTo(p2, p3);
        ctx.bezierCurveTo(p4, p5, p6);
        ctx.bezierCurveTo(p7, p8, p1);
    }

    private void type3(Canvas canvas) {
        var ctx = canvas.getContext();

        var p1 = pointOnCircle(60);
        var p2 = pointOnCircle(120);
        var p3 = pointOnCircle(180);
        var p4 = pointOnCircle(240);
        var p5 = pointOnCircle(300);
        var p6 = pointOnCircle(360);

        ctx.moveTo(p1);
        ctx.quadraticCurveTo(p2, p3);
        ctx.quadraticCurveTo(p4, p5);
        ctx.quadraticCurveTo(p6, p1);
    }

    private void type4(Canvas canvas) {
        var ctx = canvas.getContext();

        var p1 = pointOnCircle(20);
        var p2 = pointOnCircle(25, innerRadius);
        var p3 = pointOnCircle(55);
        var p4 = pointOnCircle(90, innerRadius);
        var p5 = pointOnCircle(135);
        var p6 = pointOnCircle(155);
        var p7 = pointOnCircle(180);
        var p8 = pointOnCircle(195);
        var p9 = pointOnCircle(225);
        var p10 = pointOnCircle(270, innerRadius);
        var p11 = pointOnCircle(305);
        var p12 = pointOnCircle(335, innerRadius);
        var p13 = pointOnCircle(340);
        var p14 = pointOnCircle(360, innerRadius);

        ctx.moveTo(p1);
        ctx.quadraticCurveTo(p2, p3);
        ctx.quadraticCurveTo(new Point2D.Double(p4.getX() - 30, p4.getY()), p5);
        ctx.quadraticCurveTo(p6, p7);
        ctx.quadraticCurveTo(p8, p9);
        ctx.quadraticCurveTo(new Point2D.Double(p10.getX() + 30, p10.getY()), p11);
        ctx.quadraticCurveTo(p12, p13);
        ctx.quadraticCurveTo(p14, p1);
    }

    private Point2D.Double pointOnCircle(double angle) {
        return pointOnCircle(angle, outerRadius);
    }

    private Point2D.Double pointOnCircle(double angle, double radius) {
        var alpha = (angle - 90.0) * Math.PI / 180;

        var px = x + radius * Math.cos(alpha);
        var py = y + radius * Math.sin(alpha);

        return new Point2D.Double(px, py);
    }

    public enum Type {
        TYPE_1,
        TYPE_2,
        TYPE_3,
        TYPE_4;
    }
}
