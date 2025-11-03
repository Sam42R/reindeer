package io.github.sam42r.reindeer.color;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ColorNamesTest {

    @Test
    void shouldGetRgb() {
        var color = ColorNames.Crimson;
        assertThat(color.getRgb()).isEqualTo("rgb(220,20,60)");
    }

    @Test
    void shouldGetRgba() {
        var color = ColorNames.Crimson;
        assertThat(color.getRgba(0.5)).isEqualTo("rgb(220,20,60,0.50)");
    }

    @Test
    void shouldGetBrighterShade() {
        var color = ColorNames.Crimson;
        assertThat(color.getShade(0.2)).isEqualTo("rgb(227,67,99)");
    }

    @Test
    void shouldGetDarkerShade() {
        var color = ColorNames.Crimson;
        assertThat(color.getShade(-0.2)).isEqualTo("rgb(176,16,48)");
    }
}
