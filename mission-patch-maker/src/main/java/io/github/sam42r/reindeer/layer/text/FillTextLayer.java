package io.github.sam42r.reindeer.layer.text;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.vaadin.pekkam.CanvasRenderingContext2D;

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
