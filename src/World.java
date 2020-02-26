
public class World {
    private int size;
    private double pit;
    private double obs;
    private double wumpus;

    private char[][] world;
    private int wumpusCount = 0;

    public World(int size, double p, double o, double w) {
        this.size = size;
        this.pit = p;
        this.obs = o;
        this.wumpus = w;

        populateWorld();
        printWorld();
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
        world = new char[size][size];
        boolean set = false;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
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
        for(int i = 0; i < size; i++) {
            System.out.print(" ");
            for(int j = 0; j < size; j++) {
                System.out.print(world[i][j] + " ");
            }
            System.out.println("");
        }
    }
}
