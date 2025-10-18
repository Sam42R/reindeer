package io.github.sam42r.reindeer;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import io.github.sam42r.reindeer.color.ColorNames;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class StarsRating extends Composite<Div> {

    public enum Orientation {
        HORIZONTAL, VERTICAL;
    }

    protected interface DisabledStyle<T> {
        T value();
    }

    public static final class None implements DisabledStyle<Void> {
        @Override
        public Void value() {
            return null;
        }
    }

    public static final class Shade implements DisabledStyle<Double> {
        private final Double value;

        public Shade(Double value) {
            if (value < -1.0 || value > 1.0) {
                throw new IllegalArgumentException("Shade should be between -1.0 and 1.0");
            }
            this.value = value;
        }

        @Override
        public Double value() {
            return value;
        }
    }

    public static final class Color implements DisabledStyle<String> {
        private final String value;

        public Color(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }
    }

    public static final class Opacity implements DisabledStyle<Double> {
        private final Double value;

        public Opacity(Double value) {
            if (value < 0.0 || value > 1.0) {
                throw new IllegalArgumentException("Opacity should be between 0.0 and 1.0");
            }
            this.value = value;
        }

        @Override
        public Double value() {
            return value;
        }
    }

    private final List<Div> stars = new ArrayList<>();
    private final Supplier<Icon> iconSupplier;
    private final Supplier<Icon> selectedIconSupplier;
    private final String color;
    private final String selectedColor;

    private final DisabledStyle<?> disabledStyle;

    private boolean enabled;
    private int value;

    private StarsRating(
            int size,
            int initial,
            Orientation orientation,
            Supplier<Icon> iconSupplier,
            Supplier<Icon> selectedIconSupplier,
            String color,
            String selectedColor,
            DisabledStyle<?> disabledStyle,
            boolean enabled
    ) {
        this.iconSupplier = iconSupplier;
        this.selectedIconSupplier = selectedIconSupplier;

        this.color = color;
        this.selectedColor = selectedColor;

        this.disabledStyle = disabledStyle;

        this.enabled = enabled;

        IntStream.range(0, size).forEach(index -> {
            var star = new Div();
            star.add(iconSupplier.get());
            star.addClickListener(this::handleClickEvent);
            stars.add(star);
        });

        var layout = Orientation.HORIZONTAL.equals(orientation) ? new HorizontalLayout() : new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);

        stars.forEach(s -> layout.add(s));
        getContent().add(layout);

        setValue(initial);
    }

    private void handleClickEvent(ClickEvent<Div> clickEvent) {
        if (!enabled) {
            return;
        }

        var newValue = stars.indexOf(clickEvent.getSource()) + 1;

        if (value == newValue && value > 0) {
            newValue = value - 1;
        }

        setValue(newValue);
        fireEvent(new Event(this, false));
    }

    public void setValue(int newValue) {
        this.value = newValue;

        stars.forEach(star -> {
            star.removeAll();

            var iconToSet = stars.indexOf(star) < value ? selectedIconSupplier : iconSupplier;
            var colorToSet = stars.indexOf(star) < value ? selectedColor : color;

            if (!enabled && disabledStyle != null) {
                if (disabledStyle instanceof Color colorStyle) {
                    colorToSet = colorStyle.value();
                } else if (disabledStyle instanceof Shade shadeStyle) {
                    var colorNameOpt = ColorNames.ofColorName(colorToSet);
                    if (colorNameOpt.isPresent()) {
                        colorToSet = colorNameOpt.get().getShade(shadeStyle.value());
                    }
                } else if (disabledStyle instanceof Opacity opacityStyle) {
                    var colorNameOpt = ColorNames.ofColorName(colorToSet);
                    if (colorNameOpt.isPresent()) {
                        colorToSet = colorNameOpt.get().getRgba(opacityStyle.value());
                    }
                }
            }

            var icon = iconToSet.get();
            icon.setColor(colorToSet);

            star.add(icon);
        });
    }

    public int getValue() {
        return value;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        setValue(value);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Registration addChangeListener(ComponentEventListener<StarsRating.Event> listener) {
        return addListener(StarsRating.Event.class, listener);
    }


    /**
     * {@link ComponentEvent} for {@link StarsRating} value change events.
     */
    public static final class Event extends ComponentEvent<StarsRating> {

        public Event(StarsRating source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public static StarsRatingBuilder builder() {
        return new StarsRatingBuilder();
    }

    /**
     * Builder for {@link StarsRating}.
     */
    public static final class StarsRatingBuilder {

        private int size = 5;
        private int initial = 0;
        private Orientation orientation = Orientation.HORIZONTAL;
        private Supplier<Icon> iconSupplier = VaadinIcon.STAR_O::create;
        private Supplier<Icon> selectedIconSupplier = VaadinIcon.STAR::create;
        private String color = ColorNames.Black.name();
        private String selectedColor = ColorNames.Orange.name();
        private DisabledStyle<?> disabledStyle = new Opacity(0.5);
        private boolean enabled = true;

        private StarsRatingBuilder() {
        }

        public StarsRatingBuilder size(int size) {
            this.size = size;
            return this;
        }

        public StarsRatingBuilder initial(int initial) {
            this.initial = initial;
            return this;
        }

        public StarsRatingBuilder orientation(Orientation orientation) {
            this.orientation = orientation;
            return this;
        }

        public StarsRatingBuilder icon(Supplier<Icon> iconSupplier) {
            this.iconSupplier = iconSupplier;
            return this;
        }

        public StarsRatingBuilder selectedIcon(Supplier<Icon> selectedIconSupplier) {
            this.selectedIconSupplier = selectedIconSupplier;
            return this;
        }

        public StarsRatingBuilder color(String color) {
            this.color = color;
            return this;
        }

        public StarsRatingBuilder selectedColor(String selectedColor) {
            this.selectedColor = selectedColor;
            return this;
        }

        public StarsRatingBuilder disabledStyle(DisabledStyle<?> disabledStyle) {
            this.disabledStyle = disabledStyle;
            return this;
        }

        public StarsRatingBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public StarsRating build() {
            return new StarsRating(size, initial, orientation, iconSupplier, selectedIconSupplier, color, selectedColor, disabledStyle, enabled);
        }
    }
}
