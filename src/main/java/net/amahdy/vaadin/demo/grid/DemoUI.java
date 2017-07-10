package net.amahdy.vaadin.demo.grid;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import javax.servlet.annotation.WebServlet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

@Theme("mytheme")
public class DemoUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        TableExamples example = new TableExamples();
        HorizontalLayout exampleHost = new HorizontalLayout();
        VerticalLayout exampleArea = new VerticalLayout();
        Panel exampleAreaHost = new Panel();
        Label defaultLbl = new Label(
                "<h1 style='color:blue'>"
                        + VaadinIcons.ARROW_LONG_LEFT.getHtml()
                        + " Select an item to see the demo!</h1>",
                ContentMode.HTML);

        Grid<Method> examples = new Grid<>();
        examples.setHeight("100%");
        examples.setWidth("350px");
        examples.setItems(
                Arrays.stream(example.getClass().getDeclaredMethods())
                        .filter(method -> Modifier.isPublic(method.getModifiers()))
                        .sorted(Comparator.comparing(Method::getName))
        );
        examples.addColumn(method -> (
                method.getName().substring(5, 6).toUpperCase() + method.getName().substring(6))
                .replaceAll(String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ), " ")
        );
        examples.removeHeaderRow(0);
        examples.asSingleSelect().addValueChangeListener(evt -> {
            exampleArea.removeAllComponents();

            if (evt.getValue() != null) {
                try {
                    Method method = example.getClass().getDeclaredMethod(evt.getValue().getName());

                    try {
                        Component component = (Component) method.invoke(example);
                        if (component instanceof AbstractOrderedLayout) {
                            // Make the demo looks consistent
                            ((AbstractOrderedLayout) component).setMargin(false);
                        }
                        exampleArea.addComponent(component);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchMethodException e) {
                    exampleArea.addComponent(defaultLbl);
                }
            } else {
                exampleArea.addComponent(defaultLbl);
            }
        });

        exampleArea.addComponent(defaultLbl);

        exampleAreaHost.setSizeFull();
        exampleAreaHost.setContent(exampleArea);

        exampleHost.setSizeFull();
        exampleHost.setMargin(true);
        exampleHost.addComponent(examples);
        exampleHost.addComponentsAndExpand(exampleAreaHost);

        setContent(exampleHost);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DemoUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
