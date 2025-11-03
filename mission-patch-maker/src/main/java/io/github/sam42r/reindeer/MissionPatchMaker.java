package io.github.sam42r.reindeer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.google.common.net.MediaType;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import elemental.json.Json;
import in.virit.color.HexColor;
import io.github.sam42r.reindeer.color.ColorNames;
import io.github.sam42r.reindeer.layer.MissionPatchLayer;
import io.github.sam42r.reindeer.layer.MissionPatchLayerProperty;
import io.github.sam42r.reindeer.layer.image.ImageLayer;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.addons.parttio.colorful.HexColorPicker;
import org.vaadin.pekkam.Canvas;
import org.vaadin.pekkam.event.ImageLoadEvent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.sam42r.reindeer.layer.MissionPatchLayer.DEFAULT_CANVAS_HEIGHT;
import static io.github.sam42r.reindeer.layer.MissionPatchLayer.DEFAULT_CANVAS_WIDTH;

@Slf4j
public class MissionPatchMaker extends VerticalLayout {

    private final transient ServiceLoader<MissionPatchLayer> serviceLoader = ServiceLoader.load(MissionPatchLayer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<MissionPatchLayer> missionPatchLayers = new ArrayList<>();

    private Canvas canvas;

    private Grid<MissionPatchLayer> availableLayersGrid;
    private Grid<MissionPatchLayer> selectedLayersGrid;
    private Grid<MissionPatchLayerProperty<?>> propertiesGrid;

    public MissionPatchMaker() {
        // basic setup
        setSizeFull();
        setPadding(false);

        // layout components
        var toolbar = toolbar();

        var view = view();
        view.setMinWidth(300, Unit.PIXELS);

        var layers = layers();
        layers.setMinHeight(150, Unit.PIXELS);

        var properties = properties();
        properties.setMinHeight(150, Unit.PIXELS);

        var toolSplit = new SplitLayout(layers, properties);
        toolSplit.setSizeFull();
        toolSplit.setSplitterPosition(30);
        toolSplit.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        toolSplit.setOrientation(SplitLayout.Orientation.VERTICAL);

        var tools = new VerticalLayout(toolSplit);
        tools.setSizeFull();
        tools.setPadding(false);
        tools.setMinWidth(250, Unit.PIXELS);
        tools.setMaxWidth(500, Unit.PIXELS);

        var center = new VerticalLayout(toolbar, view);

        var rightSplit = new SplitLayout(center, tools);
        rightSplit.setSizeFull();
        rightSplit.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);

        var navigation = navigation();
        navigation.setMinWidth(150, Unit.PIXELS);
        navigation.setMaxWidth(300, Unit.PIXELS);

        var leftSplit = new SplitLayout(navigation, rightSplit);
        leftSplit.setSizeFull();
        leftSplit.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);

        add(leftSplit);

