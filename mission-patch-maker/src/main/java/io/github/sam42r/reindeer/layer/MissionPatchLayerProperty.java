package io.github.sam42r.reindeer.layer;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record MissionPatchLayerProperty<T>(
        String name,
        Class<T> clazz,
        Supplier<T> getter,
        Consumer<T> setter,
        List<T> values) {

    public MissionPatchLayerProperty(String name, Class<T> clazz, Supplier<T> getter, Consumer<T> setter) {
        this(name, clazz, getter, setter, Collections.emptyList());
    }
}
