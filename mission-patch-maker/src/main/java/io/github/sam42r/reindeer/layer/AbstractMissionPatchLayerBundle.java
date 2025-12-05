package io.github.sam42r.reindeer.layer;

import org.vaadin.pekkam.Canvas;

import java.util.List;

public abstract class AbstractMissionPatchLayerBundle extends AbstractMissionPatchLayer {

    protected final List<MissionPatchLayer> bundle;

    protected AbstractMissionPatchLayerBundle(List<MissionPatchLayer> bundle) {
        this.bundle = bundle;
    }

    @Override
    public void draw(Canvas canvas) {
        bundle.forEach(l -> l.draw(canvas));
    }
}
