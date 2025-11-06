package io.github.sam42r.reindeer.layer.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;
import io.github.sam42r.reindeer.layer.AbstractMissionPatchLayer;
import io.github.sam42r.reindeer.layer.MissionPatchLayerProperty;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.vaadin.pekkam.Canvas;
import org.vaadin.pekkam.event.ImageLoadEvent;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImageLayer extends AbstractMissionPatchLayer {

    private Image image;
    @Builder.Default
    private int x = DEFAULT_CANVAS_WIDTH / 2;
    @Builder.Default
    private int y = DEFAULT_CANVAS_HEIGHT / 2;
    @Builder.Default
    private int width = (int) (DEFAULT_CANVAS_WIDTH * 0.25);
    @Builder.Default
    private int height = (int) (DEFAULT_CANVAS_HEIGHT * 0.25);
    @Builder.Default
    private int rotation = 0;

    @JsonIgnore
    private String source;

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }

    @Override
    public List<MissionPatchLayerProperty<?>> properties() {
        return List.of(
                new MissionPatchLayerProperty<>("file", Image.class, this::getImage, this::setImage),
                new MissionPatchLayerProperty<>("x", Integer.class, this::getX, this::setX),
                new MissionPatchLayerProperty<>("y", Integer.class, this::getY, this::setY),
                new MissionPatchLayerProperty<>("width", Integer.class, this::getWidth, this::setWidth),
                new MissionPatchLayerProperty<>("height", Integer.class, this::getHeight, this::setHeight),
                new MissionPatchLayerProperty<>("rotation", Integer.class, this::getRotation, this::setRotation)
        );
    }

    @Override
    public Registration preloadImage(@NonNull Canvas canvas, @NonNull ComponentEventListener<ImageLoadEvent> listener) {
        if (image == null) {
            return null;
        }

        var registration = canvas.addImageLoadListener(listener);

        source = "data:%s;base64,%s".formatted(
                image.contentType(),
                Base64.getEncoder().encodeToString(image.content()));
        canvas.loadImage(source);

        return registration;
    }

    @Override
    public void draw(Canvas canvas) {
        if (image == null) {
            return;
        }

        var ctx = canvas.getContext();

        ctx.save();
        ctx.translate(x, y);
        ctx.rotate(rotation * Math.PI / 180);
        ctx.drawImage(source, -(width / 2d), -(height / 2d), width, height);
        ctx.restore();
    }

    public record Image(String contentType, byte[] content) {

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Image image) {
                return image.contentType.equals(contentType) && Arrays.equals(image.content, content);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            var hashCodeBuilder = new HashCodeBuilder();
            hashCodeBuilder.append(contentType);
            hashCodeBuilder.append(content);
            return hashCodeBuilder.hashCode();
        }

        @Override
        public String toString() {
            return "Image{contentType=%s, content=%s}".formatted(contentType, Arrays.toString(content));
        }
    }
}
