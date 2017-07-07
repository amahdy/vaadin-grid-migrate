package net.amahdy.vaadin.demo.grid;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
        Label defaultLbl = new Label(
                "<h1 style='color:blue'>"
                        + VaadinIcons.ARROW_LONG_LEFT.getHtml()
                        + " Select an item to see the demo!</h1>",
                ContentMode.HTML);

        Grid<Method> examples = new Grid<>();
        examples.setHeight("100%");
        examples.setWidth("350px");
        examples.setItems(Arrays.stream(example.getClass().getDeclaredMethods()).filter(method ->
                Modifier.isPublic(method.getModifiers())).sorted(Comparator.comparing(Method::getName)));
        examples.addColumn(method -> (
                method.getName().substring(0, 1).toUpperCase() + method.getName().substring(1))
                .replaceAll(
                        String.format("%s|%s|%s",
                            "(?<=[A-Z])(?=[A-Z][a-z])",
                            "(?<=[^A-Z])(?=[A-Z])",
                            "(?<=[A-Za-z])(?=[^A-Za-z])")
                        , " "));
        examples.removeHeaderRow(0);
        examples.asSingleSelect().addValueChangeListener(evt -> {
            exampleArea.removeAllComponents();

            if(evt.getValue() != null) {
                try {
                    Method method = example.getClass().getDeclaredMethod(evt.getValue().getName());

                    try {
                        exampleArea.addComponent((Component) method.invoke(example));
                    } catch (IllegalAccessException|InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchMethodException e) {
                    exampleArea.addComponent(defaultLbl);
                }
            }else {
                exampleArea.addComponent(defaultLbl);
            }
        });

        exampleArea.setSizeFull();
        exampleArea.addComponent(defaultLbl);

        exampleHost.setSizeFull();
        exampleHost.addComponent(examples);
        exampleHost.addComponentsAndExpand(exampleArea);

        setContent(exampleHost);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DemoUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
