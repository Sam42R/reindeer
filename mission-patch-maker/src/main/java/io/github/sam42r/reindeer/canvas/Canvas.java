package io.github.sam42r.reindeer.canvas;

public class Canvas extends org.vaadin.pekkam.Canvas {

    private final CanvasRenderingContext2D context2D = new CanvasRenderingContext2D(this);

    public Canvas(int width, int height) {
        super(width, height);
    }

    @Override
    public CanvasRenderingContext2D getContext() {
        return context2D;
    }
}
