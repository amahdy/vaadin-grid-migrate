package net.amahdy.vaadin.demo.grid.data;

import java.io.Serializable;

public class Planet implements Serializable {

    String name;
    float equatorDiameter; // Relative to Earth
    float mass;            // Relative to Earth
    float orbitalRadius;   // AU
    float orbitalPeriod;   // Years
    boolean rings;

    public Planet(String name, float equatorDiameter, float mass, float orbitalRadius, float orbitalPeriod, boolean rings) {
        this.name = name;
        this.equatorDiameter = equatorDiameter;
        this.mass = mass;
        this.orbitalRadius = orbitalRadius;
        this.orbitalPeriod = orbitalPeriod;
        this.rings = rings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getEquatorDiameter() {
        return equatorDiameter;
    }

    public void setEquatorDiameter(float equatorDiameter) {
        this.equatorDiameter = equatorDiameter;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getOrbitalRadius() {
        return orbitalRadius;
    }

    public void setOrbitalRadius(float orbitalRadius) {
        this.orbitalRadius = orbitalRadius;
    }

    public float getOrbitalPeriod() {
        return orbitalPeriod;
    }

    public void setOrbitalPeriod(float orbitalPeriod) {
        this.orbitalPeriod = orbitalPeriod;
    }

    public boolean isRings() {
        return rings;
    }

    public void setRings(boolean rings) {
        this.rings = rings;
    }
}