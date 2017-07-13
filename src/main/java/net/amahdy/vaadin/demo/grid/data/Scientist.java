package net.amahdy.vaadin.demo.grid.data;

/**
 * Created by amahdy on 7/11/17.
 */
public class Scientist {

    private String name;
    private String city;
    private Integer year;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Name: " + getName() + ", City: " + getCity() + ", Year: " + getYear();
    }
}
