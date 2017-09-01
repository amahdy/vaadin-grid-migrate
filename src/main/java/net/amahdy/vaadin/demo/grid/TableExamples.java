package net.amahdy.vaadin.demo.grid;

import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.event.FieldEvents.TextChangeEvent;
import com.vaadin.v7.event.FieldEvents.TextChangeListener;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Form;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import net.amahdy.vaadin.demo.grid.data.Bean;
import net.amahdy.vaadin.demo.grid.data.Bodies;
import net.amahdy.vaadin.demo.grid.data.ComponentBean;
import net.amahdy.vaadin.demo.grid.data.ItemPropertyId;
import net.amahdy.vaadin.demo.grid.data.Planet;
import net.amahdy.vaadin.demo.grid.data.Scientist;
import net.amahdy.vaadin.demo.grid.util.Helper;
import net.amahdy.vaadin.demo.grid.util.KbdHandlerFooter;
import net.amahdy.vaadin.demo.grid.util.KbdHandlerSpreadsheet;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("deprecation")
public class TableExamples extends CustomComponent {

    public Component _000_basic() {
        Object[][] data = {
                {"Sirius", -1.46f},
                {"Canopus", -0.72f},
                {"Arcturus", -0.04f},
                {"Alpha Centauri", -0.01f}
        };

        Grid<Object[]> table = new Grid<>("The Brightest Stars");
        table.setItems(data);

        // Define two columns for the built-in container
        table.addColumn(v -> v[0]).setCaption("Name");
        table.addColumn(v -> v[1]).setCaption("Mag");

        // Show exactly the currently contained rows (items)
        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(data.length);

        // Default in Table
        table.setSelectionMode(Grid.SelectionMode.NONE);

        return table;
    }

    public Component _001_singleSelect() {
        VerticalLayout layout = new VerticalLayout();

        // Table with a component column in non-editable mode
        Grid<Scientist> table = new Grid<>(Scientist.class);
        table.setCaption("Important People");
        table.setItems(Helper.generateScientists());
        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(10);

        // Optional line, this is the default:
        table.setSelectionMode(Grid.SelectionMode.SINGLE);

        // Handle selection changes
        table.addSelectionListener(event -> {
            if (table.asSingleSelect().getValue() != null)
                layout.addComponent(new Label("Selected item " +
                        table.asSingleSelect().getValue()));
            else // Item deselected
                layout.addComponent(new Label("Nothing selected"));
        });
        layout.addComponent(table);

        return layout;
    }

    public Component _002_multiSelect() {
        VerticalLayout layout = new VerticalLayout();

        // Table with a component column in non-editable mode
        Grid<Scientist> table = new Grid<>(Scientist.class);
        table.setCaption("Important People");
        table.setItems(Helper.generateScientists());
        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(10);

        table.setSelectionMode(Grid.SelectionMode.MULTI);

        // Handle selection changes
        table.addSelectionListener(event ->
                layout.addComponent(new Label("Selected items " +
                        table.asMultiSelect().getSelectedItems())));
        layout.addComponent(table);

        return layout;
    }

    public Component _003_components() {
        // Table with a component column in non-editable mode
        final Grid<String[]> table = new Grid<>("My Table");
        table.setWidth("100%");
        table.setRowHeight(70);

        // Insert this data
        String people[][] = {
                {"Galileo", "Liked to go around the Sun"},
                {"Monnier", "Liked star charts"},
                {"Väisälä", "Liked optics"},
                {"Oterma", "Liked comets"},
                {"Valtaoja", "Likes cosmology and still " +
                        "lives unlike the others above"},
        };

        table.setItems(people);
        table.addColumn(p -> p[0]).setCaption("Name");
        table.addComponentColumn(p -> {
            TextArea area = new TextArea(null, p[1]);
            area.setRows(2);
            return area;
        }).setCaption("Description");
        table.addComponentColumn(p -> new com.vaadin.ui.CheckBox()).setCaption("Delete");

        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(people.length);
        return table;
    }

    public Component _004_nestedTables() {
        // Table with a component column in non-editable mode
        Grid<String[]> table = new Grid<>("My Nested Table");
        table.setWidth("250px");
        table.setRowHeight(200);

        // Insert this data
        String planets[][] = {{"Mercury"},
                {"Venus"},
                {"Earth", "The Moon"},
                {"Mars", "Phobos", "Deimos"},
                {"Jupiter", "Io", "Europa", "Ganymedes", "Callisto"},
                {"Saturn", "Titan", "Tethys", "Dione", "Rhea", "Iapetus"},
                {"Uranus", "Miranda", "Ariel", "Umbriel", "Titania", "Oberon"},
                {"Neptune", "Triton", "Proteus", "Nereid", "Larissa"}};

        table.setItems(planets);
        table.addColumn(p -> p[0]).setCaption("Name");
        table.addComponentColumn(p -> {
            Grid<String> moonTable = new Grid<>();
            moonTable.removeHeaderRow(0);
            moonTable.setWidth("120px");

            String[] moons = new String[p.length - 1];
            System.arraycopy(p, 0, moons, 0, moons.length);

            moonTable.setItems(moons);
            moonTable.addColumn(String::toString);

            // Should be fixed height or there will be trouble
            moonTable.setHeightMode(HeightMode.ROW);
            moonTable.setHeightByRows(4);

            return moonTable;
        }).setCaption("Moons").setWidth(170);

        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(planets.length);
        return table;
    }

    public Component _005_interactingComponents() {
        // Table with a component column in non-editable mode
        final Grid<String[]> table = new Grid<>("My Table");
        table.setWidth("100%");
        table.setRowHeight(70);

        // Insert this data
        String people[][] = {
                {"Galileo", "Liked to go around the Sun", "0"},
                {"Monnier", "Liked star charts", "0"},
                {"Väisälä", "Liked optics", "0"},
                {"Oterma", "Liked comets", "0"},
                {"Valtaoja", "Likes cosmology and still " +
                        "lives unlike the others above", "0"},
        };

        table.setItems(people);
        table.addColumn(p -> p[0]).setCaption("Name");
        table.addComponentColumn(p -> {
            TextArea area = new TextArea(null, p[1]);
            area.setRows(2);
            if(p[2].equals("0")) {
                area.setEnabled(true);
            }else {
                area.setEnabled(false);
            }
            return area;
        }).setCaption("Description");
        table.addComponentColumn(p -> {
            final com.vaadin.ui.CheckBox checkbox = new com.vaadin.ui.CheckBox();
            if(p[2].equals("0")) {
                checkbox.setValue(false);
            }else {
                checkbox.setValue(true);
            }

            checkbox.addValueChangeListener(evt -> {
                boolean value = checkbox.getValue();
                if(value) {
                    p[2] = "1";
                }else {
                    p[2] = "0";
                }
                table.getDataProvider().refreshItem(p);
            });

            return checkbox;
        }).setCaption("Delete");

        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(people.length);
        return table;
    }

