package io.github.sam42r.reindeer.view;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.github.sam42r.reindeer.StarsRating;
import io.github.sam42r.reindeer.StarsRating.Color;
import io.github.sam42r.reindeer.StarsRating.None;
import io.github.sam42r.reindeer.StarsRating.Shade;
import io.github.sam42r.reindeer.color.ColorNames;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Route
public class MainView extends Main {

    public MainView() {
        var horizontalLayout = new HorizontalLayout(
                VaadinIcon.VAADIN_H.create(),
                new H1("vaadin show-case")
        );
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        add(horizontalLayout);

        var starsRatingDefault = StarsRating.builder().initial(1).build();
        var starsRatingDefaultDisabledNone = StarsRating.builder().initial(2).disabledStyle(new None()).build();
        var starsRatingDefaultDisabledShade = StarsRating.builder().initial(3).disabledStyle(new Shade(-0.5)).build();
        var starsRatingDefaultDisabledColor = StarsRating.builder().initial(4).disabledStyle(new Color(ColorNames.LightGray.name())).build();

        starsRatingDefault.addChangeListener(event ->
                log.debug("Stars-Rating (default): {}", event.getSource().getValue()));

        var enabled = new Checkbox("enabled", true);
        enabled.addValueChangeListener(e -> {
            starsRatingDefault.setEnabled(e.getValue());
            starsRatingDefaultDisabledNone.setEnabled(e.getValue());
            starsRatingDefaultDisabledShade.setEnabled(e.getValue());
            starsRatingDefaultDisabledColor.setEnabled(e.getValue());
        });

        add(new H2("stars-rating default"),
                new HorizontalLayout(
                        new VerticalLayout(enabled, new Span("Click to enable/disable")),
                        new VerticalLayout(starsRatingDefault, new Span("Opacity")),
                        new VerticalLayout(starsRatingDefaultDisabledNone, new Span("None")),
                        new VerticalLayout(starsRatingDefaultDisabledShade, new Span("Shade")),
                        new VerticalLayout(starsRatingDefaultDisabledColor, new Span("Color"))
                )
        );

        add(new H2("stars-rating vertical"),
                StarsRating.builder()
                        .orientation(StarsRating.Orientation.VERTICAL)
                        .build()
        );

        add(new H2("stars-rating custom"),
                StarsRating.builder()
                        .size(3)
                        .initial(1)
                        .icon(VaadinIcon.HEART_O::create)
                        .selectedIcon(VaadinIcon.HEART::create)
                        .color(ColorNames.Tomato.name())
                        .selectedColor(ColorNames.Red.name())
                        .build(),
                StarsRating.builder()
                        .size(4)
                        .initial(2)
                        .icon(VaadinIcon.CIRCLE_THIN::create)
                        .selectedIcon(VaadinIcon.DOT_CIRCLE::create)
                        .color(ColorNames.LightBlue.name())
                        .selectedColor(ColorNames.Blue.name())
                        .build(),
                StarsRating.builder()
                        .size(5)
                        .initial(3)
                        .icon(VaadinIcon.BULLSEYE::create)
                        .selectedIcon(VaadinIcon.CROSSHAIRS::create)
                        .color(ColorNames.DarkSeaGreen.name())
                        .selectedColor(ColorNames.DarkGreen.name())
                        .build(),
                StarsRating.builder()
                        .size(6)
                        .initial(4)
                        .icon(VaadinIcon.CIRCLE::create)
                        .selectedIcon(VaadinIcon.CIRCLE::create)
                        .color(ColorNames.LightGoldenrodYellow.name())
                        .selectedColor(ColorNames.Gold.name())
                        .enabled(false)
                        .build(),
                StarsRating.builder()
                        .size(6)
                        .initial(4)
                        .icon(VaadinIcon.DIAMOND_O::create)
                        .selectedIcon(VaadinIcon.DIAMOND::create)
                        .color(ColorNames.LightSteelBlue.name())
                        .selectedColor(ColorNames.RoyalBlue.name())
                        .build(),
                StarsRating.builder()
                        .size(5)
                        .initial(3)
                        .icon(VaadinIcon.THIN_SQUARE::create)
                        .selectedIcon(VaadinIcon.CHECK_SQUARE_O::create)
                        .color(ColorNames.LightGray.name())
                        .selectedColor(ColorNames.DarkGray.name())
                        .build(),
                StarsRating.builder()
                        .size(4)
                        .initial(2)
                        .icon(FontAwesome.Brands.LINUX::create)
                        .selectedIcon(FontAwesome.Brands.LINUX::create)
                        .color(ColorNames.LightGray.name())
                        .selectedColor(ColorNames.Black.name())
                        .build(),
                StarsRating.builder()
                        .size(3)
                        .initial(1)
                        .icon(FontAwesome.Solid.CROWN::create)
                        .selectedIcon(FontAwesome.Solid.CROWN::create)
                        .color(ColorNames.LightGray.name())
                        .selectedColor(ColorNames.Gold.name())
                        .build()
        );
    }
}
