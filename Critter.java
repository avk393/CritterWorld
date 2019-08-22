/* CRITTERS GUI <MyClass.java>
 * EE422C Project 5 submission by
 * Replace <...> with your actual data.
 * Ankith Kandikonda
 * avk393
 * 16190
 * Donovan McCray
 * dom342
 * 16190
 * Slip days used: <0>
 * Spring 2019
 */
// Ankith Kandikonda, avk393
// Donovan McCray, dom342
package assignment5;

import javafx.animation.AnimationTimer;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * See the PDF for descriptions of the methods and fields in this
 * class.
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter {

    /* START --- NEW FOR PROJECT 5 */
    public enum CritterShape {
        CIRCLE,
        SQUARE,
        TRIANGLE,
        DIAMOND,
        STAR
    }

    /* the default color is white, which I hope makes critters invisible by default
     * If you change the background color of your View component, then update the default
     * color to be the same as you background
     *
     * critters must override at least one of the following three methods, it is not
     * proper for critters to remain invisible in the view
     *
     * If a critter only overrides the outline color, then it will look like a non-filled
     * shape, at least, that's the intent. You can edit these default methods however you
     * need to, but please preserve that intent as you implement them.
     */
    public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.WHITE;
    }

    public javafx.scene.paint.Color viewOutlineColor() {
        return viewColor();
    }

    public javafx.scene.paint.Color viewFillColor() {
        return viewColor();
    }

    public abstract CritterShape viewShape();

    protected final String look(int direction, boolean steps) {
        this.energy -= Params.LOOK_ENERGY_COST;

        int observed_X;
        int observed_Y;

        if (steps) {
            // TODO: I have no idea if this checks two positions in the indicated direction
            observed_X = x_coord + (movement[direction][0]*4);
            observed_Y = y_coord + (movement[direction][1]*4);
        } else {
            observed_X = x_coord + (movement[direction][0]*2);
            observed_Y = y_coord + (movement[direction][1]*2);
        }

        if(observed_X<0) observed_X= observed_X + Params.WORLD_WIDTH;
        else if(observed_X>Params.WORLD_WIDTH) observed_X = observed_X - Params.WORLD_WIDTH;
        if(observed_Y<0) observed_Y = observed_Y + Params.WORLD_HEIGHT;
        else if(observed_Y>Params.WORLD_HEIGHT) observed_Y = observed_Y - Params.WORLD_HEIGHT;

        // if looking at a critter, return the toString for that critter
        for (Critter crit : population) {
            if (observed_X == crit.x_coord && observed_Y == crit.y_coord) {
                return crit.toString();
            }
        }
        return null;
    }

    public static String runStats(List<Critter> critters) {
        String output = "";
        output += critters.size() + " critters as follows -- ";

//        System.out.print("" + critters.size() + " critters as follows -- ");
        Map<String, Integer> critter_count = new HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            critter_count.put(crit_string,
                    critter_count.getOrDefault(crit_string, 0) + 1);
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
//            System.out.print(prefix + s + ":" + critter_count.get(s));
            output += prefix + s + ":" + critter_count.get(s);
            prefix = ", ";
        }