    public Component _006_beanComponents() {
        final Grid<ComponentBean> table = new Grid<>("My Table");

        List<ComponentBean> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            items.add(new ComponentBean("Hello", true));
            items.add(new ComponentBean("There", false));
        }
        table.setItems(items);
        table.addComponentColumn(ComponentBean::getTextfield).setCaption("Hello");
        table.addComponentColumn(ComponentBean::getCheckbox).setCaption("There");

        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(6);

        return table;
    }

    public Component _007_varyingRows() {
        VerticalLayout layout = new VerticalLayout();

        final Table table = new Table();
        table.addContainerProperty("column1", Component.class, null);

        for (int i = 0; i < 10; i++) {
            int height = 10 * i + 10;
            Label label = new Label("Height " + height);
            label.setHeight(height, Unit.PIXELS);
            table.addItem(new Object[]{label}, i);
        }

        // No scrollbar
        table.setPageLength(table.size());

        // TEST: Change the height of a row
        Button button = new Button("Change the heights of a row");
        button.addClickListener(new ClickListener() {

            Table tableRef = table;

            @Override
            public void buttonClick(ClickEvent event) {
                final Table newTable = new Table();
                newTable.addContainerProperty("column1", Component.class, null);

                for (int i = 0; i < 10; i++) {
                    int height = 10 * i + 10;
                    Label label = new Label("Height " + height);
                    label.setHeight(height, Unit.PIXELS);
                    newTable.addItem(new Object[]{label}, i);
                }

                Label label = new Label("Foo");
                label.setHeight((float) Math.random() * 200, Unit.PIXELS);
                newTable.getItem(5).getItemProperty("column1").setValue(label);
                newTable.setPageLength(newTable.size());

                layout.replaceComponent(tableRef, newTable);
                tableRef = newTable;
            }
        });

        layout.addComponent(table);
        layout.addComponent(button);

        return layout;
    }

    public Component _008_varyingHeightLabels() {
        final Table table = new Table();

        table.addContainerProperty("column1", Component.class, null);

        String labels[] = {
                "This is a label",
                "This is a longer label",
                "This is yet a bit longer label",
                "This is significantly longer label " +
                        "that will most certainly span over " +
                        "several rows."};
        for (int i = 0; i < 100 * labels.length; i++) {
            Label label = new Label(i + ": " + labels[i % labels.length]);
            label.setWidth("100px");
            table.addItem(new Object[]{label}, i);
        }

        table.setPageLength(10);

        return table;
    }

    public Component _009_reverseByIndex() {
        List<Integer> items =
                IntStream.rangeClosed(0, 4).boxed().collect(Collectors.toList());

        Grid<Integer> table = new Grid<>("Normal order");
        table.setWidth("100px");
        table.setItems(items);
        table.addColumn(Integer::intValue).setCaption("Index").setId("index");
        table.sort("index", SortDirection.ASCENDING);

        Grid<Integer> reverse = new Grid<>("Reverse order");
        reverse.setWidth("100px");
        reverse.setItems(items);
        reverse.addColumn(Integer::intValue).setCaption("Index").setId("index");
        reverse.sort("index", SortDirection.DESCENDING);

        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(items.size());
        reverse.setHeightMode(HeightMode.ROW);
        reverse.setHeightByRows(items.size());

        return new HorizontalLayout(table, reverse);
    }

    public Component _010_headers() {
        Grid<Object[]> table = new Grid<>("Custom Table Headers");

        // Insert some data
        Object people[][] = {{"Galileo", 1564},
                {"Väisälä", 1891},
                {"Valtaoja", 1951}};
        table.setItems(people);

        // Set nicer header names
        table.addColumn(o -> o[0]).setCaption("Name");
        table.addColumn(o -> o[1]).setCaption("Born In");

        // Adjust the table height a bit
        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(people.length);

        return table;
    }

    public Component _011_fakeHeaders() {
        VerticalLayout layout = new VerticalLayout();

        final Table table = new Table();
        table.addContainerProperty("lastname", String.class, null);
        table.addContainerProperty("country", String.class, null);
        table.addContainerProperty("born", Integer.class, null);

        // Insert some data
        Object people[][] = {{"Galileo", "Italian", 1564},
                {"Väisälä", "Finnish", 1891},
                {"Valtaoja", "Finnish", 1951}};
        for (int i = 0; i < people.length; i++)
            table.addItem(people[i], i);

        // Hide the normal table header
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);

        // Use fixed widths for the columns
        final int lastnameWidth = 100;
        final int countryWidth = 100;
        final int bornWidth = 40;
        table.setColumnWidth("lastname", lastnameWidth);
        table.setColumnWidth("country", countryWidth);
        table.setColumnWidth("born", bornWidth);

        // Create country filter
        ComboBox countryFilterBox = new ComboBox();
        countryFilterBox.setWidth(countryWidth + 2 * 6, Unit.PIXELS);
        countryFilterBox.addItem("No filter");
        countryFilterBox.addItem("Italian");
        countryFilterBox.addItem("Finnish");
        countryFilterBox.setNullSelectionItemId("No filter");
        countryFilterBox.setInputPrompt("No filter");
        countryFilterBox.setImmediate(true);
        countryFilterBox.addValueChangeListener(evt -> {

                // The filter API is not accessible in Table
                IndexedContainer container = (IndexedContainer) table.getContainerDataSource();

                String filter = (String) evt.getProperty().getValue();
                if (filter == null) {
                    container.removeContainerFilters("country");
                    return;
                }

                // Set the filter
                container.removeContainerFilters("country");
                container.addContainerFilter("country", filter, true, true);
        });

        // Custom header
        GridLayout tableHeader = new GridLayout(3, 2);

        Label nameLabel = new Label("Name");
        nameLabel.setWidth(lastnameWidth + 2 * 6, Unit.PIXELS);
        tableHeader.addComponent(nameLabel, 0, 0, 0, 1);
        Label countryLabel = new Label("Country");
        countryLabel.setWidth(countryWidth + 2 * 6, Unit.PIXELS);
        tableHeader.addComponent(new Label("Country"), 1, 0);
        Label bornLabel = new Label("Born");
        bornLabel.setWidth(bornWidth + 2 * 6, Unit.PIXELS);
        tableHeader.addComponent(bornLabel, 2, 0, 2, 1);
        tableHeader.addComponent(countryFilterBox, 1, 1);

        // Put the header and table inside a vertical layout
        layout.addComponent(tableHeader);
        layout.addComponent(table);

        // Adjust the table height a bit
        table.setPageLength(table.size());

        return layout;
    }

    public Component _012_htmlHeaders() {
        final Table table = new Table();
        table.addContainerProperty("lastname", String.class, null);
        table.addContainerProperty("country", String.class, null);
        table.addContainerProperty("born", Integer.class, null);

        // Normal column header
        table.setColumnHeader("lastname", "Last Name");

        // This column header is two rows high with the upper row
        // bleeding on the right
        table.setColumnHeader("country", "<div width='300px'>Details</div><br/>Country");

        // This is in the lower row
        table.setColumnHeader("born", "<br/>Born");

        // CSS modifications are necessary to make the header higher

        // Insert some data
        Object people[][] = {{"Galileo", "Italian", 1564},
                {"Väisälä", "Finnish", 1891},
                {"Valtaoja", "Finnish", 1951}};
        for (int i = 0; i < people.length; i++)
            table.addItem(people[i], i);

        // The sort indicators are not rendered properly - could
        // perhaps fix the problem
        table.setSortEnabled(false);

        // Adjust the table height a bit
        table.setPageLength(table.size());

        return table;
    }

    public Component _013_footerBasic() {
        // Have a table with a numeric column
        Table table = new Table("Custom Table Footer");
        table.addContainerProperty("Name", String.class, null);
        table.addContainerProperty("Died At Age", Integer.class, null);

        // Insert some data
        Object people[][] = {{"Galileo", 77},
                {"Monnier", 83},
                {"Väisälä", 79},
                {"Oterma", 86}};
        for (int i = 0; i < people.length; i++)
            table.addItem(people[i], i);

        // Calculate the average of the numeric column
        double avgAge = 0;
        for (Object[] person: people)
            avgAge += (Integer) person[1];
        avgAge /= people.length;

        // Set the footers
        table.setFooterVisible(true);
        table.setColumnFooter("Name", "Average");
        table.setColumnFooter("Died At Age", String.valueOf(avgAge));

        // Adjust the table height a bit
        table.setPageLength(table.size());

        return table;

    }

    public Component _014_footerSum() {
        VerticalLayout layout = new VerticalLayout();

        // Have a table with a numeric column
        final Table table = new Table("Roster of Fear");
        table.addContainerProperty("Source of Fear", String.class, null);
        table.addContainerProperty("Fear Factor", Double.class, null);

        // Put the table in edit mode to allow editing the fear factors
        table.setEditable(true);

        // Insert some data
        final Object fears[][] = {{"Psycho", 8.7},
                {"Alien", 8.5},
                {"The Shining", 8.5},
                {"The Thing", 8.2}};
        for (int i = 0; i < fears.length; i++)
            table.addItem(fears[i], i);

        // Set the footers
        table.setFooterVisible(true);
        table.setColumnFooter("Source of Fear",
                "The Sum of All Fears");

        // Calculate the sum every time any of the values change
        final Property.ValueChangeListener listener =
                evt -> {
                    // Calculate the sum of the numeric column
                    double sum = table.getItemIds().stream().mapToDouble(i ->
                            (Double) table.getItem(i).getItemProperty("Fear Factor").getValue()).sum();
                    sum = Math.round(sum * 100) / 100.0; // Round to two decimals

                    // Set the new sum in the footer
                    table.setColumnFooter("Fear Factor", String.valueOf(sum));
                };

        // Can't access the editable components from the table so
        // must store the information
        final HashMap<Integer, TextField> valueFields = new HashMap<>();

        // Set the same listener of all textfields, and set them as immediate
        table.setTableFieldFactory((container, itemId, propertyId, uiContext) -> {
            TextField field = new TextField((String) propertyId);

            // User can only edit the numeric column
            if ("Source of Fear".equals(propertyId))
                field.setReadOnly(true);
            else { // The numeric column
                field.setImmediate(true);
                field.addValueChangeListener(listener);
                field.setColumns(7);

                // The field needs to know the item it is in
                field.setData(itemId);

                // Remember the field
                valueFields.put((Integer) itemId, field);

                // Focus the first editable value
                if (((Integer) itemId) == 0)
                    field.focus();
            }
            return field;
        });

        // Panel that handles keyboard navigation
        Panel navigator = new Panel();
        navigator.addStyleName(ValoTheme.PANEL_WELL);
        navigator.setContent(table);
        navigator.addActionHandler(new KbdHandlerFooter(table, valueFields));

        // Adjust the table height a bit
        table.setPageLength(table.size());

        layout.addComponent(navigator);

        return layout;
    }

    public Component _015_headerClick() {
        Table table = Helper.createSmallTable("Custom Table Headers");

        // table.setColumnReorderingAllowed(true);
        table.setImmediate(true);

        // Handle the header clicks
        table.addHeaderClickListener(evt -> {

                String column = (String) evt.getPropertyId();
                Notification.show("Clicked " + column +
                        "with " + evt.getButtonName());
        });

        // Disable the default sorting behavior
        table.setSortEnabled(false);

        // Adjust the table height a bit
        table.setPageLength(table.size());

        return table;
    }

    public Component _016_columnResize() {
        final Table table = Helper.createSmallTable("ColumnResize Events");

        table.addColumnResizeListener(evt -> {

                // Get the new width of the resized column
                int width = evt.getCurrentWidth();

                // Get the property ID of the resized column
                String column = (String) evt.getPropertyId();

                // Do something with the information
                table.setColumnFooter(column, String.valueOf(width) + "px");
        });

        // Must be immediate to send the resize events immediately
        table.setImmediate(true);

        // Enable the footer
        table.setFooterVisible(true);

        // Give plenty of width
        table.setWidth("400px");

        // Adjust the table height a bit
        table.setPageLength(table.size());

        return table;
    }

    public Component _017_columnReordering() {
        VerticalLayout layout = new VerticalLayout();

        final Table table = Helper.createSmallTable("Reordering Columns");

        table.setColumnReorderingAllowed(true);

        // Adjust the table height a bit
        table.setPageLength(table.size());

        layout.addComponent(table);
        layout.addComponent(new Label("Reorder the columns"));

        return layout;
    }

    public Component _018_columnCollapsing() {
        final Table table = Helper.createSmallTable("Column Collapsing");

        // Allow the user to collapse and uncollapse columns
        table.setColumnCollapsingAllowed(true);

        // Collapse this column programmatically
        try {
            table.setColumnCollapsed("born", true);
        } catch (IllegalStateException e) {
            // Can't occur - collapsing was allowed above
            System.err.println("Something horrible occurred");
        }

        // Give enough width for the table to accommodate the
        // initially collapsed column later
        table.setWidth("250px");

        // Adjust the table height a bit
        table.setPageLength(table.size());

        return table;
    }

    public Component _019_rowHeaders() {
        Planet planets[] = {
                new Planet("Mercury", 0, 0, 0, 0, false),
                new Planet("Venus", 0, 0, 0, 0, false),
                new Planet("Earth", 0, 0, 0, 0, false),
                new Planet("Mars", 0, 0, 0, 0, false),
                new Planet("Jupiter", 0, 0, 0, 0, false),
                new Planet("Saturn", 0, 0, 0, 0, false),
                new Planet("Uranus", 0, 0, 0, 0, false),
                new Planet("Neptune", 0, 0, 0, 0, false),
        };

        // Bind it to a table
        Grid<Planet> table = new Grid<>("Custom Column Headers");
        table.setItems(planets);
        table.addStyleName("rowheaders");
        table.setWidth("250px");
        table.addColumn(c ->
                new ThemeResource("img/planets/" + c.getName() + "_symbol.png"))
                .setRenderer(new ImageRenderer<>());
        table.addColumn(Planet::getName).setCaption("Name");
        table.setFrozenColumnCount(1);

        // Adjust the table height a bit
        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(planets.length);

        return table;
    }

    public Component _020_contextMenu() {
        // Have a table with some data
        List<Scientist> scientists = Helper.generateScientists();
        final Grid<Scientist> table = new Grid<>(Scientist.class);
        table.setCaption("My Table");
        table.setItems(scientists);

        ContextMenu ctxMenu = new ContextMenu(table, true);
        ctxMenu.addItem("Add After", evt -> {
            Scientist newItem = new Scientist();
            newItem.setName("Someone");
            newItem.setCity("Somewhere");
            newItem.setYear(1901);
            Scientist selected = table.asSingleSelect().getValue();
            if (selected != null) // Clicked on an item
                scientists.add(scientists.indexOf(selected)+1, newItem);
            else
                scientists.remove(selected);
            table.setItems(scientists);
        });
        ctxMenu.addItem("Delete Item", evt -> {
            scientists.remove(table.asSingleSelect().getValue());
            table.setItems(scientists);
        });
        return table;
    }

    private String nameFilterTxt="", bornFilterTxt="";
    private void applyFilter(ListDataProvider<Object[]> listDataProvider) {
        listDataProvider.setFilter(o ->
                String.valueOf(o[0]).toLowerCase().contains(nameFilterTxt.toLowerCase()) &&
                String.valueOf(o[1]).contains(bornFilterTxt)
        );
    }
    public Component _021_filtering() {
        final Grid<Object[]> table = new Grid<>("Table with column filters");

        // Insert this data
        Object people[][] = {
                {"Galileo", 1564},
                {"Monnier", 1715},
                {"Väisälä", 1891},
                {"Oterma", 1915},
                {"Valtaoja", 1951}
        };

        ListDataProvider<Object[]> listDataProvider = DataProvider.ofItems(people);
        table.setDataProvider(listDataProvider);
        table.addColumn(p -> p[0]).setCaption("Name").setId("Name");
        table.addColumn(p -> p[1]).setCaption("Born").setId("Born");

        com.vaadin.ui.TextField nameFilter = new com.vaadin.ui.TextField();
        nameFilter.addValueChangeListener(evt -> {
            nameFilterTxt = evt.getValue();
            applyFilter(listDataProvider);
        });

        com.vaadin.ui.TextField bornFilter = new com.vaadin.ui.TextField();
        bornFilter.addValueChangeListener(evt -> {
            bornFilterTxt = evt.getValue();
            applyFilter(listDataProvider);
        });

        HeaderRow filterRow = table.appendHeaderRow();
        filterRow.getCell("Name").setComponent(nameFilter);
        filterRow.getCell("Born").setComponent(bornFilter);

        return table;
    }

    public Component _022_columnFormattingSimple() {
        // Create a table that overrides the default property (column) format
        final Table table = new Table("Formatted Table") {

            @Override
            protected String formatPropertyValue(Object rowId,
                                                 Object colId, Property<?> property) {
                // Format by property type
                if (property.getType() == Date.class) {
                    SimpleDateFormat df =
                            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    return df.format((Date) property.getValue());
                }

                return super.formatPropertyValue(rowId, colId, property);
            }
        };

        // The table has some columns
        table.addContainerProperty("Time", Date.class, null);

        // Put some sample data in the table
        long date = 0;
        for (int i = 0; i < 100; i++) {
            table.addItem(new Object[]{new Date(date)}, i);
            date += (long) (Math.random() * 1E11);
        }

        table.setPageLength(6);

        return table;
    }

    public Component _023_columnFormattingExtended() {
        // Use a specific locale for formatting decimal numbers
        final Locale locale = new Locale("fi", "FI");

        // Create a table that overrides the default property (column) format
        final Table table = new Table("Formatted Table") {

            @Override
            protected String formatPropertyValue(Object rowId,
                                                 Object colId, Property<?> property) {
                String pid = (String) colId;

                if ("Time".equals(pid)) {
                    // Format a date
                    SimpleDateFormat df =
                            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    return df.format((Date) property.getValue());

                } else if ("Value".equals(pid)) {
                    // Format a decimal value for a specific locale
                    DecimalFormat df = new DecimalFormat("#.00",
                            new DecimalFormatSymbols(locale));
                    return df.format(property.getValue());

                } else if ("Message".equals(pid)) {
                    // You can add text while formatting, etc.
                    return "Msg #" + (property.getValue());
                }
                return super.formatPropertyValue(rowId, colId, property);
            }
        };

        // Right-align the decimal column
        table.setCellStyleGenerator((source, itemId, propertyId) -> {

                if (propertyId != null &&
                        ("Value".equals(propertyId)))
                    return "right-aligned";
                return null;
        });

        // The table has some columns
        table.addContainerProperty("Time", Date.class, null);
        table.addContainerProperty("Value", Double.class, null);
        table.addContainerProperty("Message", Integer.class, null);

        // Put some data in the table
        long date = 0;
        for (int i = 0; i < 100; i++) {
            table.addItem(new Object[]{
                    new Date(date),
                    Math.random() * 1000,
                    (int) (100 * Math.random())
            }, i);
            date += (long) (Math.random() * 10000000000L);
        }

        table.setPageLength(6);

        return table;
    }

    public Component _024_cellStyleGenerator() {
        Table table = new Table("Table with Cell Styles");

        // Add some columns in the table. In this example, the property
        // IDs of the container are integers so we can determine the
        // column number easily.
        table.addContainerProperty("0", String.class, null, "", null,
                null);
        for (int i = 0; i < 8; i++)
            table.addContainerProperty(String.valueOf(i + 1), String.class, null,
                    String.valueOf((char) (65 + i)), null, null);

        // Add some items in the table.
        table.addItem(new Object[]{
                "1", "R", "N", "B", "Q", "K", "B", "N", "R"}, 0);
        table.addItem(new Object[]{
                "2", "P", "P", "P", "P", "P", "P", "P", "P"}, 1);
        for (int i = 2; i < 6; i++)
            table.addItem(new Object[]{String.valueOf(i + 1),
                    "", "", "", "", "", "", "", ""}, i);
        table.addItem(new Object[]{
                "7", "P", "P", "P", "P", "P", "P", "P", "P"}, 6);
        table.addItem(new Object[]{
                "8", "R", "N", "B", "Q", "K", "B", "N", "R"}, 7);
        table.setPageLength(8);

        // Set cell style generator
        table.setCellStyleGenerator((source, itemId, propertyId) -> {

                // Per-row style setting, not relevant in this example.
                if (propertyId == null)
                    return "green";

                int row = (Integer) itemId;
                int col = Integer.parseInt((String) propertyId);

                // The first column.
                if (col == 0)
                    return "rowheader";

                // Other cells.
                if ((row + col) % 2 == 0)
                    return "black";
                else
                    return "white";
        });
        return table;
    }

    public Component _025_editorForm() {
        VerticalLayout layout = new VerticalLayout();

        // Create a container for such beans
        final BeanItemContainer<Bean> beans = new BeanItemContainer<>(Bean.class);

        // Add some beans to it
        beans.addBean(new Bean("Mung bean", 1452.0));
        beans.addBean(new Bean("Chickpea", 686.0));
        beans.addBean(new Bean("Lentil", 1477.0));
        beans.addBean(new Bean("Common bean", 129.0));
        beans.addBean(new Bean("Soybean", 1866.0));
        beans.addItem(new Bean("Java Bean", 0.0));

        // A layout for the table and form
        HorizontalLayout hLayout = new HorizontalLayout();

        // Bind a table to it
        final Table table = new Table("Beans of All Sorts", beans);
        table.setVisibleColumns("name", "energy");
        table.setPageLength(7);
        table.setBuffered(false);
        hLayout.addComponent(table);

        // Create a form for editing a selected or new item.
        // It is invisible until actually used.
        final Form form = new Form();
        form.setCaption("Edit Item");
        form.setVisible(false);
        form.setBuffered(true);
        hLayout.addComponent(form);

        // When the user selects an item, show it in the form
        table.addValueChangeListener(evt -> {

                // Close the form if the item is deselected
                if (evt.getProperty().getValue() == null) {
                    form.setVisible(false);
                    return;
                }

                // Bind the form to the selected item
                form.setItemDataSource(table.getItem(table.getValue()));
                form.setVisible(true);

                // The form was opened for editing an existing item
                table.setData(null);
        });
        table.setSelectable(true);
        table.setImmediate(true);

        // Creates a new bean for editing in the form before adding
        // it to the table. Adding is handled after committing
        // the form.
        final Button newBean = new Button("New Bean");
        newBean.addClickListener(evt -> {

                // Create a new item; this will create a new bean
                Object itemId = beans.addItem(new Bean("Foo", 42.0));
                form.setItemDataSource(table.getItem(itemId));

                // Make the form a bit nicer
                form.setVisibleItemProperties("name", "energy");
                //((TextField)form.getField("name"))
                //        .setNullRepresentation("");

                // The form was opened for editing a new item
                table.setData(itemId);

                table.select(itemId);
                table.setEnabled(false);
                newBean.setEnabled(false);
                form.setVisible(true);
        });

        // When OK button is clicked, commit the form to the bean
        final Button submit = new Button("Save");
        submit.addClickListener((ClickListener) event -> {
            form.commit();
            form.setVisible(false); // and close it

            // New items have to be added to the container
            if (table.getValue() == null) {
                // Commit the addition
                table.commit();

                table.setEnabled(true);
                newBean.setEnabled(true);
            }
        });
        form.getFooter().addComponent(submit);

        // Make modification to enable/disable the Save button
        form.setFormFieldFactory(new DefaultFieldFactory() {

            @Override
            public Field createField(Item item, Object propertyId, Component uiContext) {
                final AbstractField field = (AbstractField)
                        super.createField(item, propertyId, uiContext);
                field.addValueChangeListener((ValueChangeListener) event -> submit.setEnabled(form.isModified()));
                if (field instanceof TextField) {
                    final TextField tf = (TextField) field;
                    tf.addTextChangeListener(new TextChangeListener() {

                        @Override
                        public void textChange(TextChangeEvent event) {
                            if (form.isModified() ||
                                    !event.getText().equals(tf.getValue())) {
                                submit.setEnabled(true);

                                // Not needed after first event unless
                                // want to detect also changes back to
                                // unmodified value.
                                tf.removeListener(this);

                                // Has to be reset because the
                                // removeListener() setting causes
                                // updating the field value from the
                                // server-side.
                                tf.setValue(event.getText());
                            }
                        }
                    });
                }
                field.setImmediate(true);

                return field;
            }
        });

        Button cancel = new Button("Cancel");
        cancel.addClickListener((ClickListener) event -> {
            form.discard();  // Not really necessary
            form.setVisible(false); // and close it
            table.discard(); // Discards possible addItem()
            table.setEnabled(true);
            if (table.getData() != null)
                beans.removeItem(table.getData());
            newBean.setEnabled(true);
        });
        form.getFooter().addComponent(cancel);

        hLayout.setSpacing(true);
        layout.addComponent(hLayout);
        layout.addComponent(newBean);

        return layout;
    }

    public Component _026_scrollToItem() {
        VerticalLayout layout = new VerticalLayout();

        List<Scientist> items = Helper.generateScientists();

        // Create to table to show it
        Grid<Scientist> table = new Grid<>(Scientist.class);
        table.setCaption("Scrolled Table");
        table.setItems(items);

        // Pick up some item
        int rowIndex = items.size() / 2;

        Button button = new Button("Scroll to item #" + rowIndex, evt -> {
            // Scroll the table to that item
            table.scrollTo(rowIndex);
        });

        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(10);
        layout.addComponents(table, button);

        return layout;
    }

    public Component _027_removeAllItems() {
        VerticalLayout layout = new VerticalLayout();

        // Create to table to show it
        final Grid<Scientist> table = new Grid<>(Scientist.class);
        table.setCaption("Table with Stuff");
        table.setItems(Helper.generateScientists());
        layout.addComponent(table);

        Button recreate = new Button("Recreate Data");
        recreate.addClickListener((ClickListener) event -> {
            table.setItems( Collections.emptyList());

            // Add some content to it
            table.setItems(Helper.generateScientists());
        });
        layout.addComponent(recreate);

        return layout;
    }

    public Component _028_detailsShrink() {
        VerticalLayout layout = new VerticalLayout();

        Panel panel = new Panel("A fixed-size area for the stuff");
        panel.setWidth("600px");
        panel.setHeight("500px");
        layout.addComponent(panel);

        // Intermediate layout to contain both table and details
        VerticalLayout panelContent = new VerticalLayout();
        panelContent.setSizeFull();
        panelContent.setMargin(true);
        panel.setContent(panelContent);

        // Have a table with some data
        final Table table = new Table("Master Table",
                Helper.generateContent());
        table.setSizeFull();
        table.setSelectable(true);
        panelContent.addComponent(table);
        panelContent.setExpandRatio(table, 1.0f);

        // Show item details here
        class MyForm extends FormLayout {

            private TextField name = new TextField("Name");
            private TextField city = new TextField("City");
            private TextField year = new TextField("Year");

            private void setItemDataSource(Item item) {
                addComponents(name, city, year);

                FieldGroup binder = new FieldGroup(item);
                binder.bindMemberFields(this);
            }
        }

        final MyForm myform = new MyForm();
        myform.setVisible(false);
        panelContent.addComponent(myform);

        // When an item is selected, resize the table
        table.addValueChangeListener((ValueChangeListener) event -> {
            Object selectedItemId = event.getProperty().getValue();
            if (selectedItemId != null) {
                myform.setItemDataSource(table.getItem(selectedItemId));
                if (!myform.isVisible()) {
                    table.setCurrentPageFirstItemId(selectedItemId);
                    myform.setVisible(true);
                }
            } else
                myform.setVisible(false);
        });
        table.setImmediate(true);

        return layout;
    }

    public Component _029_cssInjection() {
        Table table = new Table("Colorful Table");

        table.setPageLength(16);

        // We wrap cell contents inside a CssLayout, which
        // allows CSS injection.
        table.addContainerProperty("Color", CssLayout.class, null);

        for (int i = 0; i < 16; i++) {
            final int color = 255 - i * 16;

            // Get hexadecimal representation
            StringBuilder sb = new StringBuilder();
            new Formatter(sb).format("#%1$02x%1$02xff", color);
            final String colorCode = sb.toString();

            // Stylable wrapper for the cell content
            CssLayout content = new CssLayout() {

                @Override
                public String getCss(Component c) {
                    return "background: " + colorCode + ";";
                }
            };

            // The actual cell content
            Label label = new Label("Here's color " + colorCode);
            label.setSizeUndefined();
            content.addComponent(label);

            table.addItem(new Object[]{content}, i);
        }

        return table;
    }

    public Component _030_editable() {
        VerticalLayout layout = new VerticalLayout();

        // Table with a component column in non-editable mode
        Table table = new Table("The Important People");
        table.addContainerProperty("Name", String.class, null);
        table.addContainerProperty("Born", Date.class, null);
        table.addContainerProperty("Alive", Boolean.class, null);

        // Insert this data
        Object people[][] = {{"Galileo", 1564, false},
                {"Monnier", 1715, false},
                {"Väisälä", 1891, false},
                {"Oterma", 1915, false},
                {"Valtaoja", 1951, true}};

        // Insert the data, transforming the year number to Date object
        for (int i = 0; i < people.length; i++) {
            Object item[] = {people[i][0],
                    new GregorianCalendar((Integer) people[i][1], 0, 1).getTime(),
                    people[i][2]};
            table.addItem(item, i);
        }
        table.setPageLength(table.size());

        // Set a custom field factory that overrides the default factory
        table.setTableFieldFactory(new DefaultFieldFactory() {

            @Override
            public Field<?> createField(Container container, Object itemId,
                                        Object propertyId, Component uiContext) {
                // Create fields by their class
                Class<?> cls = container.getType(propertyId);

                // Create a DateField with year resolution for dates
                if (cls.equals(Date.class)) {
                    DateField df = new DateField();
                    df.setResolution(Resolution.YEAR);
                    return df;
                }

                // Create a CheckBox for Boolean fields
                if (cls.equals(Boolean.class))
                    return new CheckBox();

                // Otherwise use the default field factory
                return super.createField(container, itemId, propertyId,
                        uiContext);
            }
        });

        // Put the table in editable mode
        table.setEditable(true);
        table.setSelectable(true);
        layout.addComponent(table);

        // Allow switching to non-editable mode
        CheckBox editable = new CheckBox("Editable", true);
        editable.addValueChangeListener(valueChange -> // Java 8
                table.setEditable(editable.getValue()));
        layout.addComponent(editable);

        return layout;
    }

    public Component _031_editableHeights() {
        VerticalLayout layout = new VerticalLayout();

        // Have a container with some data
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("birthday", Date.class, null);
        container.addContainerProperty("nationality", String.class, null);
        container.addContainerProperty("name", String.class, null);

        // Some example data
        Object people[][] = {
                {new GregorianCalendar(1564, 0, 0).getTime(), "Italian", "Galileo"},
                {new GregorianCalendar(1715, 0, 0).getTime(), "French", "Monnier"},
                {new GregorianCalendar(1891, 0, 0).getTime(), "Finnish", "Väisälä"},
                {new GregorianCalendar(1915, 0, 0).getTime(), "Finnish", "Oterma"},
                {new GregorianCalendar(1951, 0, 0).getTime(), "Finnish", "Valtaoja"}};

        // Insert the data
        for (int i = 0; i < people.length; i++) {
            Item item = container.addItem(i);
            item.getItemProperty("birthday").setValue(people[i][0]);
            item.getItemProperty("nationality").setValue(people[i][1]);
            item.getItemProperty("name").setValue(people[i][2]);
        }

        // Have the table
        Table table = new Table("Edible Table", container);
        table.setPageLength(table.size());

        // Set a custom field factory that overrides the default factory
        table.setTableFieldFactory(new DefaultFieldFactory() {

            @Override
            public Field<?> createField(Container container, Object itemId,
                                        Object propertyId, Component uiContext) {
                if ("nationality".equals(propertyId)) {
                    ComboBox select = new ComboBox();
                    select.addItem("Italian");
                    select.addItem("French");
                    select.addItem("Finnish");
                    select.setNullSelectionAllowed(false);
                    return select;
                }

                return super.createField(container, itemId, propertyId, uiContext);
            }
        });
        table.setEditable(true);

        // Allow switching to non-editable mode
        CheckBox editable = new CheckBox("Table is editable", true);
        editable.addValueChangeListener(event -> // Java 8
                table.setEditable(editable.getValue()));
        editable.setImmediate(true);

        layout.addComponent(table);
        layout.addComponent(editable);

        return layout;
    }

    public Component _032_comboBox() {
        VerticalLayout layout = new VerticalLayout();

        final Grid<Bodies> table = new Grid<>("My Table");
        table.setWidth("100%");

        Bodies bodies[] = new Bodies[11];
        bodies[0] = new Bodies("Mercury", "Planet", "Nobody");
        bodies[1] = new Bodies("Venus", "Planet", "Women");
        bodies[2] = new Bodies("Earth", "Planet", "People");
        bodies[3] = new Bodies("Mars", "Planet", "Men");
        bodies[4] = new Bodies("Ceres", "Minor Planet", "Nobody");
        bodies[5] = new Bodies("Jupiter", "Planet", "Monoliths");
        bodies[6] = new Bodies("Saturn", "Planet", "Nobody");
        bodies[7] = new Bodies("Uranus", "Planet", "Nobody");
        bodies[8] = new Bodies("Neptune", "Planet", "Nobody");
        bodies[9] = new Bodies("Pluto", "Plutoid", "Plutonians");
        bodies[10] = new Bodies("Eris", "Minor Planet", "Plutonians");

        com.vaadin.ui.ComboBox<String> nameCb = new com.vaadin.ui.ComboBox<>();
        nameCb.setItems("Mercury", "Venus", "Earth", "Mars",
                        "Jupiter", "Saturn", "Uranus", "Neptune",
                        "Pluto", "Ceres", "Eris");

        com.vaadin.ui.ComboBox<String> classificationCb = new com.vaadin.ui.ComboBox<>();
        classificationCb.setItems("Planet", "Minor Planet", "Plutoid", "Dwarf Planet");

        com.vaadin.ui.ComboBox<String> populationCb = new com.vaadin.ui.ComboBox<>();
        populationCb.setItems("Nobody", "People", "Women", "Men", "Martians", "Monoliths", "Plutonians");

        table.setItems(bodies);
        table.addColumn(Bodies::getName).setCaption("Name")
                .setEditorComponent(nameCb, Bodies::setName);
        table.addColumn(Bodies::getClassification).setCaption("Classification")
                .setEditorComponent(classificationCb, Bodies::setClassification);
        table.addColumn(Bodies::getPopulation).setCaption("Population")
                .setEditorComponent(populationCb, Bodies::setPopulation);
        for (int col = 3; col < 15; col++) {
            int finalCol = col;
            table.addColumn(s -> "Col " + finalCol).setCaption("Col " + col);
        }

        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(bodies.length);

        layout.addComponent(new Panel(table));

        final CheckBox editable = new CheckBox("Editable", true);
        editable.addValueChangeListener(event -> table.getEditor().setEnabled(editable.getValue()));
        editable.setImmediate(true);
        layout.addComponent(editable);

        table.getEditor().setEnabled(true);

        return layout;
    }

    public Component _033_buffering() {
        VerticalLayout layout = new VerticalLayout();

        // The data model + some data
        Bean[] beans = {
            new Bean("Mung bean", 1452.0),
            new Bean("Chickpea", 686.0),
            new Bean("Lentil", 1477.0),
            new Bean("Common bean", 129.0),
            new Bean("Soybean", 1866.0),
            new Bean("Java Bean", 0.0)
        };

        // This is the buffered editable table
        final Grid<Bean> editable = new Grid<>("Editable");
        editable.getEditor().setEnabled(true);
        editable.getEditor().setBuffered(false);
        editable.setItems(beans);
        editable.addColumn(Bean::getName)
                .setEditorBinding(editable.getEditor().getBinder()
                        .bind(new com.vaadin.ui.TextField(), Bean::getName, Bean::setName))
                .setCaption("Name");
        editable.addColumn(Bean::getEnergy)
                .setEditorBinding(editable.getEditor().getBinder()
                        .forField(new com.vaadin.ui.TextField())
                        .withConverter(new StringToDoubleConverter("Must be double"))
                        .bind(Bean::getEnergy, Bean::setEnergy))
                .setCaption("Energy");

        // Read-only table
        Grid<Bean> rotable = new Grid<>(Bean.class);
        rotable.setCaption("Rotable");
        rotable.setItems(beans);

        final Button save = new Button("Update", event -> {
            rotable.getDataProvider().refreshAll();
        });

        HorizontalLayout hor = new HorizontalLayout();
        hor.addComponent(editable);
        hor.addComponent(layout);
        layout.addComponent(rotable);
        layout.addComponent(save);
        layout.setMargin(false);

        return hor;
    }

    public Component _034_spreadsheet() {
        VerticalLayout layout = new VerticalLayout();

            // The data model + some data
        final BeanItemContainer<Bean> beans =
                new BeanItemContainer<>(Bean.class);
        beans.addBean(new Bean("Mung bean", 1452.0));
        beans.addBean(new Bean("Chickpea", 686.0));
        beans.addBean(new Bean("Lentil", 1477.0));
        beans.addBean(new Bean("Common bean", 129.0));
        beans.addBean(new Bean("Soybean", 1866.0));
        beans.addItem(new Bean("Java Bean", 0.0));

        // The table to edit
        final Table table = new Table();
        table.setPageLength(table.size());

        // The table needs to be in editable mode
        table.setEditable(true);

        // Map to find a field component by its item ID and property ID
        final HashMap<Object, HashMap<Object, Field<?>>> fields = new HashMap<>();

        table.setTableFieldFactory((container, itemId, propertyId, uiContext) -> {
            final TextField tf = new TextField();
            tf.setData(new ItemPropertyId(itemId, propertyId));

            // Needed for the generated column
            tf.setImmediate(true);

            // Manage the field in the field storage
            fields.computeIfAbsent(itemId, key -> new HashMap<>());
            fields.get(itemId).put(propertyId, tf);

            tf.setReadOnly(true);
            tf.addFocusListener(evt -> {
                // Make the entire item editable
                HashMap<Object, Field<?>> items = fields.get(itemId);
                for (Field<?> f : items.values())
                    f.setReadOnly(false);

                table.select(itemId);
            });
            tf.addBlurListener(evt -> {
                // Make the entire item read-only
                HashMap<Object, Field<?>> items = fields.get(itemId);
                for (Field<?> f : items.values())
                    f.setReadOnly(true);
            });

            return tf;
        });

        table.setContainerDataSource(beans);

        // Add a generated column
        table.addGeneratedColumn("kcal", (source, itemId, columnId) -> {
            double value = (Double) beans.getItem(itemId).getItemProperty("energy").getValue();
            return value * 0.000239005736;
        });

        table.setVisibleColumns("name", "energy", "kcal");

        // Panel that handles the keyboard navigation
        Panel navigator = new Panel("The \"Spreadsheet\"");
        navigator.addStyleName(ValoTheme.PANEL_WELL);
        Layout navigatorContent = new VerticalLayout();
        navigator.setContent(navigatorContent);
        navigatorContent.addComponent(new Label("Press" +
                "<ul>" +
                "  <li><b>Enter</b> to edit/accept an item,</li>" +
                "  <li><b>Tab</b> and <b>Shift+Tab</b> to navigate fields, and</li>" +
                "  <li><b>Up</b> and <b>Down</b> to move to previous/next item.</li>" +
                "</ul>", ContentMode.HTML));
        navigatorContent.addComponent(table);
        ((VerticalLayout) navigator.getContent()).setExpandRatio(table, 1.0f);
        navigator.addActionHandler(new KbdHandlerSpreadsheet(table, fields, beans));

        // Use selecting the row to be edited
        table.setSelectable(true);
        table.select(table.getItemIds().toArray()[0]);
        table.focus();

        layout.setSpacing(true);
        layout.addComponent(navigator);

        return layout;
    }

    public Component _035_longTable() {

        // The table to edit
        Grid<Scientist> table = new Grid<>(null, Helper.generateScientists());
        table.setWidth("500px");
        table.setHeightMode(HeightMode.ROW);
        table.setHeightByRows(8);
        table.getEditor().setEnabled(true);

        return table;
    }

    public Component _036_adding() {
        // The data model + some data
        BeanItemContainer<Bean> beans =
                new BeanItemContainer<>(Bean.class);
        beans.addBean(new Bean("Mung bean", 1452.0));
        beans.addBean(new Bean("Chickpea", 686.0));
        beans.addBean(new Bean("Lentil", 1477.0));
        beans.addBean(new Bean("Common bean", 129.0));
        beans.addBean(new Bean("Soybean", 1866.0));
        beans.addItem(new Bean("Java Bean", 0.0));

        // The table to edit
        final Table table = new Table(null, beans);
        table.setWidth("500px");
        table.setPageLength(10);
        table.setEditable(true);
        table.setVisibleColumns("name", "energy");

        table.setTableFieldFactory(new DefaultFieldFactory() {

            @Override
            public Field<?> createField(Container container, Object itemId,
                                        Object propertyId, Component uiContext) {
                Field<?> field = super.createField(container, itemId, propertyId, uiContext);
                if (field instanceof TextField) {
                    ((TextField) field).setNullRepresentation("");
                    field.setWidth("100%");
                }
                return field;
            }
        });

        // Adding new items
        Button add = new Button("Add New Item", e -> // Java 8
                beans.addBean(new Bean(null, 0.0)));

        return new VerticalLayout(table, add);
    }
}
