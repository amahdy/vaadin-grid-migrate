package net.amahdy.vaadin.demo.grid.util;

import org.vaadin.elements.Element;
import org.vaadin.elements.Elements;
import org.vaadin.elements.Tag;

@Tag("vaadin-grid")
public interface GridElement extends Element {

    static GridElement create() {
        return Elements.create(GridElement.class);
    }

    void setItems(String items);
}