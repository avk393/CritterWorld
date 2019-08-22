/*
 * CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Donovan McCray
 * dom342
 * 16190
 * Ankith Kandikonda
 * avk393
 * 16190
 * Slip days used: <1>
 * Spring 2019
 */
// Ankith Kandikonda, avk393
// Donovan McCray, dom342
package assignment5;

import javafx.scene.paint.Color;

/**
 * Critter2 just wants to relax.
 * Critter2 happily sits and looks around until they get hungry (half energy).
 * Critter2 casually strolls away from a fight.
 */
public class Critter2 extends Critter {

    public Critter2() {

    }

    @Override
    public CritterShape viewShape() {
        return CritterShape.DIAMOND;
    }

    @Override
    public Color viewOutlineColor() {
        return Color.PURPLE;
    }

    @Override
    public Color viewFillColor() { return Color.PURPLE; }

    @Override
    public void doTimeStep() {
        if (getEnergy() < (Params.START_ENERGY/2)) {
            walk(getRandomInt(7));
        } else {
            look(getRandomInt(7),false);
        }
    }

    @Override
    public boolean fight(String opponent) {
        switch (opponent) {
            case "@":               // clover
                return true;
            default:
                walk(getRandomInt(7));
                return false;

        }
    }

    @Override
    public String toString() {
        return "2";
    }
}