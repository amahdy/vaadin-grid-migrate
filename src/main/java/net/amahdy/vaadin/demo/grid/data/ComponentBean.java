package net.amahdy.vaadin.demo.grid.data;

import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.TextField;

import java.io.Serializable;

public class ComponentBean implements Serializable {

    TextField textfield = new TextField();
    CheckBox checkbox = new CheckBox();

    public ComponentBean(String text, boolean value) {
        textfield.setValue(text);
        checkbox.setValue(value);
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(CheckBox checkbox) {
        this.checkbox = checkbox;
    }

    public TextField getTextfield() {
        return textfield;
    }

    public void setTextfield(TextField textfield) {
        this.textfield = textfield;
    }
}