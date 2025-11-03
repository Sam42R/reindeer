package io.github.sam42r.reindeer;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

    @Test
    @SuppressWarnings("unchecked")
    void shouldFireEvent() {
        var starsRating = StarsRating.builder().initial(1).build();

        var stars = starsRating.getContent().getChildren()
                .flatMap(Component::getChildren)
                .toList();

        var componentEventListener = mock(ComponentEventListener.class);
        starsRating.addChangeListener(componentEventListener);

        starsRating.handleClickEvent(new ClickEvent<>(stars.get(stars.size() - 1)));

        verify(componentEventListener, times(1)).onComponentEvent(any());
        assertThat(starsRating.getValue()).isEqualTo(5);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldFireNoEvent() {
        var starsRating = StarsRating.builder().initial(1).enabled(false).build();

        var stars = starsRating.getContent().getChildren()
                .flatMap(Component::getChildren)
                .toList();

        var componentEventListener = mock(ComponentEventListener.class);
        starsRating.addChangeListener(componentEventListener);

        starsRating.handleClickEvent(new ClickEvent<>(stars.get(stars.size() - 1)));

        verify(componentEventListener, times(0)).onComponentEvent(any());
        assertThat(starsRating.getValue()).isEqualTo(1);
    }
}
