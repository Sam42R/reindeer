package io.github.sam42r.reindeer.layer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public abstract class AbstractMissionPatchLayer implements MissionPatchLayer {

    private boolean visible;
    private String label;

    protected AbstractMissionPatchLayer() {
        this.visible = true;
    }
}