//        System.out.println();

        return output;
    }

    public static void displayWorld(Object pane) {
        // TODO Implement this method
    }

	/* END --- NEW FOR PROJECT 5
			rest is unchanged from Project 4 */

    private int energy = 0;

    private int x_coord;
    private int y_coord;
    private boolean hasMoved;
    private Shape shape;

    private static int timeStep = 0;
    private static boolean fightPhase = false;
    private static List<Critter> population = new ArrayList<Critter>();
    private static List<Critter> babies = new ArrayList<Critter>();
    private static ArrayList<ArrayList<Critter>> fightingList = new ArrayList<ArrayList<Critter>>();

    /* Gets the package name.  This assumes that Critter and its
     * subclasses are all in the same package. */
    private static String myPackage;

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    private static Random rand = new Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new Random(new_seed);
    }

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the qualified name of a concrete
     * subclass of Critter, if not, an InvalidCritterException must be
     * thrown.
     *
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void createCritter(String critter_class_name)
            throws InvalidCritterException {

        try {
            Class className = Class.forName(myPackage + "." + critter_class_name);

            Critter newCrit;
            if (critter_class_name.equals("Goblin")) {
                newCrit = (Goblin) className.newInstance();
            } else {
                newCrit = (Critter) className.newInstance();
            }

            // set energy to Params.START_ENERGY
            newCrit.energy = assignment5.Params.START_ENERGY;

            // place critter in random position
            newCrit.x_coord = getRandomInt(assignment5.Params.WORLD_WIDTH);
            newCrit.y_coord = getRandomInt(assignment5.Params.WORLD_HEIGHT);

            population.add(newCrit);
            // add critter to GUI
            newCrit.shape = CritterWorld.addCritter(newCrit, newCrit.x_coord, newCrit.y_coord, true);

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
            throw new InvalidCritterException(critter_class_name);
        }
    }

    /**
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.
     *                           Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name)
            throws InvalidCritterException {

        List<Critter> critterList = new ArrayList<Critter>();
        for (Critter crit : population) {

            try {
                Class critClass = Class.forName(myPackage + "." + critter_class_name);
                Critter newCrit = (Critter) critClass.newInstance();

                // critClass.isInstance(crit) should also work
                if (crit.getClass() == newCrit.getClass()) {
                    // If of the same class as desired
                    critterList.add(crit);
                }

            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
                throw new InvalidCritterException(critter_class_name);
            }

        }
        return critterList;
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
        Iterator<Critter> it = population.iterator();
        while (it.hasNext()) {
            Critter critter = it.next();
            CritterWorld.eraseCritter(critter.shape);
            it.remove();
        }
    }

    public static void worldTimeStep() {
        // 1 - Increment timestep
        if (timeStep == 0) {
            // **create 100 clovers and 25 goblins at the start **
            try {
                for (int i = 0; i < 100; i++) {
                    if (i < 25) {
                        Critter.createCritter("Goblin");
                    }
                    Critter.createCritter("Clover");
                }
            } catch (InvalidCritterException e) { }
        }
        for (Critter crit : population) crit.hasMoved = false;
        timeStep++;

        // 2 - Invoke doTimeStep() for every living critter
        for (Critter crit : population) {
            crit.doTimeStep();
        }
        System.out.println("got past time step");

        // 3 - Check if critters are in the same position
        checkForEncounters();
        // if so, have the critters fight
        fightPhase = true;
        doEncounters();
        System.out.println("got past encounters");

        // 4 - Update rest energy for all critters
        updateRestEnergy();

        // 5 - Generate clovers
        for (int i = 0; i < Params.REFRESH_CLOVER_COUNT; i++) {
            try {
                Critter.createCritter("Clover");
            } catch (InvalidCritterException e) { }
        }
        System.out.println("got past generating clovers");

        // 6 - Add new baby critters to the population
        population.addAll(babies);
        babies.clear();

        // 7 - Cull the dead critters
        ArrayList<Critter> dead = new ArrayList<Critter>();
        for (Critter crit : population) {
            if (crit.energy <= 0) {
                dead.add(crit);
                // remove critters from GUI
                CritterWorld.eraseCritter(crit.shape);
            }
        }
        population.removeAll(dead);
        System.out.println("got past dead section");

        // 8 - Reset for next turn
        for (Critter crit:population) {
            CritterWorld.drawCritter(crit.shape,crit.x_coord,crit.y_coord);
            crit.hasMoved = false;
        }
        System.out.println("got past redrawing critter for next turn");
        fightPhase = false;
    }

    private static void updateRestEnergy() {
        for (Critter critr : population) {
            critr.energy -= Params.REST_ENERGY_COST;
        }
    }

    /**
     * Checks the population for overlapping critters
     */
    private static void checkForEncounters() {

        for (int i = 0; i < population.size(); i++) {
            ArrayList<Critter> pairing = new ArrayList<Critter>();
            pairing.add(population.get(i));
            for (int j = i+1; j < population.size(); j++) {
                if (Arrays.equals(population.get(i).getPosition(), population.get(j).getPosition())) {
                    pairing.add(population.get(j));
                }
            }

            if (pairing.size() != 1) {
                fightingList.add(pairing);
            }
        }
    }

    private static void doEncounters() {
        // Process fights (if any)
        for (ArrayList<Critter> fight : fightingList) {
            for (int i = 0; i < fight.size()-1; i++) {
                Critter critA = fight.get(i);
                Critter critB = fight.get(i+1);
                int x_tempA = critA.x_coord;
                int y_tempA = critB.y_coord;
                int x_tempB = critB.x_coord;
                int y_tempB = critB.y_coord;

                boolean critAWantsFight = critA.fight(critB.toString());
                boolean critBWantsFight = critB.fight(critA.toString());

                // Check if both critters are still alive
                if (critA.energy > 0 && critB.energy > 0) {
                    // Check if critters are in the same position
                    if (Arrays.equals(critA.getPosition(), critB.getPosition())) {

                        int critARoll = 0;
                        if (critAWantsFight) {
                            critARoll = Critter.getRandomInt(critA.getEnergy());
                        }

                        int critBRoll = 0;
                        if (critBWantsFight) {
                            critBRoll = Critter.getRandomInt(critB.getEnergy());
                        }

                        if (critARoll > critBRoll) {
                            critA.energy += critB.energy/2;
                            critB.energy = 0;
                        } else if (critBRoll > critARoll) {
                            critB.energy += critA.energy/2;
                            critA.energy = 0;
                        } else if (critARoll == critBRoll) {
                            critA.energy += critB.energy/2;
                            critB.energy = 0;
                        }
                    }
                }

            }
        }
    }

    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    /* a one-character long string that visually depicts your critter
     * in the ASCII interface */
    public String toString() {
        return "";
    }

    protected int getEnergy() {
        return energy;
    }

    public int[] getPosition() {
        int[] position = new int[2];
        position[0] = this.x_coord;
        position[1] = this.y_coord;
        return position;
    }

    // table to access changes in coordinates from direction input
    private static final int [][] movement = new int[][] {
            {1,0},      //0
            {1,1},      //1
            {1,1},      //2
            {-1,1},     //3
            {-1,0},     //4
            {-1,-1},    //5
            {0,-1},     //6
            {1,-1}      //7
    };

    protected final void walk(int direction) {
// make sure to set hasMoved to false at the beginning of each turn
        this.energy -= Params.WALK_ENERGY_COST;
        if(hasMoved) return;

        int updated_x = x_coord + movement[direction][0];
        int updated_y = y_coord + movement[direction][1];

        if(updated_x<0) updated_x= updated_x + Params.WORLD_WIDTH;
        else if(updated_x>Params.WORLD_WIDTH) updated_x = updated_x - Params.WORLD_WIDTH;
        if(updated_y<0) updated_y = updated_y + Params.WORLD_HEIGHT;
        else if(updated_y>Params.WORLD_HEIGHT) updated_y = updated_y - Params.WORLD_HEIGHT;

        // if landing on a critter that has moved, can't move there if walking from a fight
        if(fightPhase) {
            for (Critter crit : population) if (updated_x == crit.x_coord && updated_y == crit.y_coord) return;
        }

        x_coord = updated_x;
        y_coord = updated_y;
        hasMoved = true;
    }

    protected final void run(int direction) {
        this.energy -= Params.RUN_ENERGY_COST;
        if(hasMoved) return;

        int updated_x = x_coord + (movement[direction][0]*2);
        int updated_y = y_coord + (movement[direction][1]*2);

        if(updated_x<0) updated_x= updated_x + Params.WORLD_WIDTH;
        else if(updated_x>Params.WORLD_WIDTH) updated_x = updated_x - Params.WORLD_WIDTH;
        if(updated_y<0) updated_y = updated_y + Params.WORLD_HEIGHT;
        else if(updated_y>Params.WORLD_HEIGHT) updated_y = updated_y - Params.WORLD_HEIGHT;

        // if landing on a critter that has moved, can't move there if it is running from a fight
        if(fightPhase) {
            for (Critter crit : population) if (updated_x == crit.x_coord && updated_y == crit.y_coord) return;
        }

        x_coord = updated_x;
        y_coord = updated_y;
        hasMoved = true;
    }

    protected final void reproduce(Critter offspring, int direction) {
        if(energy<=Params.MIN_REPRODUCE_ENERGY /*|| hasMoved???*/) return;

        offspring.energy = energy/2;
        if(energy%2==1) energy = energy/2 + 1;
        else energy = energy/2;

        offspring.x_coord = x_coord + movement[direction][0];
        offspring.y_coord = y_coord + movement[direction][1];
        offspring.hasMoved = true;
        offspring.shape = CritterWorld.addCritter(offspring,offspring.x_coord,offspring.y_coord,false);
        babies.add(offspring);
    }

    /**
     * The TestCritter class allows some critters to "cheat". If you
     * want to create tests of your Critter model, you can create
     * subclasses of this class and then use the setter functions
     * contained here.
     * <p>
     * NOTE: you must make sure that the setter functions work with
     * your implementation of Critter. That means, if you're recording
     * the positions of your critters using some sort of external grid
     * or some other data structure in addition to the x_coord and
     * y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {

        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        /**
         * This method getPopulation has to be modified by you if you
         * are not using the population ArrayList that has been
         * provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            return population;
        }

        /**
         * This method getBabies has to be modified by you if you are
         * not using the babies ArrayList that has been provided in
         * the starter code.  In any case, it has to be implemented
         * for grading tests to work.  Babies should be added to the
         * general population at either the beginning OR the end of
         * every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }
}