        // finally do initial draw
        selectedLayersGrid.setItems(missionPatchLayers);
        draw();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        draw();
    }

    private HorizontalLayout navigation() {
        var tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);

        var empty = new Tab();
        empty.setEnabled(false);

        var simple = new Tab(FontAwesome.Solid.SHAPES.create());
        simple.setTooltipText("Simple");

        var images = new Tab(FontAwesome.Solid.IMAGE.create());
        images.setTooltipText("Image");

        var text = new Tab(FontAwesome.Solid.FONT.create());
        text.setTooltipText("Text");

        var template = new Tab(FontAwesome.Solid.OBJECT_GROUP.create());
        template.setTooltipText("Template");

        var unsorted = new Tab(FontAwesome.Solid.BROOM_BALL.create());
        unsorted.setTooltipText("Unsorted");

        tabs.add(empty, simple, images, text, template, unsorted);

        availableLayersGrid = new Grid<>(MissionPatchLayer.class, false);
        availableLayersGrid.setSizeFull();
        availableLayersGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

        var add = new Button(FontAwesome.Regular.SQUARE_PLUS.create());
        add.setEnabled(false);
        add.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
        add.setTooltipText("Add");
        add.addClickListener(e -> add());

        availableLayersGrid.addColumn(MissionPatchLayer::getName).setHeader("Layer").setAutoWidth(true).setFlexGrow(1);
        availableLayersGrid.addColumn(layer -> null).setHeader(add).setAutoWidth(true).setFlexGrow(0);

        availableLayersGrid.addSelectionListener(e -> add.setEnabled(!e.getAllSelectedItems().isEmpty()));

        tabs.addSelectedChangeListener(e -> {
            if (e.getSelectedTab() == simple) {
                availableLayersGrid.setItems(layers(MissionPatchLayer.Category.SIMPLE));
            } else if (e.getSelectedTab() == images) {
                availableLayersGrid.setItems(layers(MissionPatchLayer.Category.IMAGE));
            } else if (e.getSelectedTab() == text) {
                availableLayersGrid.setItems(layers(MissionPatchLayer.Category.TEXT));
            } else if (e.getSelectedTab() == template) {
                availableLayersGrid.setItems(layers(MissionPatchLayer.Category.TEMPLATE));
            } else if (e.getSelectedTab() == unsorted) {
                availableLayersGrid.setItems(layers(MissionPatchLayer.Category.NONE));
            }
        });

        tabs.setSelectedTab(simple);
        availableLayersGrid.setItems(layers(MissionPatchLayer.Category.SIMPLE));

        var layout = new HorizontalLayout(tabs, availableLayersGrid);
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        return layout;
    }

    private List<MissionPatchLayer> layers(MissionPatchLayer.Category category) {
        return serviceLoader.stream()
                .map(ServiceLoader.Provider::get)
                .filter(v -> category.equals(v.getCategory()))
                .toList();
    }

    @SneakyThrows
    private void add() {
        var missionPatchLayer = availableLayersGrid.getSelectedItems().stream().findAny().orElseThrow();

        var instance = missionPatchLayer.getClass().getDeclaredConstructor().newInstance();
        missionPatchLayers.add(instance);

        selectedLayersGrid.setItems(missionPatchLayers);
        draw();
    }

    private HorizontalLayout view() {
        canvas = new Canvas(DEFAULT_CANVAS_WIDTH, DEFAULT_CANVAS_HEIGHT);
        canvas.setMaxWidth(DEFAULT_CANVAS_WIDTH, Unit.PIXELS);
        canvas.setMaxHeight(DEFAULT_CANVAS_HEIGHT, Unit.PIXELS);

        var layout = new HorizontalLayout(canvas);
        layout.setSizeFull();
        layout.setAlignItems(Alignment.CENTER);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        return layout;
    }

    private HorizontalLayout toolbar() {
        var loadFromJsonButton = new Button(VaadinIcon.CLOUD_UPLOAD_O.create());
        loadFromJsonButton.setTooltipText("Upload JSON");

        var loadFromJson = new Upload();
        loadFromJson.setDropAllowed(false);
        loadFromJson.setMaxFiles(1);
        loadFromJson.setAcceptedFileTypes("application/json");
        loadFromJson.setUploadButton(loadFromJsonButton);
        loadFromJson.setUploadHandler(uploadEvent -> {
            var layers = objectMapper.readValue(uploadEvent.getInputStream(), MissionPatchLayer[].class);
            uploadEvent.getUI().access(() -> {
                // clear uploaded files list
                loadFromJson.getElement().setPropertyJson("files", Json.createArray());
                // set loaded layers
                missionPatchLayers.clear();
                missionPatchLayers.addAll(Arrays.stream(layers).toList());
                selectedLayersGrid.setItems(missionPatchLayers);
                draw();
            });
        });

        var saveAsJsonButton = new Button((VaadinIcon.CLOUD_DOWNLOAD_O.create()));
        saveAsJsonButton.setTooltipText("Download JSON");

        var saveAsJson = new Anchor();
        saveAsJson.add(saveAsJsonButton);
        saveAsJson.setHref(DownloadHandler.fromInputStream(downloadEvent ->
                exportToJson(downloadEvent.getUI())));

        var saveAsPngButton = new Button(VaadinIcon.FILE_PICTURE.create());
        saveAsPngButton.setTooltipText("Save as PNG");
        saveAsPngButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var saveAsPng = new Anchor();
        saveAsPng.add(saveAsPngButton);
        saveAsPng.setHref(DownloadHandler.fromInputStream(downloadEvent ->
                exportToPng(downloadEvent.getUI())));

        var layout = new HorizontalLayout(loadFromJson, saveAsJson, saveAsPng);
        layout.setWidthFull();
        layout.setJustifyContentMode(JustifyContentMode.END);

        return layout;
    }

    private void draw() {
        var ctx = canvas.getContext();

        ctx.clearRect(0, 0, DEFAULT_CANVAS_WIDTH, DEFAULT_CANVAS_HEIGHT);

        var numOfRegistrations = new AtomicLong(0);
        ComponentEventListener<ImageLoadEvent> listener = e -> {
            e.unregisterListener();
            var pendingRegistrations = numOfRegistrations.decrementAndGet();
            log.trace("Image loaded. Num of pending images: {}", pendingRegistrations);
            if (pendingRegistrations == 0) {
                drawToCanvas();
            }
        };
        numOfRegistrations.set(
                missionPatchLayers.stream()
                        .map(v -> v.preloadImage(canvas, listener))
                        .filter(Objects::nonNull)
                        .count()
        );

        if (numOfRegistrations.get() == 0) {
            drawToCanvas();
        }
        // TODO maybe show some loading animation!?
    }

    private void drawToCanvas() {
        missionPatchLayers.forEach(v -> v.draw(canvas));
    }

    @SneakyThrows
    private DownloadResponse exportToPng(@NonNull UI ui) {
        var downloadResponse = new CompletableFuture<DownloadResponse>();

        ui.access(() ->
                canvas.getElement()
                        .callJsFunction("toDataURL", MediaType.PNG.toString())
                        .then(String.class, dataUrl -> {
                            var data = Base64.getDecoder().decode(dataUrl.split(",")[1]);
                            downloadResponse.completeAsync(() -> new DownloadResponse(
                                    new ByteArrayInputStream(data),
                                    "MissionPatch_%d.png".formatted(System.currentTimeMillis()),
                                    MediaType.PNG.toString(),
                                    data.length
                            ));
                        }));

        return downloadResponse.get();
    }

    @SneakyThrows
    private DownloadResponse exportToJson(@NonNull UI ui) {
        var outputStream = new ByteArrayOutputStream();

        objectMapper
                .writerFor(objectMapper.getTypeFactory().constructCollectionType(List.class, MissionPatchLayer.class))
                .withDefaultPrettyPrinter()
                .writeValue(outputStream, this.missionPatchLayers);

        var data = outputStream.toByteArray();

        return new DownloadResponse(
                new ByteArrayInputStream(data),
                "MissionPatch_%d.json".formatted(System.currentTimeMillis()),
                "application/json",
                data.length
        );
    }

    private VerticalLayout layers() {
        selectedLayersGrid = new Grid<>(MissionPatchLayer.class, false);
        selectedLayersGrid.setSizeFull();
        selectedLayersGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

        var delete = new Button(FontAwesome.Regular.TRASH_CAN.create());
        delete.setEnabled(false);
        delete.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        delete.setTooltipText("Delete");
        delete.addClickListener(e -> delete());

        var up = new Button(FontAwesome.Regular.SQUARE_CARET_UP.create());
        up.setEnabled(false);
        up.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON);
        up.setTooltipText("Up");
        up.addClickListener(e -> moveUp());

        var down = new Button(FontAwesome.Regular.SQUARE_CARET_DOWN.create());
        down.setEnabled(false);
        down.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON);
        down.setTooltipText("Down");
        down.addClickListener(e -> moveDown());

        var actions = new HorizontalLayout(up, down, delete);
        actions.setPadding(false);
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-xs");

        selectedLayersGrid.addColumn(MissionPatchLayer::getName)
                .setHeader("Layer")
                .setAutoWidth(true)
                .setFlexGrow(1);
        selectedLayersGrid.addColumn(layer -> null)
                .setHeader(actions)
                .setAutoWidth(true)
                .setFlexGrow(0);

        selectedLayersGrid.addSelectionListener(e -> {
            var selectedItemOptional = e.getFirstSelectedItem();
            if (selectedItemOptional.isPresent()) {
                var selectedItem = selectedItemOptional.get();

                var index = missionPatchLayers.indexOf(selectedItem);
                up.setEnabled(index > 0);
                down.setEnabled(index < missionPatchLayers.size() - 1);

                delete.setEnabled(true);
                showProperties(selectedItem);
            } else {
                up.setEnabled(false);
                down.setEnabled(false);
                delete.setEnabled(false);
                showProperties(null);
            }
        });

        var layout = new VerticalLayout(selectedLayersGrid);
        layout.setSizeFull();
        layout.setPadding(false);
        return layout;
    }

    private void moveUp() {
        var missionPatchLayer = selectedLayersGrid.getSelectedItems().stream().findAny().orElseThrow();

        var index = missionPatchLayers.indexOf(missionPatchLayer);

        missionPatchLayers.set(index, missionPatchLayers.get(index - 1));
        missionPatchLayers.set(index - 1, missionPatchLayer);

        selectedLayersGrid.setItems(missionPatchLayers);
        selectedLayersGrid.select(missionPatchLayer);

        draw();
    }

    private void moveDown() {
        var missionPatchLayer = selectedLayersGrid.getSelectedItems().stream().findAny().orElseThrow();

        var index = missionPatchLayers.indexOf(missionPatchLayer);

        missionPatchLayers.set(index, missionPatchLayers.get(index + 1));
        missionPatchLayers.set(index + 1, missionPatchLayer);

        selectedLayersGrid.setItems(missionPatchLayers);
        selectedLayersGrid.select(missionPatchLayer);

        draw();
    }

    private void delete() {
        var missionPatchLayer = selectedLayersGrid.getSelectedItems().stream().findAny().orElseThrow();

        missionPatchLayers.remove(missionPatchLayer);

        selectedLayersGrid.setItems(missionPatchLayers);
        draw();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private VerticalLayout properties() {
        propertiesGrid = new Grid<>();
        propertiesGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

        ValueProvider<MissionPatchLayerProperty<?>, Component> propertyEditor = missionPatchLayerProperty -> {
            var clazz = missionPatchLayerProperty.clazz();

            Component editor;
            if (clazz == Integer.class) {
                var getter = (Supplier<Integer>) missionPatchLayerProperty.getter();
                var setter = (Consumer<Integer>) missionPatchLayerProperty.setter();

                var integerField = new IntegerField();
                integerField.setStepButtonsVisible(true);
                integerField.setValue(getter.get());
                if (missionPatchLayerProperty.min() != null && missionPatchLayerProperty.max() != null) {
                    integerField.setMin((int) missionPatchLayerProperty.min());
                    integerField.setMax((int) missionPatchLayerProperty.max());
                }
                integerField.addValueChangeListener(e -> {
                    setter.accept(e.getValue());
                    draw();
                });

                editor = integerField;
            } else if (clazz == Double.class) {
                var getter = (Supplier<Double>) missionPatchLayerProperty.getter();
                var setter = (Consumer<Double>) missionPatchLayerProperty.setter();

                var numberField = new NumberField();
                numberField.setStepButtonsVisible(true);
                numberField.setValue(getter.get());
                if (missionPatchLayerProperty.min() != null && missionPatchLayerProperty.max() != null) {
                    numberField.setMin((double) missionPatchLayerProperty.min());
                    numberField.setMax((double) missionPatchLayerProperty.max());
                }
                numberField.addValueChangeListener(e -> {
                    setter.accept(e.getValue());
                    draw();
                });

                editor = numberField;
            } else if (clazz == String.class) {
                var getter = (Supplier<String>) missionPatchLayerProperty.getter();
                var setter = (Consumer<String>) missionPatchLayerProperty.setter();

                if (missionPatchLayerProperty.name().toLowerCase().contains("color")) {
                    var color = getter.get();

                    var stringColorPicker = new ComboBox<String>();
                    stringColorPicker.setRenderer(
                            new ComponentRenderer<>(string -> {
                                var icon = VaadinIcon.CIRCLE.create();
                                icon.setColor(string);
                                return new HorizontalLayout(icon, new Span(string));
                            }));
                    stringColorPicker.setItems(Arrays.stream(ColorNames.values()).map(ColorNames::name).toList());
                    stringColorPicker.setValue(color);

                    var hexColorPicker = new HexColorPicker();
                    if (color != null) {
                        hexColorPicker.setValue(HexColor.of(color.startsWith("#") ? color : ColorNames.of(color).getHex()));
                    }

                    hexColorPicker.addValueChangeListener(e -> stringColorPicker.setValue(e.getValue().hex()));

                    stringColorPicker.addValueChangeListener(e -> {
                        setter.accept(e.getValue());
                        draw();
                    });

                    editor = new VerticalLayout(stringColorPicker, hexColorPicker);
                } else if (missionPatchLayerProperty.name().toLowerCase().contains("family")) {
                    var fontFamilyPicker = new ComboBox<String>();
                    fontFamilyPicker.setRenderer(
                            new ComponentRenderer<>(string -> {
                                var text = new Span(string);
                                text.getStyle().set("font-family", string);
                                return new HorizontalLayout(text);
                            }));
                    fontFamilyPicker.addValueChangeListener(e -> {
                        setter.accept(e.getValue());
                        draw();
                    });
                    fontFamilyPicker.setItems((List<String>) missionPatchLayerProperty.values());
                    fontFamilyPicker.setValue(getter.get());

                    editor = fontFamilyPicker;
                } else {
                    if (missionPatchLayerProperty.values().isEmpty()) {
                        var textField = new TextField();
                        textField.setValue(getter.get());
                        textField.addValueChangeListener(e -> {
                            setter.accept(e.getValue());
                            draw();
                        });

                        editor = textField;
                    } else {
                        var comboBox = new ComboBox<String>();
                        comboBox.setAllowCustomValue(true);
                        comboBox.setItems((List<String>) missionPatchLayerProperty.values());
                        comboBox.setValue(getter.get());
                        comboBox.addValueChangeListener(e -> {
                            setter.accept(e.getValue());
                            draw();
                        });

                        editor = comboBox;
                    }
                }
            } else if (clazz.isEnum()) {
                var getter = (Supplier<Enum>) missionPatchLayerProperty.getter();
                var setter = (Consumer<Enum>) missionPatchLayerProperty.setter();

                var combo = new ComboBox<Enum>();
                combo.setItems((Collection<Enum>) missionPatchLayerProperty.values());
                combo.setValue(getter.get());
                combo.addValueChangeListener(e -> {
                    setter.accept(e.getValue());
                    draw();
                });

                editor = combo;
            } else if (clazz == Boolean.class) {
                var getter = (Supplier<Boolean>) missionPatchLayerProperty.getter();
                var setter = (Consumer<Boolean>) missionPatchLayerProperty.setter();

                var toggleButton = new ToggleButton();
                toggleButton.setValue(getter.get());
                toggleButton.addValueChangeListener(e -> {
                    setter.accept(toggleButton.getValue());
                    draw();
                });

                editor = toggleButton;
            } else if (clazz == ImageLayer.Image.class) {
                var getter = (Supplier<ImageLayer.Image>) missionPatchLayerProperty.getter();
                var setter = (Consumer<ImageLayer.Image>) missionPatchLayerProperty.setter();

                var uploadButton = new Button("Upload Image");
                uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

                var upload = new Upload();
                upload.setMaxFiles(1);
                upload.setAcceptedFileTypes(
                        MediaType.BMP.toString(),
                        MediaType.GIF.toString(),
                        MediaType.JPEG.toString(),
                        MediaType.PNG.toString(),
                        MediaType.SVG_UTF_8.toString(),
                        MediaType.WEBP.toString()
                );
                upload.setUploadHandler(uploadEvent -> {
                    var image = new ImageLayer.Image(
                            uploadEvent.getContentType(),
                            uploadEvent.getInputStream().readAllBytes());
                    setter.accept(image);
                    uploadEvent.getUI().access(this::draw);
                });
                upload.setUploadButton(uploadButton);
                upload.setDropLabelIcon(VaadinIcon.PICTURE.create());
                upload.setDropLabel(new Span("Drop image here"));

                editor = upload;
            } else {
                editor = new Span("NA");
            }

            return editor;
        };

        propertiesGrid.addColumn(MissionPatchLayerProperty::name)
                .setHeader("Property")
                .setAutoWidth(true)
                .setFlexGrow(0);
        propertiesGrid.addComponentColumn(propertyEditor)
                .setHeader("Value")
                .setAutoWidth(true)
                .setFlexGrow(1);

        var layout = new VerticalLayout(propertiesGrid);
        layout.setSizeFull();
        layout.setPadding(false);
        return layout;
    }

    private void showProperties(MissionPatchLayer layer) {
        propertiesGrid.setItems(layer == null ? List.of() : layer.properties());
    }
}
