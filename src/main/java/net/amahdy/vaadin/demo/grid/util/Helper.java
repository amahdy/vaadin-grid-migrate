package net.amahdy.vaadin.demo.grid.util;

import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.ui.Table;
import net.amahdy.vaadin.demo.grid.data.Scientist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amahdy on 7/7/17.
 */
public abstract class Helper {

    public static String generateScientistsJson() {
        List<Scientist> scientists = generateScientists();
        List<String> items = new ArrayList<>();
        for (Scientist scientist: scientists) {
            items.add("{"
                    + "\"name\":\"" + scientist.getName() + "\","
                    + "\"city\":\"" + scientist.getCity() + "\","
                    + "\"year\":\"" + scientist.getYear() + "\""
                    + "}"
            );
        }
        return "[" + String.join(",", items) + "]";
    }

    public static List<Scientist> generateScientists() {
        List<Scientist> scientists = new ArrayList<>();
        String[] firstnames = new String[]{"Isaac", "Ada", "Charles", "Douglas"};
        String[] lastnames = new String[]{"Newton", "Lovelace", "Darwin", "Adams"};
        String[] cities = new String[]{"London", "Oxford", "Innsbruck", "Turku"};
        for (int i = 0; i < 1000; i++) {
            Scientist scientist = new Scientist();
            scientist.setName(firstnames[(int) (Math.random() * 4)] + " " + lastnames[(int) (Math.random() * 4)]);
            scientist.setCity(cities[(int) (Math.random() * 4)]);
            scientist.setYear(1800 + (int) (Math.random() * 200));
            scientists.add(scientist);
        }

        return scientists;
    }

    public static IndexedContainer generateContent() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("name", String.class, null);
        container.addContainerProperty("city", String.class, null);
        container.addContainerProperty("year", Integer.class, null);

        addContent(container);
        return container;
    }

    public static void addContent(IndexedContainer container) {
        String[] firstnames = new String[]{"Isaac", "Ada", "Charles", "Douglas"};
        String[] lastnames = new String[]{"Newton", "Lovelace", "Darwin", "Adams"};
        String[] cities = new String[]{"London", "Oxford", "Innsbruck", "Turku"};
        for (int i = 0; i < 1000; i++) {
            Object itemId = container.addItem();
            String name = firstnames[(int) (Math.random() * 4)] + " " + lastnames[(int) (Math.random() * 4)];
            container.getItem(itemId).getItemProperty("name").setValue(name);
            String city = cities[(int) (Math.random() * 4)];
            container.getItem(itemId).getItemProperty("city").setValue(city);
            Integer year = 1800 + (int) (Math.random() * 200);
            container.getItem(itemId).getItemProperty("year").setValue(year);
        }
    }

    public static Table createSmallTable(String caption) {
        Table table = new Table(caption);

        table.addContainerProperty("lastname", String.class, null);
        table.addContainerProperty("born", Integer.class, null);
        table.addContainerProperty("died", Integer.class, null);

        // Insert some data
        Object people[][] = {{"Galileo", 1564, 1642},
                {"Väisälä", 1891, 1971},
                {"Valtaoja", 1951, null}};
        for (int i = 0; i < people.length; i++)
            table.addItem(people[i], new Integer(i));

        // Set nicer header names
        table.setColumnHeader("lastname", "Name");
        table.setColumnHeader("born", "Born");
        table.setColumnHeader("died", "Died");

        return table;
    }
}
