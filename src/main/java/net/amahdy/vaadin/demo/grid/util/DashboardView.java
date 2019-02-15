package net.amahdy.vaadin.demo.grid.util;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DashboardView extends VerticalLayout implements View {
    //private GridCellFilter filter;

    //private  TestGrid testGrid;
//    private GridCellFilter filter;

    private Grid<Person>  grid = new Grid<>(Person.class);

    private List<Person> people = Arrays.asList(
            new Person(1, "kari", "kukkonen", "01111"),
            new Person(2, "matias", "meikalainen", "02222")
    );

    public DashboardView() {
  
        ListDataProvider listDataProvider = new ListDataProvider(people);
//        ListDataProvider listDataProvider = DataProvider.ofItems(people);
        grid.setDataProvider(listDataProvider);

        Binder<Person> binder = grid.getEditor().getBinder();

        TextField tf = new TextField();
        Grid.Column<Person, String> column = grid.addColumn(
                Person::getFirstName).setEditorComponent(new TextField(), Person::setFirstName);

        column.setEditable(true);
        column.setHidden(false);
        column.setCaption("firstName");
        
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

//        // using the GridUtil addon to have the filters..
//        this.filter = new GridCellFilter(grid, Person.class);
//        this.filter.setNumberFilter("id", Integer.class, "You need to use only numbers..", "1", "1000");
//        this.filter.setTextFilter("firstName", true,true, "name starts with..");
//        this.filter.setTextFilter("lastName", true, true, "lastname starts with..");
//        this.filter.setTextFilter("sotu", true, true, "sotu starts with..");


        // Localize the editor button captions
        grid.getEditor().setSaveCaption("Tallenna");
        grid.getEditor().setCancelCaption("Peruuta");
        grid.getEditor().setEnabled(true);


        // now just add onclick listeners for save and cancel buttons..
        grid.getEditor().addSaveListener( e -> {

            Set<Person> selectedPeople = e.getGrid().getSelectedItems();
            try {
                binder.writeBean(e.getBean());
                System.out.println("writing the bean.. " + e.getBean().getFirstName() + " : " + e.getBean().getLastName() + " : " + e.getBean().getSotu() + " : " + e.getBean().getId());
                listDataProvider.refreshAll();
                

            } catch (ValidationException e1) {
                e1.printStackTrace();
            }
            for( Person p:  selectedPeople) {
                System.out.println(p.getFirstName() + " : " + p.getLastName());

            }
        });


        grid.setSizeFull();
        grid.setEnabled(true);
        addComponent(grid);
        
    }

    @Override
    public void enter(ViewChangeEvent event) {

        grid.getDataProvider().refreshAll();


    }
}