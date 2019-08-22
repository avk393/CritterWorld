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
 * Critter1 believes they're the alpha critter.
 * They will only attack weaker critters (Critter2 and Critter3) when energy is low (energy < 10%).
 * Critter1 finds Critter4's behavior annoying and thus always attempts to fight.
 * They're constantly walking around in search for clovers to bulk themselves up.
 */
public class Critter1 extends Critter {

    @Override
    public CritterShape viewShape() {
        return CritterShape.SQUARE;
    }

    @Override
    public Color viewFillColor() {
        return Color.BLUE;
    }

    @Override
    public Color viewOutlineColor() {
        return Color.SILVER;
    }

    @Override
    public void doTimeStep() { walk(getRandomInt(7)); }

    @Override
    public boolean fight(String opponent) {
        switch (opponent) {
            case "@":               // Clover
                return true;
            case "2":               // Relaxer
                if (getEnergy() < Params.START_ENERGY/10) return true;
                return false;
            case "3":               // Reproducer
                if (getEnergy() < Params.START_ENERGY/10) return true;
                return false;
            case "4":               // Opportunist
                return true;
            default:
                return true;

        }
    }

    @Override
    public String toString() {
        return "1";
    }
}