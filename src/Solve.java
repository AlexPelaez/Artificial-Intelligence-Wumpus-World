/**
 * Created by Alex on 2/27/20.
 */

public class Solve {
    private static final int DEATH_COST = 10000;
    private char[][] world;

    private boolean[][][] knowledgeBase;
    private int[] startLocation;
    private int wumpusCount;
    private int explorerY;
    private int explorerX;
    private int explorerDY = 1;
    private int explorerDX = 0;
    private int cellsExploredReactive;
    private int goldFoundReactive;
    private int wumpusFoundReactive;
    private int wumpusKilledReactive;
    private int explorerReactiveSuicide;
    private int pitFoundReactive;

    private int cellsExploredKB;
    private int goldFoundKB;
    private int wumpusFoundKB;
    private int wumpusKilledKB;
    private int explorerKBSuicide;
    private int pitFoundKB;

    private int cost = 0;
    private int costKB = 0;

    private boolean reachedGold = false;

    private int remainingWumpusCount;
    private int arrowCount;



    public Solve(char[][] world, int wumpusCount) {
        this.world = world;
        this.wumpusCount = wumpusCount;
        this.startLocation = getStartLocation();
        arrowCount = wumpusCount;
        remainingWumpusCount = wumpusCount;
        explorerY = startLocation[0];
        explorerX = startLocation[1];
        this.knowledgeBase = new boolean[world.length][world.length][11];
//        solveRecative();

        this.world = world;
        this.wumpusCount = wumpusCount;
        this.startLocation = getStartLocation();
        arrowCount = wumpusCount;
        remainingWumpusCount = wumpusCount;
        explorerY = startLocation[0];
        explorerX = startLocation[1];
        solveKB();
//        solveKnowdledgeBased();
    }

