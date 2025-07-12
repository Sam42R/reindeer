package io.github.sam42r.vaadin;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class StarsRating extends Composite<Div> {

    public enum Orientation {
        HORIZONTAL, VERTICAL;
    }

    private static final int DEFAULT_SIZE = 5;
    private static final int DEFAULT_VALUE = 0;
    private static final Orientation DEFAULT_ORIENTATION = Orientation.HORIZONTAL;
    private static final VaadinIcon DEFAULT_ICON = VaadinIcon.STAR_O;
    private static final VaadinIcon DEFAULT_ICON_SELECTED = VaadinIcon.STAR;
    private static final String DEFAULT_COLOR = "default";
    private static final String DEFAULT_SELECTED_COLOR = "orange";

    private final List<Div> stars = new ArrayList<>();
    private final VaadinIcon icon;
    private final VaadinIcon selectedIcon;
    private final String color;
    private final String selectedColor;

    private int value;

    public StarsRating() {
        this(DEFAULT_SIZE, DEFAULT_VALUE, DEFAULT_ORIENTATION, DEFAULT_ICON, DEFAULT_ICON_SELECTED, DEFAULT_COLOR, DEFAULT_SELECTED_COLOR);
    }

    private StarsRating(int size, int initial, Orientation orientation, VaadinIcon icon, VaadinIcon iconSelected, String color, String selectedColor) {
        this.icon = icon;
        this.selectedIcon = iconSelected;

        this.color = color;
        this.selectedColor = selectedColor;

        IntStream.range(0, size).forEach(index -> {
            var star = new Div();
            star.add(icon.create());
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

            var iconToSet = stars.indexOf(star) < value ? selectedIcon : icon;
            var colorToSet = stars.indexOf(star) < value ? selectedColor : color;

            var icon = iconToSet.create();
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

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link StarsRating}.
     */
    public static final class Builder {

        private int size = DEFAULT_SIZE;
        private int initial = DEFAULT_VALUE;
        private Orientation orientation = DEFAULT_ORIENTATION;
        private VaadinIcon icon = DEFAULT_ICON;
        private VaadinIcon selectedIcon = DEFAULT_ICON_SELECTED;
        private String color = DEFAULT_COLOR;
        private String selectedColor = DEFAULT_SELECTED_COLOR;

        private Builder() {
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder initial(int initial) {
            this.initial = initial;
            return this;
        }

        public Builder orientation(Orientation orientation) {
            this.orientation = orientation;
            return this;
        }

        public Builder icon(VaadinIcon icon) {
            this.icon = icon;
            return this;
        }

        public Builder selectedIcon(VaadinIcon selectedIcon) {
            this.selectedIcon = selectedIcon;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder selectedColor(String selectedColor) {
            this.selectedColor = selectedColor;
            return this;
        }

        public StarsRating build() {
            return new StarsRating(size, initial, orientation, icon, selectedIcon, color, selectedColor);
        }
    }
}
