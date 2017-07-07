package net.amahdy.vaadin.demo.grid.data;

import java.io.Serializable;

public class Bean implements Serializable {

    String name;
    double energy; // Energy content in kJ/100g

    public Bean(String name, double energy) {
        this.name = name;
        this.energy = energy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }
}