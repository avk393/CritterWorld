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
 * Critter4 is an opportunist.
 * They constantly walk around in search for reward.
 * They will pick a fight against "easy" Critters (Critter2 and Critter3).
 * They will run away from the alpha (Critter1).
 */
public class Critter4 extends Critter {

    public Critter4() {

    }

    @Override
    public CritterShape viewShape() {
        return CritterShape.STAR;
    }

    @Override
    public Color viewFillColor() {
        return Color.BROWN;
    }

    @Override
    public void doTimeStep() {
        walk(getRandomInt(7));
    }

    @Override
    public boolean fight(String opponent) {
        switch (opponent) {
            case "@":               // clover
                return true;
            case "1":               // alpha
                run(getRandomInt(7));
                return false;
            case "2":
                return true;
            case "3":
                return true;
            default:
                return true;

        }
    }

    @Override
    public String toString() {
        return "4";
    }
}