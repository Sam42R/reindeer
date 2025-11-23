package io.github.sam42r.reindeer.layer.text;

import io.github.sam42r.reindeer.canvas.CanvasRenderingContext2D;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FillTextLayer extends AbstractTextLayer {

    @Override
    protected void drawText(CanvasRenderingContext2D ctx) {
        ctx.setFillStyle(getColor());
        ctx.fillText(getText(), 0, 0);
    }
}
