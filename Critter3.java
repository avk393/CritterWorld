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
 * Critter3 just wants to reproduce as much as possible.
 * While they have the stamina (energy > 50%), they reproduce.
 * They try to run (while lookking first) from all fights, with no regard for their children's safety. ¯\_(ツ)_/¯
 */
public class Critter3 extends Critter {

    public Critter3() {

    }

    @Override
    public CritterShape viewShape() {
        return CritterShape.TRIANGLE;
    }

    @Override
    public Color viewOutlineColor() {
        return Color.RED;
    }

    @Override
    public Color viewFillColor() {
        return Color.CRIMSON;
    }

    @Override
    public void doTimeStep() {
        if (getEnergy() > (Params.START_ENERGY/2)) {
            Critter3 baby = new Critter3();
            reproduce(baby, getRandomInt(7));
        } else {
            walk(getRandomInt(7));
        }
    }

    @Override
    public boolean fight(String opponent) {
        switch (opponent) {
            case "@":               // clover
                return true;
            case "1":
                return false;
            default:
                int rInt = getRandomInt(7);
                look(rInt,true);
                run(rInt);
                return false;

        }
    }

    @Override
    public String toString() {
        return "3";
    }
}