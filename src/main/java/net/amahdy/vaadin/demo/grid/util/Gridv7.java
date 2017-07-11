package net.amahdy.vaadin.demo.grid.util;

import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.ui.Grid;

/**
 * Created by amahdy on 7/11/17.
 */
public class Gridv7 extends Grid {

    public Gridv7(String caption) {
        super(caption);
    }

    public Gridv7(String caption, IndexedContainer container) {
        super(caption, container);
    }
}
