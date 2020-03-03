
public class World {
    private int size;
    private double pit;
    private double obs;
    private double wumpus;

//
    private char[][] world;
    private int wumpusCount = 0;

    public World(int size, double p, double o, double w) {
        this.size = size;
        this.pit = p;
        this.obs = o;
        this.wumpus = w;
        world = new char[][] {
        { 'o', 'o', 'o', 'o', 'o', 'o', 'o'},
        { 'o', 'x', 'o', 'w', 'w', 'x', 'o'},
        { 'o', 'p', 'o', 'x', 'o', 'w' ,'o'},
        { 'o', 'x', 'o', 'x', 'x', 'w', 'o'},
        { 'o', 'x', 'x', 'x', 'w', 'w' ,'o'},
        { 'o', 'x', 'x', 'a', 'w', 'g', 'o'},
        { 'o', 'o', 'o', 'o', 'o', 'o', 'o'}
        };
        wumpusCount = 7;
//        world[0] = {'o', 'o', 'o', 'o', 'o', 'o', 'o'};
//        o x o w w x o
//        o p o x o w o
//        o x o x x w o
//        o x x x w w o
//        o x x a w g o
//        o o o o o o o
//        populateWorld();
//        printWorld();
    }

    /*
        populateWorld method

        world char[][] key*
        empty = x
        pit = p
        obstacle = o
        wumpus = w
        gold = g
        agent = a
     */
    private void populateWorld() {
        world = new char[size+2][size+2];
        boolean set = false;
        for(int i = 0; i < size+2; i++) {
            world[0][i] = 'o';
            world[world.length-1][i] = 'o';
        }

        for(int i = 1; i < size+1; i++) {
            world[i][0] = 'o';
            world[i][world.length-1] = 'o';
            for(int j = 1; j < size+1; j++) {
                set = false;
                while(!set){
                    boolean isPit = calculatePitProbability();
                    boolean isObs = calculateObsProbability();
                    boolean isWumpus = calculateWumpusProbability();

                    if(!isPit && !isWumpus && !isObs) {
                        world[i][j] = 'x';
                        set = true;
                    } else if (isPit && !isWumpus && !isObs){
                        world[i][j] = 'p';
                        set = true;
                    } else if (isObs && !isWumpus && !isPit) {
                        world[i][j] = 'o';
                        set = true;
                    } else if (isWumpus && !isPit && !isObs) {
                        world[i][j] = 'w';
                        wumpusCount++;
                        set = true;
                    }
                }
            }
        }
        // place gold
        set = false;
        while(!set) {
            int i = (int)(Math.random()*size);
            int j = (int)(Math.random()*size);
            if(world[i][j] == 'x') {
                world[i][j] = 'g';
                set = true;
            }
        }

        // place agent
        set = false;
        while(!set) {
            int i = (int)(Math.random()*size);
            int j = (int)(Math.random()*size);
            if(world[i][j] == 'x') {
                world[i][j] = 'a';
                set = true;
            }
        }
    }

    private boolean calculatePitProbability() {
        if (Math.random() <= pit)
            return true;
        else
            return false;
    }

    private boolean calculateObsProbability() {
        if (Math.random() <= obs)
            return true;
        else
            return false;
    }

    private boolean calculateWumpusProbability() {
        if (Math.random() <= wumpus)
            return true;
        else
            return false;
    }

    private void printWorld() {
        for(int i = 0; i < size+2; i++) {
            System.out.print(" ");
            for(int j = 0; j < size+2; j++) {
                System.out.print(world[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public char[][] getWorld() {
        return world;
    }

    public int getWumpusCount() {
        return wumpusCount;
    }
}
