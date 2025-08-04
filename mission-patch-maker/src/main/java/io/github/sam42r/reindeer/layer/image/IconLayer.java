package io.github.sam42r.reindeer.layer.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.server.VaadinService;
import io.github.sam42r.reindeer.layer.MissionPatchLayer;
import io.github.sam42r.reindeer.layer.MissionPatchLayerProperty;
import lombok.*;
import org.vaadin.pekkam.Canvas;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IconLayer implements MissionPatchLayer {

    private static final String FONT_AWESOME_VERSION = "6.6.0";
    private static final String FONT_AWESOME_STYLE = "solid";

    private FontAwesome.Solid icon = FontAwesome.Solid.ROCKET;
    @Builder.Default
    private int x = DEFAULT_CANVAS_WIDTH / 2;
    @Builder.Default
    private int y = DEFAULT_CANVAS_HEIGHT / 2;
    @Builder.Default
    private int width = (int) (DEFAULT_CANVAS_WIDTH * 0.25);
    @Builder.Default
    private int height = (int) (DEFAULT_CANVAS_HEIGHT * 0.25);
    @Builder.Default
    private String color = "Black";
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
                new MissionPatchLayerProperty<>("icon", FontAwesome.Solid.class, this::getIcon, this::setIcon, Arrays.stream(FontAwesome.Solid.values()).toList()),
                new MissionPatchLayerProperty<>("x", Integer.class, this::getX, this::setX),
                new MissionPatchLayerProperty<>("y", Integer.class, this::getY, this::setY),
                new MissionPatchLayerProperty<>("width", Integer.class, this::getWidth, this::setWidth),
                new MissionPatchLayerProperty<>("height", Integer.class, this::getHeight, this::setHeight),
                new MissionPatchLayerProperty<>("color", String.class, this::getColor, this::setColor),
                new MissionPatchLayerProperty<>("rotation", Integer.class, this::getRotation, this::setRotation)
        );
    }

    @SneakyThrows
    @Override
    public void draw(Canvas canvas) {
        var name = (icon.name().startsWith("_") ? icon.name().substring(1) : icon.name())
                .toLowerCase().replace("_", "-");
        var image = "webjars/font-awesome/%s/svgs/%s/%s.svg".formatted(FONT_AWESOME_VERSION, FONT_AWESOME_STYLE, name);

        //drawWithImageLoaded(canvas, image);
        drawWithImageEmbedded(canvas, image);
    }

    @Deprecated
    @SuppressWarnings("unused")
    private void drawWithImageLoaded(Canvas canvas, String image) {
        canvas.addImageLoadListener(e -> {
            var ctx = canvas.getContext();

            ctx.save();
            ctx.translate(x, y);
            ctx.rotate(rotation * Math.PI / 180);
            ctx.drawImage(e.getSrc(), -(width / 2d), -(height / 2d), width, height);
            ctx.restore();

            e.unregisterListener();
        });

        canvas.loadImage(image);
    }

    private void drawWithImageEmbedded(Canvas canvas, String image) {
        var resource = VaadinService.getCurrent().getResource(image);

        try (var inputStream = resource.openStream()) {
            canvas.addImageLoadListener(e -> {
                if (!e.getSrc().equals(source)) {
                    return;
                }

                var ctx = canvas.getContext();

                ctx.save();
                ctx.translate(x, y);
                ctx.rotate(rotation * Math.PI / 180);
                ctx.drawImage(e.getSrc(), -(width / 2d), -(height / 2d), width, height);
                ctx.restore();

                e.unregisterListener();
            });

            var svg = addColor(inputStream.readAllBytes(), URLEncoder.encode(color, StandardCharsets.UTF_8));
            source = "data:image/svg+xml;charset=utf-8,%s".formatted(svg);

            canvas.loadImage(source);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @SneakyThrows
    private String addColor(byte[] bytes, String color) {
        var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        var transformer = TransformerFactory.newInstance().newTransformer();

        var document = builder.parse(new ByteArrayInputStream(bytes));

        var nodes = document.getDocumentElement().getElementsByTagName("path");
        if (nodes.getLength() > 0) {
            var element = (Element) nodes.item(0);
            element.setAttribute("fill", color);
        }

        var outputStream = new ByteArrayOutputStream();

        transformer.transform(new DOMSource(document), new StreamResult(outputStream));

        return outputStream.toString();
    }

}
