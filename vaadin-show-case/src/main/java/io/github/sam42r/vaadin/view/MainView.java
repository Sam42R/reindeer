package io.github.sam42r.vaadin.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import io.github.sam42r.vaadin.StarsRating;

@Route
public class MainView extends Main {

    public MainView() {
        var horizontalLayout = new HorizontalLayout(
                VaadinIcon.VAADIN_H.create(),
                new H1("vaadin show-case")
        );
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        add(horizontalLayout);

        var starsRatingDefault = new StarsRating();
        add(new H2("stars-rating default"), starsRatingDefault);
        starsRatingDefault.addChangeListener(event ->
                System.out.printf("Stars-Rating (default): %d%n", event.getSource().getValue()));

        var starsRatingVertical = StarsRating.builder().orientation(StarsRating.Orientation.VERTICAL).build();
        add(new H2("stars-rating vertical"), starsRatingVertical);
        var registration = starsRatingVertical.addChangeListener(event ->
                System.out.printf("Stars-Rating (vertical): %d%n", event.getSource().getValue()));
        registration.remove();

        add(new H2("stars-rating custom"),
                StarsRating.builder()
                        .size(3)
                        .initial(1)
                        .icon(VaadinIcon.HEART_O)
                        .selectedIcon(VaadinIcon.HEART)
                        .color("tomato")
                        .selectedColor("red")
                        .build(),
                StarsRating.builder()
                        .size(4)
                        .initial(2)
                        .icon(VaadinIcon.CIRCLE_THIN)
                        .selectedIcon(VaadinIcon.DOT_CIRCLE)
                        .color("lightblue")
                        .selectedColor("blue")
                        .build(),
                StarsRating.builder()
                        .size(5)
                        .initial(3)
                        .icon(VaadinIcon.BULLSEYE)
                        .selectedIcon(VaadinIcon.CROSSHAIRS)
                        .color("darkseagreen")
                        .selectedColor("darkgreen")
                        .build(),
                StarsRating.builder()
                        .size(6)
                        .initial(4)
                        .icon(VaadinIcon.CIRCLE)
                        .selectedIcon(VaadinIcon.CIRCLE)
                        .color("lightgoldenrodyellow")
                        .selectedColor("gold")
                        .build(),
                StarsRating.builder()
                        .size(5)
                        .initial(3)
                        .icon(VaadinIcon.DIAMOND_O)
                        .selectedIcon(VaadinIcon.DIAMOND)
                        .color("lightsteelblue")
                        .selectedColor("royalblue")
                        .build(),
                StarsRating.builder()
                        .size(4)
                        .initial(2)
                        .icon(VaadinIcon.THUMBS_UP_O)
                        .selectedIcon(VaadinIcon.THUMBS_UP)
                        .color("default")
                        .selectedColor("default")
                        .build(),
                StarsRating.builder()
                        .size(3)
                        .initial(1)
                        .icon(VaadinIcon.THIN_SQUARE)
                        .selectedIcon(VaadinIcon.CHECK_SQUARE_O)
                        .color("lightgrey")
                        .selectedColor("darkgrey")
                        .build()
        );
    }
}
