package net.amahdy.vaadin.demo.grid.util;

import com.vaadin.annotations.HtmlImport;
import org.vaadin.elements.AbstractElementComponent;
import org.vaadin.elements.ElementIntegration;
import org.vaadin.elements.Root;

@HtmlImport(
    "vaadin://bower_components/vaadin-grid/vaadin-grid.html")
public class GridComponent extends AbstractElementComponent {

    private GridElement element;

    public GridComponent(String itemsJson) {
        element = GridElement.create();
        element.setItems(itemsJson);
        element.appendHtml("<vaadin-grid-column>\n" +
                "        <template class=\"header\">Name</template>\n" +
                "        <template>\n" +
                "          <div>[[item.name]]</div>\n" +
                "        </template>\n" +
                "      </vaadin-grid-column>");

        Root root = ElementIntegration.getRoot(this);
        root.appendChild(element);
    }
}