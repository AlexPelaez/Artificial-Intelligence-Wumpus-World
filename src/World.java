
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
//        world = new char[][] {
//        { 'o', 'o', 'o', 'o', 'o', 'o', 'o'},
//        { 'o', 'x', 'o', 'w', 'w', 'a', 'o'},
//        { 'o', 'p', 'o', 'x', 'o', 'w' ,'o'},
//        { 'o', 'x', 'o', 'x', 'x', 'w', 'o'},
//        { 'o', 'x', 'x', 'x', 'w', 'w' ,'o'},
//        { 'o', 'x', 'x', 'x', 'w', 'g', 'o'},
//        { 'o', 'o', 'o', 'o', 'o', 'o', 'o'}
//        };
//        wumpusCount = 7;
        populateWorld();
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
//        create a wall around the world
        for(int i = 0; i < size+2; i++) {
            world[0][i] = 'o';
            world[world.length-1][i] = 'o';
        }
//        loop through the entire world
        for(int i = 1; i < size+1; i++) {
            world[i][0] = 'o'; // set the wall around the world
            world[i][world.length-1] = 'o';
            for(int j = 1; j < size+1; j++) {
                set = false;
//                loop until position is properly set
                while(!set){
                    // find if any of the spaces should be anything but safe
                    boolean isPit = calculatePitProbability();
                    boolean isObs = calculateObsProbability();
                    boolean isWumpus = calculateWumpusProbability();

                    if(!isPit && !isWumpus && !isObs) { // if none of them are true make the space safe
                        world[i][j] = 'x';
                        set = true;
                    } else if (isPit && !isWumpus && !isObs){ // if it should be a pit set it
                        world[i][j] = 'p';
                        set = true;
                    } else if (isObs && !isWumpus && !isPit) { // if it should be a obs set it
                        world[i][j] = 'o';
                        set = true;
                    } else if (isWumpus && !isPit && !isObs) { // if it should be a wumpus set it
                        world[i][j] = 'w';
                        wumpusCount++;
                        set = true;
                    }
                }
            }
        }
        // place gold randomly in the world
        set = false;
        while(!set) {
            int i = (int)(Math.random()*size);
            int j = (int)(Math.random()*size);
            if(world[i][j] == 'x') {
                world[i][j] = 'g';
                set = true;
            }
        }

        // place agent randomly in the world
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
    //    returns true if there should be a pit and false if else
    private boolean calculatePitProbability() {
        if (Math.random() <= pit)
            return true;
        else
            return false;
    }
    //    returns true if there should be a obstical and false if else
    private boolean calculateObsProbability() {
        if (Math.random() <= obs)
            return true;
        else
            return false;
    }
//    returns true if there should be a wumpus and false if else
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
