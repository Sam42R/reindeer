package io.github.sam42r.reindeer.color;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.vaadin.addons.parttio.colorful.HexColorPicker;

import java.util.Arrays;

public class ColorChooser extends CustomField<String> {

    private final ComboBox<String> comboBox;

    private String color;

    public ColorChooser() {
        this(null);
    }

    public ColorChooser(String label) {
        this.comboBox = new ComboBox<>();
        comboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);

        var button = new Button(VaadinIcon.PALETTE.create());
        button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON);
        button.getStyle().setBackgroundColor("transparent");
        button.getStyle().setBorder("none");

        comboBox.setLabel(label);
        comboBox.setRenderer(
                new ComponentRenderer<>(string -> {
                    var icon = VaadinIcon.CIRCLE.create();
                    icon.setColor(string);
                    return new HorizontalLayout(icon, new Span(string));
                }));
        comboBox.setItems(Arrays.stream(ColorNames.values()).map(ColorNames::name).toList());
        comboBox.setPrefixComponent(button);

        comboBox.addValueChangeListener(e -> {
            if (button.getIcon() instanceof Icon icon) {
                icon.setColor(e.getValue());
            }
            // triggers ValueChangeEvent
            setValue(e.getValue());
        });

        var hexColorPicker = new HexColorPicker();
        hexColorPicker.addValueChangeListener(e -> comboBox.setValue(e.getValue().hex()));

        Popover popover = new Popover();
        popover.setTarget(button);
        popover.addThemeVariants(PopoverVariant.ARROW, PopoverVariant.LUMO_NO_PADDING);
        popover.setPosition(PopoverPosition.BOTTOM);
        popover.setModal(true);
        popover.add(hexColorPicker);

        add(comboBox);
    }

    @Override
    protected String generateModelValue() {
        return color;
    }

    @Override
    protected void setPresentationValue(String value) {
        this.color = value;
        comboBox.setValue(value);
    }
}