    private int solveKB(){
        boolean reachedGold = false;
        boolean smelledStench = false;
        int action = 1;
        costKB = 0;
        while (!reachedGold){
//            printWorld1();
//            System.out.println();
//            System.out.println("X: " + explorerX);
//            System.out.println("Y: " + explorerY);
            boolean[] sensorList;

            costKB--;
            sensorList = sense(action);
            updateKnowledgeBase(sensorList);
            if(world[explorerY][explorerX] == 'g'){
                goldFoundReactive++;
                System.out.println("Gold found");
                reachedGold = true;
                costKB = costKB + 1000;
                return costKB;

            }
            else if(world[explorerY][explorerX] == 'p'){
                pitFoundReactive++;
                System.out.println("Death by pit");
                reachedGold = true;
                costKB = costKB - 10000;
                return costKB;

            }
            else if(world[explorerY][explorerX] == 'w'){
                wumpusFoundReactive++;
                System.out.println("Death by wumpus");
                costKB =costKB - 10000;
                return costKB;
            }
            else if(costKB == -100000000){
//                System.out.println("Death by suicide");
                explorerReactiveSuicide++;
                costKB =costKB - 10000;
                return costKB;

            } else{
                sense(1);
                world[explorerY][explorerX] = 'v';
                printWorld();
                System.out.println();
                System.out.println("X: " + explorerX);
                System.out.println("Y: " + explorerY);


                if (world[explorerY][explorerX - 1] == 'w') {
                    smelledStench = true;
                }

                if(world[explorerY][explorerX+1] == 'w') {
                    smelledStench = true;
                }

                if(world[explorerY-1][explorerX] == 'w') {
                    smelledStench = true;
                }
                if(world[explorerY+1][explorerX] == 'w') {
                    smelledStench = true;

                }

            }

            if(smelledStench == true && arrowCount !=  0){
                System.out.println("arrow Shot");
                System.out.println("DX: " + explorerDX);
                System.out.println("DY: " + explorerDY);
                if(shootArrow()){
                    System.out.println("Wumpus Killed");

                    wumpusKilledReactive++;
                }
                arrowCount--;
                costKB -= 10;
                smelledStench = false;
                world[explorerY][explorerX] = 'v';
                if(!goForward())
                {
                    turnLeft();
                    costKB--;
                }

                costKB--;
            } else {
                int counter = 0;
                while(counter !=20) {
                    counter++;
                    int move = (int) (Math.random() * 4);


                    if (move == 0) {
                        if (queryKB(explorerY - 1, explorerX)) {
                            counter=20;
                            System.out.println("move North");
                            if (explorerDX == 1 && explorerDY == 0) { // facing east

                                turnLeft();
                                costKB--;

                            } else if (explorerDX == -1 && explorerDY == 0) { // facing west
                                turnRight();
                                costKB--;

                            } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                                turnLeft();
                                costKB--;
                                turnLeft();
                                costKB--;
                            }
                            costKB--;
                            goForward();
                        }
                    } else if (move == 1) {
                        if (queryKB(explorerY, explorerX + 1)) {
                            counter=20;
                            System.out.println("move East");
                            if (explorerDX == -1 && explorerDY == 0) { // facing west
                                turnLeft();
                                costKB--;
                                turnLeft();
                                costKB--;

                            } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                                turnLeft();
                                costKB--;

                            } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                                turnRight();
                                costKB--;
                            }
                            costKB--;
                            goForward();

                        }
                    } else if (move == 2) {
                        if (queryKB(explorerY + 1, explorerX)) {
                            counter=20;
                            System.out.println("move south");
                            if (explorerDX == 1 && explorerDY == 0) { // facing east
                                turnRight();
                                costKB--;
                            } else if (explorerDX == -1 && explorerDY == 0) { // facing west
                                turnLeft();
                                costKB--;
                            } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                                turnLeft();
                                costKB--;
                                turnLeft();
                                costKB--;
                            }
                            costKB--;
                            goForward();
                        }
                    } else if (move == 3) {
                        if (queryKB(explorerY, explorerX - 1)) {
                            counter=20;
                            System.out.println("move West");
                            if (explorerDX == 1 && explorerDY == 0) { // facing east
                                turnLeft();
                                costKB--;
                                turnLeft();
                                costKB--;

                            } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                                turnRight();
                                costKB--;

                            } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                                turnLeft();
                                costKB--;
                            }
                            costKB--;
                            goForward();
                        }
                    }




                }

            }
            cellsExploredReactive++;
        }
        return costKB;
    }

    private boolean queryKB(int newY, int newX){
        // sensor list: breeze, stench, bump, scream, gold
        // KnowledgeBase: pit, breeze, wumpus, stench, visited, gold, safe, obstacle, wumpusFact, pitFact, safeFact
        if(knowledgeBase[newY][newX][10]){
            return true;
        } else if(knowledgeBase[newY][newX][0] || knowledgeBase[newY][newX][2] || knowledgeBase[newY][newX][8] || knowledgeBase[newY][newX][9]){
            return false;
        }

       return true;
    }


    private void updateKnowledgeBase(boolean[] sensorList){
        boolean[] input = new boolean[8];
        // sensor list: breeze, stench, bump, scream, gold
        // KnowledgeBase: pit, breeze, wumpus, stench, visited, gold, safe, obstacle, wumpusFact, pitFact, safeFact
        knowledgeBase[explorerY][explorerX][10] = true;
        if(!(sensorList[0]||sensorList[1])){
            knowledgeBase[explorerY][explorerX + 1][10] = true;
            knowledgeBase[explorerY + 1][explorerX][10] = true;
            knowledgeBase[explorerY][explorerX - 1][10] = true;
            knowledgeBase[explorerY - 1][explorerX][10] = true;
        }
        if(sensorList[2]){
            if(explorerDX == 1 && explorerDY == 0) { // facing east
                knowledgeBase[explorerY][explorerX+1][7] = true;
                knowledgeBase[explorerY][explorerX+1][4] = true;
                knowledgeBase[explorerY][explorerX+1][6] = false;
                knowledgeBase[explorerY][explorerX+1][0] = false;
                knowledgeBase[explorerY][explorerX+1][2] = false;
                knowledgeBase[explorerY][explorerX+1][8] = false;
                knowledgeBase[explorerY][explorerX+1][9] = false;
                knowledgeBase[explorerY][explorerX+1][10] = false;


            } else if (explorerDX == -1 && explorerDY == 0) { // facing west
                knowledgeBase[explorerY][explorerX - 1][7] = true;
                knowledgeBase[explorerY][explorerX - 1][4] = true;
                knowledgeBase[explorerY][explorerX - 1][6] = false;
                knowledgeBase[explorerY][explorerX - 1][0] = false;
                knowledgeBase[explorerY][explorerX - 1][2] = false;
                knowledgeBase[explorerY][explorerX - 1][8] = false;
                knowledgeBase[explorerY][explorerX - 1][9] = false;
                knowledgeBase[explorerY][explorerX - 1][10] = false;

            } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                knowledgeBase[explorerY + 1][explorerX][7] = true;
                knowledgeBase[explorerY + 1][explorerX][4] = true;
                knowledgeBase[explorerY + 1][explorerX][6] = false;
                knowledgeBase[explorerY + 1][explorerX][0] = false;
                knowledgeBase[explorerY + 1][explorerX][2] = false;
                knowledgeBase[explorerY + 1][explorerX][8] = false;
                knowledgeBase[explorerY + 1][explorerX][9] = false;
                knowledgeBase[explorerY + 1][explorerX][10] = false;


            } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                knowledgeBase[explorerY - 1][explorerX][7] = true;
                knowledgeBase[explorerY - 1][explorerX][6] = false;
                knowledgeBase[explorerY - 1][explorerX][0] = false;
                knowledgeBase[explorerY - 1][explorerX][2] = false;
                knowledgeBase[explorerY - 1][explorerX][8] = false;
                knowledgeBase[explorerY - 1][explorerX][9] = false;
                knowledgeBase[explorerY - 1][explorerX][10] = false;

            }
        }
        if(sensorList[4]){
            knowledgeBase[explorerY][explorerX][5] = true;
        }
        if(sensorList[1]){
           if(!knowledgeBase[explorerY][explorerX+1][10] && !knowledgeBase[explorerY][explorerX+1][8] && knowledgeBase[explorerY][explorerX+1][7]){
               knowledgeBase[explorerY][explorerX + 1][2] = true;
           }
            if(!knowledgeBase[explorerY+1][explorerX][10] && !knowledgeBase[explorerY+1][explorerX][8] && knowledgeBase[explorerY+1][explorerX][7]){
                knowledgeBase[explorerY + 1][explorerX][2] = true;
            }
            if(!knowledgeBase[explorerY][explorerX-1][10] && !knowledgeBase[explorerY][explorerX-1][8] && knowledgeBase[explorerY][explorerX-1][7]){
                knowledgeBase[explorerY][explorerX - 1][2] = true;
            }
            if(!knowledgeBase[explorerY-1][explorerX][10] && !knowledgeBase[explorerY-1][explorerX][8] && knowledgeBase[explorerY-1][explorerX][7]){
                knowledgeBase[explorerY - 1][explorerX][2] = true;
            }
        }

        if(sensorList[0]){
            if(!knowledgeBase[explorerY][explorerX+1][10] && !knowledgeBase[explorerY][explorerX+1][9] && knowledgeBase[explorerY][explorerX+1][7]){
                knowledgeBase[explorerY][explorerX + 1][0] = true;
            }
            if(!knowledgeBase[explorerY+1][explorerX][10] && !knowledgeBase[explorerY+1][explorerX][9] && knowledgeBase[explorerY+1][explorerX][7]){
                knowledgeBase[explorerY + 1][explorerX][0] = true;
            }
            if(!knowledgeBase[explorerY][explorerX-1][10] && !knowledgeBase[explorerY][explorerX-1][9] && knowledgeBase[explorerY][explorerX-1][7]){
                knowledgeBase[explorerY][explorerX - 1][0] = true;
            }
            if(!knowledgeBase[explorerY-1][explorerX][10] && !knowledgeBase[explorerY-1][explorerX][9] && knowledgeBase[explorerY-1][explorerX][7]){
                knowledgeBase[explorerY - 1][explorerX][0] = true;
            }
        }
    }
    private boolean[] sense(int lastAction) {
        // sensor list: breeze, stench, bump, scream, gold
        boolean[] sensorList = new boolean[5];

        // breeze and stench
        if(world[explorerY][explorerX-1] == 'p') {
            sensorList[0] = true;
        } else if(world[explorerY][explorerX-1] == 'w') {
            sensorList[1] = true;
        }

        if(world[explorerY][explorerX+1] == 'p') {
            sensorList[0] = true;
        } else if(world[explorerY][explorerX+1] == 'w') {
            sensorList[1] = true;
        }

        if(world[explorerY-1][explorerX] == 'p') {
            sensorList[0] = true;
        } else if(world[explorerY-1][explorerX] == 'w') {
            sensorList[1] = true;
        }

        if(world[explorerY+1][explorerX] == 'p') {
            sensorList[0] = true;
        } else if(world[explorerY+1][explorerX] == 'w') {
            sensorList[1] = true;
        }


        if(lastAction == -1) {   // bump
            sensorList[2] = true;
        } else if(lastAction == -2) {  // scream
            sensorList[3] = true;
        }

        if(world[explorerY][explorerX] == 'g') {   // gold
            sensorList[4] = true;
        }

        return sensorList;
    }

    private boolean turnLeft() {
        if(explorerDX == 1 && explorerDY == 0) { // facing east
            explorerDY = -1;
            explorerDX = 0;
            return true;

        } else if (explorerDX == -1 && explorerDY == 0) { // facing west
            explorerDY = 1;
            explorerDX = 0;
            return true;

        } else if (explorerDX == 0 && explorerDY == 1) { // facing south
            explorerDY = 0;
            explorerDX = 1;
            return true;

        } else if (explorerDX == 0 && explorerDY == -1) { // facing north
            explorerDY = 0;
            explorerDX = -1;
            return true;
        }
        return false;
    }

    private boolean turnRight() {
        if(explorerDX == 1 && explorerDY == 0) { // facing east
            explorerDY = 1;
            explorerDX = 0;
            return true;
        } else if (explorerDX == -1 && explorerDY == 0) { // facing west
            explorerDY = -1;
            explorerDX = 0;
            return true;

        } else if (explorerDX == 0 && explorerDY == 1) { // facing south
            explorerDY = 0;
            explorerDX = -1;
            return true;

        } else if (explorerDX == 0 && explorerDY == -1) { // facing north
            explorerDY = 0;
            explorerDX = 1;
            return true;
        }
        return false;
    }

    private boolean goForward() {
        if(explorerDX == 1 && explorerDY == 0) { // facing east
            if(explorerX != world.length){
                if(world[explorerY][explorerX + 1] != 'o') {
                    explorerX = explorerX + 1;
                    return true;
                } else {
                    return false;
                }
            }
        } else if (explorerDX == -1 && explorerDY == 0) { // facing west
            if(explorerX != 0){
                if(world[explorerY][explorerX - 1] != 'o') {
                    explorerX = explorerX - 1;
                    return true;
                } else {
                    return false;
                }
            }

        } else if (explorerDX == 0 && explorerDY == 1) { // facing south
            if(explorerY != world.length){
                if(world[explorerY + 1][explorerX] != 'o') {
                    explorerY = explorerY + 1;
                    return true;
                } else {
                    return false;
                }
            }

        } else if (explorerDX == 0 && explorerDY == -1) { // facing north
            if(explorerY != 0){
                if(world[explorerY - 1][explorerX] != 'o') {
                    explorerY = explorerY - 1;
                    return true;
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean shootArrow() {
        if(explorerDX == 1 && explorerDY == 0) { // facing east
            for(int i = explorerX; i < world.length; i++){
                if(world[explorerY][i] == 'w'){
                    world[explorerY][i] = 'x';
                    return true;
                } else if (world[explorerY][i] == 'o'){
                    return false;
                }
            }

        } else if (explorerDX == -1 && explorerDY == 0) { // facing west
            for(int i = explorerX; i > 0; i--){
                if(world[explorerY][i] == 'w'){
                    return true;
                } else if (world[explorerY][i] == 'o'){
                    return false;
                }
            }
        } else if (explorerDX == 0 && explorerDY == 1) { // facing south
            for(int i = explorerY; i < world.length; i++){
                if(world[i][explorerX] == 'w'){
                    return true;
                } else if (world[i][explorerX] == 'o'){
                    return false;
                }
            }

        } else if (explorerDX == 0 && explorerDY == -1) { // facing north
            for(int i = explorerY; i < 0; i--){
                if(world[i][explorerX] == 'w'){
                    return true;
                } else if (world[i][explorerX] == 'o'){
                    return false;
                }
            }
        }
        return false;
    }

    private int solveRecative(){
        boolean reachedGold = false;
        boolean smelledStench = false;
//        help1
        cost = 0;
        while (!reachedGold){
//            printWorld1();
//            System.out.println();
//            System.out.println("X: " + explorerX);
//            System.out.println("Y: " + explorerY);
            cost--;
            if(world[explorerY][explorerX] == 'g'){
                goldFoundReactive++;
                System.out.println("Gold found");
                reachedGold = true;
                cost = cost + 1000;
                return cost;

            }
            else if(world[explorerY][explorerX] == 'p'){
                pitFoundReactive++;
                System.out.println("Death by pit");
                reachedGold = true;
                cost = cost - 10000;
                return cost;

            }
            else if(world[explorerY][explorerX] == 'w'){
                wumpusFoundReactive++;
                System.out.println("Death by wumpus");
                cost = cost - 10000;

                return cost;
            }
            else if(cost == -100000000){
//                System.out.println("Death by suicide");
                explorerReactiveSuicide++;
                cost =cost - 10000;
                return cost;

            } else{
                world[explorerY][explorerX] = 'v';
                printWorld();
                System.out.println();
                System.out.println("X: " + explorerX);
                System.out.println("Y: " + explorerY);

                if (world[explorerY][explorerX - 1] == 'w') {
                    smelledStench = true;
                }

                if(world[explorerY][explorerX+1] == 'w') {
                    smelledStench = true;
                }

                if(world[explorerY-1][explorerX] == 'w') {
                    smelledStench = true;
                }
                if(world[explorerY+1][explorerX] == 'w') {
                    smelledStench = true;

                }

            }

            if(smelledStench == true && arrowCount !=  0){
                System.out.println("arrow Shot");
                System.out.println("DX: " + explorerDX);
                System.out.println("DY: " + explorerDY);
                if(shootArrow()){
                    System.out.println("Wumpus Killed");

                    wumpusKilledReactive++;
                }
                arrowCount--;
                cost -= 10;
                smelledStench = false;
                world[explorerY][explorerX] = 'v';
                if(!goForward())
                {
                    turnLeft();
                    cost--;
                }

                cost--;
            } else {
                int move = (int) (Math.random() * 4);

                if (move == 0) {
                    System.out.println("move North");
                    if (explorerDX == 1 && explorerDY == 0) { // facing east

                        turnLeft();
                        cost--;

                    } else if (explorerDX == -1 && explorerDY == 0) { // facing west
                        turnRight();
                        cost--;

                    } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                        turnLeft();
                        cost--;
                        turnLeft();
                        cost--;
                    }
                } else if (move == 1) {
                    System.out.println("move East");
                    if (explorerDX == -1 && explorerDY == 0) { // facing west
                        turnLeft();
                        cost--;
                        turnLeft();
                        cost--;

                    } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                        turnLeft();
                        cost--;

                    } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                        turnRight();
                        cost--;
                    }


                } else if (move == 2) {
                    System.out.println("move south");
                    if (explorerDX == 1 && explorerDY == 0) { // facing east
                        turnRight();
                        cost--;
                    } else if (explorerDX == -1 && explorerDY == 0) { // facing west
                        turnLeft();
                        cost--;
                    } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                        turnLeft();
                        cost--;
                        turnLeft();
                        cost--;
                    }
                } else if (move == 3) {
                    System.out.println("move West");
                    if (explorerDX == 1 && explorerDY == 0) { // facing east
                        turnLeft();
                        cost--;
                        turnLeft();
                        cost--;

                    } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                        turnRight();
                        cost--;

                    } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                        turnLeft();
                        cost--;
                    }
                }
                if(!goForward())
                {
                    turnLeft();
                    cost--;
                }

            }
            cellsExploredReactive++;
        }
        return cost;
    }

    private int[] getStartLocation() {
        int[] location = new int[2];

        for(int i = 0; i < world.length; i++) {
            for(int j = 0; j < world.length; j++) {
                if(world[i][j] == 'a') {
                    location[0] = i;
                    location[1] = j;
                }
            }
        }
        return location;
    }

    private void printWorld() {
        for(int i = 0; i < world.length; i++) {
            System.out.print(" ");
            for(int j = 0; j < world.length; j++) {
                System.out.print(world[i][j] + " ");
            }
            System.out.println("");
        }
    }
//    private void printWorld1() {
//        for(int i = 0; i < world.length; i++) {
//            System.out.print(" ");
//            for(int j = 0; j < world.length; j++) {
//                System.out.print(world1[i][j] + " ");
//            }
//            System.out.println("");
//        }
//    }
    public int getCellsExploredReactive() {
        return cellsExploredReactive;
    }

    public int getGoldFoundReactive() {
        return goldFoundReactive;
    }

    public int getWumpusFoundReactive() {
        return wumpusFoundReactive;
    }

    public int getWumpusKilledReactive() {
        return wumpusKilledReactive;
    }

    public int getExplorerReactiveSuicide() {
        return explorerReactiveSuicide;
    }

    public int getPitFoundReactive() {
        return pitFoundReactive;
    }

    public int getCost() {
        return cost;
    }
    public int getCostKB() {
        return costKB;
    }

}
