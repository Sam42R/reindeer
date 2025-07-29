package io.github.sam42r.reindeer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StarsRatingTest {

    @Test
    void shouldCreateDefault() {
        var starsRating = StarsRating.builder().build();
        assertThat(starsRating.getContent().getChildren())
                .anySatisfy(component -> assertThat(component).isInstanceOf(HorizontalLayout.class));

        var stars = starsRating.getContent().getChildren()
                .flatMap(Component::getChildren)
                .toList();
        assertThat(stars).hasSize(5);
    }

    @Test
    void shouldCreateCustom() {
        var starsRating = StarsRating.builder()
                .size(3)
                .orientation(StarsRating.Orientation.VERTICAL)
                .build();
        assertThat(starsRating.getContent().getChildren())
                .anySatisfy(component -> assertThat(component).isInstanceOf(VerticalLayout.class));

        var stars = starsRating.getContent().getChildren()
                .flatMap(Component::getChildren)
                .toList();
        assertThat(stars).hasSize(3);
    }
}
