package io.github.sam42r.reindeer.layer.template;

import io.github.sam42r.reindeer.layer.AbstractMissionPatchLayerBundle;
import io.github.sam42r.reindeer.layer.MissionPatchLayerProperty;
import io.github.sam42r.reindeer.layer.simple.CircleLayer;

import java.util.List;

public class NasaLayerBundle extends AbstractMissionPatchLayerBundle {

    public NasaLayerBundle() {
        super(List.of(
                CircleLayer.builder()
                        .x(DEFAULT_CANVAS_WIDTH / 2)
                        .y(DEFAULT_CANVAS_HEIGHT / 2)
                        .radius(190)
                        .stroke(10)
                        .color("steelblue")
                        .build(),
                CircleLayer.builder()
                        .x(DEFAULT_CANVAS_WIDTH / 2)
                        .y(DEFAULT_CANVAS_HEIGHT / 2)
                        .radius(165)
                        .stroke(40)
                        .color("darkslategrey")
                        .build(),
                CircleLayer.builder()
                        .x(DEFAULT_CANVAS_WIDTH / 2)
                        .y(DEFAULT_CANVAS_HEIGHT / 2)
                        .radius(140)
                        .stroke(10)
                        .color("red")
                        .build()
        ));
    }

    @Override
    public Category getCategory() {
        return Category.TEMPLATE;
    }

    public String getOuterColor() {
        return ((CircleLayer) bundle.get(0)).getColor();
    }

    public void setOuterColor(String color) {
        ((CircleLayer) bundle.get(0)).setColor(color);
    }

    public String getMiddleColor() {
        return ((CircleLayer) bundle.get(1)).getColor();
    }

    public void setMiddleColor(String color) {
        ((CircleLayer) bundle.get(1)).setColor(color);
    }

    public String getInnerColor() {
        return ((CircleLayer) bundle.get(2)).getColor();
    }

    public void setInnerColor(String color) {
        ((CircleLayer) bundle.get(2)).setColor(color);
    }

    @Override
    public List<MissionPatchLayerProperty<?>> properties() {
        return List.of(
                new MissionPatchLayerProperty<>("outerColor", String.class, this::getOuterColor, this::setOuterColor),
                new MissionPatchLayerProperty<>("middleColor", String.class, this::getMiddleColor, this::setMiddleColor),
                new MissionPatchLayerProperty<>("innerColor", String.class, this::getInnerColor, this::setInnerColor)
        );
    }
}
