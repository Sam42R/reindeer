package io.github.sam42r.reindeer.layer.simple;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;
import io.github.sam42r.reindeer.canvas.Canvas;
import io.github.sam42r.reindeer.layer.AbstractMissionPatchLayer;
import io.github.sam42r.reindeer.layer.image.ImageLayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.vaadin.pekkam.event.ImageLoadEvent;

import java.util.Base64;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractSimpleLayer extends AbstractMissionPatchLayer {

    private ImageLayer.Image backgroundImage;
    @JsonIgnore
    private String backgroundSource;

    @Override
    public Registration preloadImage(@NonNull Canvas canvas, @NonNull ComponentEventListener<ImageLoadEvent> listener) {
        if (backgroundImage == null) {
            return null;
        }

        var registration = canvas.addImageLoadListener(listener);

        backgroundSource = "data:%s;base64,%s".formatted(
                backgroundImage.contentType(),
                Base64.getEncoder().encodeToString(backgroundImage.content()));
        canvas.loadImage(backgroundSource);

        return registration;
    }
}
