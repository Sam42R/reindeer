package io.github.sam42r.reindeer.layer.text;

import io.github.sam42r.reindeer.canvas.CanvasRenderingContext2D;
import io.github.sam42r.reindeer.layer.MissionPatchLayerProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StrokeTextLayer extends AbstractTextLayer {

    @Builder.Default
    private double stroke = 1.0;

    @Override
    public List<MissionPatchLayerProperty<?>> properties() {
        var properties = new ArrayList<>(super.properties());

        properties.add(
                new MissionPatchLayerProperty<>("stroke", Double.class, this::getStroke, this::setStroke)
        );

        return properties;
    }

    @Override
    protected void drawText(CanvasRenderingContext2D ctx) {
        ctx.setLineWidth(stroke);
        ctx.setStrokeStyle(getColor());
        ctx.strokeText(getText(), 0, 0);
    }
}
