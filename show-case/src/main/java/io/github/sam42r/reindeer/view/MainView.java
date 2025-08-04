package io.github.sam42r.reindeer.view;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
import io.github.sam42r.reindeer.MissionPatchMaker;
import io.github.sam42r.reindeer.StarsRating;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Route
public class MainView extends Main {

    public MainView() {
        setSizeFull();

        var tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("stars-rating", starsRating());
        tabSheet.add("mission-patch-maker", missionPatchMaker());
        tabSheet.setSelectedIndex(1);

        add(tabSheet);
    }

    private VerticalLayout starsRating() {
        var verticalLayout = new VerticalLayout();

        var starsRatingDefault = StarsRating.builder().build();
        verticalLayout.add(new H2("stars-rating default"), starsRatingDefault);
        starsRatingDefault.addChangeListener(event ->
                log.debug("Stars-Rating (default): {}", event.getSource().getValue()));

        var starsRatingVertical = StarsRating.builder().orientation(StarsRating.Orientation.VERTICAL).build();
        verticalLayout.add(new H2("stars-rating vertical"), starsRatingVertical);
        var registration = starsRatingVertical.addChangeListener(event ->
                log.debug("Stars-Rating (vertical): {}", event.getSource().getValue()));
        registration.remove();

        verticalLayout.add(new H2("stars-rating custom"),
                StarsRating.builder()
                        .size(3)
                        .initial(1)
                        .icon(VaadinIcon.HEART_O::create)
                        .selectedIcon(VaadinIcon.HEART::create)
                        .color("tomato")
                        .selectedColor("red")
                        .build(),
                StarsRating.builder()
                        .size(4)
                        .initial(2)
                        .icon(VaadinIcon.CIRCLE_THIN::create)
                        .selectedIcon(VaadinIcon.DOT_CIRCLE::create)
                        .color("lightblue")
                        .selectedColor("blue")
                        .build(),
                StarsRating.builder()
                        .size(5)
                        .initial(3)
                        .icon(VaadinIcon.BULLSEYE::create)
                        .selectedIcon(VaadinIcon.CROSSHAIRS::create)
                        .color("darkseagreen")
                        .selectedColor("darkgreen")
                        .build(),
                StarsRating.builder()
                        .size(6)
                        .initial(4)
                        .icon(VaadinIcon.CIRCLE::create)
                        .selectedIcon(VaadinIcon.CIRCLE::create)
                        .color("lightgoldenrodyellow")
                        .selectedColor("gold")
                        .build(),
                StarsRating.builder()
                        .size(6)
                        .initial(4)
                        .icon(VaadinIcon.DIAMOND_O::create)
                        .selectedIcon(VaadinIcon.DIAMOND::create)
                        .color("lightsteelblue")
                        .selectedColor("royalblue")
                        .build(),
                StarsRating.builder()
                        .size(5)
                        .initial(3)
                        .icon(VaadinIcon.THIN_SQUARE::create)
                        .selectedIcon(VaadinIcon.CHECK_SQUARE_O::create)
                        .color("lightgrey")
                        .selectedColor("darkgrey")
                        .build(),
                StarsRating.builder()
                        .size(4)
                        .initial(2)
                        .icon(FontAwesome.Brands.LINUX::create)
                        .selectedIcon(FontAwesome.Brands.LINUX::create)
                        .color("lightgrey")
                        .selectedColor("default")
                        .build(),
                StarsRating.builder()
                        .size(3)
                        .initial(1)
                        .icon(FontAwesome.Solid.CROWN::create)
                        .selectedIcon(FontAwesome.Solid.CROWN::create)
                        .color("lightgrey")
                        .selectedColor("gold")
                        .build()
        );

        return verticalLayout;
    }

    private VerticalLayout missionPatchMaker() {
        var verticalLayout = new VerticalLayout(new MissionPatchMaker());
        verticalLayout.setSizeFull();
        verticalLayout.setPadding(false);
        return verticalLayout;
    }
}
