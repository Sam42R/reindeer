package io.github.sam42r.reindeer.canvas;

public class CanvasRenderingContext2D extends org.vaadin.pekkam.CanvasRenderingContext2D {

    protected CanvasRenderingContext2D(Canvas canvas) {
        super(canvas);
    }

    public void clip() {
        this.callJsMethod("clip");
    }
}
