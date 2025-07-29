# Stars-Rating

## create component

_Stars-Rating_ component is created using a builder:
```java
    // using defaults
    var starsRating = StarsRating.builder().build();
    // custom
    var starsRatingCustom = StarsRating.builder()
        .size(3)
        .initial(1)
        .icon(VaadinIcon.HEART_O::create)
        .selectedIcon(VaadinIcon.HEART::create)
        .color("tomato")
        .selectedColor("red")
        .build();
```

## handle change events

_Stars-Rating_ component publishes change events which can be consumed through adding a listener:
```java
    starsRating.addChangeListener(event ->
        log.info("Stars-Rating: {}", event.getSource().getValue()));
```

## using third-party icons

_Stars-Rating_ is using vaadin [icons](https://vaadin.com/docs/latest/components/icons) for its rendering. You may use
third-party icons (e.g. [font-awesome](https://vaadin.com/directory/component/fontawesome-iron-iconset)) as well:
```java
    StarsRating.builder()
      // [...]
      .icon(FontAwesome.Solid.CROWN::create)
      .selectedIcon(FontAwesome.Solid.CROWN::create)
      // [...]
      .build()
```
