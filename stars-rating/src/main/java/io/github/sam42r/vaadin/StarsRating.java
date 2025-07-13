package io.github.sam42r.vaadin;

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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class StarsRating extends Composite<Div> {

    public enum Orientation {
        HORIZONTAL, VERTICAL;
    }

    private final List<Div> stars = new ArrayList<>();
    private final Supplier<Icon> iconSupplier;
    private final Supplier<Icon> selectedIconSupplier;
    private final String color;
    private final String selectedColor;

    private int value;

    private StarsRating(
            int size,
            int initial,
            Orientation orientation,
            Supplier<Icon> iconSupplier,
            Supplier<Icon> selectedIconSupplier,
            String color,
            String selectedColor
    ) {
        this.iconSupplier = iconSupplier;
        this.selectedIconSupplier = selectedIconSupplier;

        this.color = color;
        this.selectedColor = selectedColor;

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

            var icon = iconToSet.get();
            icon.setColor(colorToSet);

            star.add(icon);
        });
    }

    public int getValue() {
        return value;
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
        private String color = "default";
        private String selectedColor = "orange";

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

        public StarsRating build() {
            return new StarsRating(size, initial, orientation, iconSupplier, selectedIconSupplier, color, selectedColor);
        }
    }
}
