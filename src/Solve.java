
public class Solve {
    private static final int DEATH_COST = 10000;
    private char[][] world;//Represent our world

    private boolean[][][] knowledgeBase; //Boolean Array to Represent our percept at each location in teh world
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

    private int solveKB(){ //Used to navigate knowledge based agent
        initKnowledgeBase(); // inititialize values in knowledge base, unknown to true, everything else to false;
        boolean reachedGold = false; // while loop control if still searching for gold or dead
        boolean smelledStench = false; // boolean to determine if we want to shoot an arrow
        int move = 0; // integer to perform which cell to move into
        costKB = 0; //total cost of knowledge base navigation

        while (!reachedGold){
//            printWorld1();
//            System.out.println();
//            System.out.println("X: " + explorerX);
//            System.out.println("Y: " + explorerY);


            costKB--;//decrement for each move

            if(world[explorerY][explorerX] == 'g'){ //if found gold leave
                goldFoundKB++;
                System.out.println("Gold found");
                reachedGold = true;
                printWorld();
                costKB = costKB + 1000;
                return costKB;

            } else if(costKB < -100000000){ // if youve been walking around for too long, commit suicide
                System.out.println("Death by suicide");
                printWorld();
                reachedGold = true;
                explorerKBSuicide++;
                costKB = costKB - 10000;
                return costKB;

            }
            else if(world[explorerY][explorerX] == 'p'){ //if dead by pit terminate
                pitFoundKB++;
                System.out.println("Death by pit");
                reachedGold = true;
                printWorld();
                costKB = costKB - 10000;
                return costKB;

            }
            else if(world[explorerY][explorerX] == 'w'){ //if dead by wumpus terminate
                wumpusFoundKB++;
                System.out.println("Death by wumpus");
                printWorld();
                reachedGold = true;
                costKB =costKB - 10000;
                return costKB;
            } else{
                smelledStench = false;
                boolean[] sensorList; // boolean array to hold senses at each poistion
                sensorList = sense(); //sense at each position
                updateKnowledgeBase(sensorList); //add new information to knowledge base
                unification(); //unify knowledge in knowledge base to infer new facts
                world[explorerY][explorerX] = 'v'; // change position to visited
//                printWorld();
//                System.out.println();
//                System.out.println("X: " + explorerX);
//                System.out.println("Y: " + explorerY);

                //test if we have a wumpus next to us, if so get nervous and fire an arrow
                if(world[explorerY][explorerX + 1] == 'w') {
                    smelledStench = true;
                }
                if(world[explorerY-1][explorerX] == 'w') {
                    smelledStench = true;
                }
                if(world[explorerY+1][explorerX] == 'w') {
                    smelledStench = true;

                }
                if(world[explorerY ][explorerX - 1] == 'w') {
                    smelledStench = true;
                }

            }
            //fire the arrow
            if(smelledStench == true && arrowCount !=  0){
//                System.out.println("arrow Shot");
//                System.out.println("DX: " + explorerDX);
//                System.out.println("DY: " + explorerDY);
                if(shootArrow()){
//                    System.out.println("Wumpus Killed");
                    wumpusKilledKB++;

                }
                arrowCount--;
                costKB -= 10;
                smelledStench = false;
                world[explorerY][explorerX] = 'v';
            } else { // otherwise query teh locations around us

                boolean topCell = queryKB(explorerY - 1, explorerX); //Top Cell
                boolean bottomCell = queryKB(explorerY  + 1, explorerX); //Bottom Cell
                boolean rightCell = queryKB(explorerY, explorerX + 1);//Right Cell
                boolean leftCell = queryKB(explorerY, explorerX - 1);//Left Cell

                if(explorerDX == 1 && explorerDY == 0) { // facing east
                    if(!topCell && !bottomCell && !rightCell){ //no cells are safe, move randomly
                        move = (int) (Math.random() * 4);

                    }
                    else {
                        boolean cellChosen = false;
                        while(cellChosen) {
                            int cell = (int) (Math.random() * 3);
                            if (cell == 0) {
                                if (topCell) { //top cell was safe
                                    move = 0;
                                    cellChosen = true;
                                }

                            } else if (cell == 1) {
                                if (bottomCell) { // bottom cell was safe
                                    move = 2;
                                    cellChosen = true;
                                }
                            } else if (cell == 2) {
                                if (rightCell) { // right cell was safe
                                    move = 1;
                                    cellChosen = true;
                                }
                            }

                        }

                    }

                } else if (explorerDX == -1 && explorerDY == 0) { // facing west

                    if(!topCell && !bottomCell && !leftCell){ //no cells are safe, move randomly
                        move = (int) (Math.random() * 4);
                    } else {
                        boolean cellChosen = false;
                        while(cellChosen == false) {
                            int cell = (int) (Math.random() * 3);
                            if (cell == 0) {
                                if (topCell) { //top cell was safe
                                    move = 0;
                                    cellChosen = true;
                                }

                            } else if (cell == 1) {
                                if (bottomCell) { // bottom cell was safe
                                    move = 2;
                                    cellChosen = true;
                                }
                            } else if (cell == 2) {
                                if (leftCell) { // right cell was safe
                                    move = 3;
                                    cellChosen = true;
                                }
                            }
                        }

                    }

                } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                    if(!rightCell && !bottomCell && !leftCell){ //no cells are safe, move randomly
                        move = (int) (Math.random() * 4);

                    }else {
                        boolean cellChosen = false;
                        while (cellChosen == false) {
                            int cell = (int) (Math.random() * 3);
                            if (cell == 0) {
                                if (rightCell) { //top cell was safe
                                    move = 1;
                                    cellChosen = true;
                                }

                            } else if (cell == 1) {
                                if (bottomCell) { // bottom cell was safe
                                    move = 2;
                                    cellChosen = true;
                                }
                            } else if (cell == 2) {
                                if (leftCell) { // right cell was safe
                                    move = 3;
                                    cellChosen = true;
                                }
                            }
                        }
                    }

                } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                    if(!topCell && !rightCell && !leftCell){ //no cells are safe, move randomly
                        move = (int) (Math.random() * 4);

                    }else {
                        boolean cellChosen = false;
                        while(cellChosen == false) {
                            int cell = (int) (Math.random() * 3);
                            if (cell == 0) {
                                if (topCell) { //top cell was safe
                                    move = 0;
                                    cellChosen = true;
                                }

                            } else if (cell == 1) {
                                if (rightCell) { // bottom cell was safe
                                    move = 1;
                                    cellChosen = true;
                                }
                            } else if (cell == 2) {
                                if (leftCell) { // right cell was safe
                                    move = 3;
                                    cellChosen = true;
                                }
                            }
                        }

                    }

                }


                    if (move == 0) { // move to top cell
//                            System.out.println("move North");
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
                            if(!goForward()) // move forward and check bump
                            {
                                turnRight();
                                costKB--;
                            }
                    } else if (move == 1) { // move right
//                            System.out.println("move East");
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
                            if(!goForward()) //move forward and check bump
                            {
                                turnLeft();
                                costKB--;
                            }
                    } else if (move == 2) { // move down
//                            System.out.println("move south");
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
                            if(!goForward()) // move down and check for bump
                            {
                                turnLeft();
                                costKB--;
                            }
                    } else if (move == 3) { // move left
//                            System.out.println("move West");
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
                            if(!goForward()) //move left and check for bump
                            {
                                turnLeft();
                                costKB--;
                            }
                    }

                }
            cellsExploredKB++;
            }

        return costKB;
    }

    private boolean queryKB(int newY, int newX) { // function to query the knowledge base. takes in desired postion and returns true if safe otherwise false
        // sensor list: breeze, stench, bump, scream, gold
        // KnowledgeBase: unknown 0 , factSafe 1 , safe 2, factWumpus 3, wumpus 4, factPit 5, pit 6, obstacle 7, breeze 8, stench 9, glitter 10
        if(isOstacle(newY, newX)){
            return false;
        }
        else if(isSafe(newY, newX)){
            return true;
        }
        else if(isFactSafe(newY, newX)){
            return true;
        }
        else if(isFactWumpus(newY, newX) || isFactPit(newY, newX)){
            return false;
        }
        else if(isWumpus(newY, newX) || isPit(newY, newX) ){
            return false;
        }
        else{
            return false;
        }
    }


    public void initKnowledgeBase(){ // initialize the knowledge base at start
        for(int i = 0; i < knowledgeBase.length; i++) {
            for(int j = 0; j < knowledgeBase.length; j++) {
                knowledgeBase[i][j][0] = true;
                knowledgeBase[i][j][1] = false;
                knowledgeBase[i][j][2] = false;
                knowledgeBase[i][j][3] = false;
                knowledgeBase[i][j][4] = false;
                knowledgeBase[i][j][5] = false;
                knowledgeBase[i][j][6] = false;
            }
        }
    }




    private void updateKnowledgeBase(boolean[] sensorList) { // update knowledge base based on given senses
        // sensor list: breeze, stench, bump, scream, gold
        if(sensorList[2]){// Sensed bump
            if(explorerDX == 1 && explorerDY == 0) { // facing east
                makeObstacle(explorerY,explorerX + 1); //add bump to knowledgeBase
            } else if (explorerDX == -1 && explorerDY == 0) { // facing west
                makeObstacle(explorerY,explorerX - 1);//add bump to knowledgeBase
            } else if (explorerDX == 0 && explorerDY == 1) { // facing south
                makeObstacle(explorerY + 1,explorerX);//add bump to knowledgeBase
            } else if (explorerDX == 0 && explorerDY == -1) { // facing north
                makeObstacle(explorerY - 1,explorerX);//add bump to knowledgeBase
            }
        }
        else if(sensorList[1] && sensorList[0]){ // sensed breeze and stench
            makeBreeze(explorerY,explorerX);
            makeStench(explorerY,explorerX);
        }
        else if(sensorList[1]){ // sense stench
            makeStench(explorerY,explorerX);
        }
        else if(sensorList[0]){ // sense breeze
            makeBreeze(explorerY,explorerX);
        }
        else{ // didnt sense anything spot is guarenteed safe
            makeFactSafe(explorerY,explorerX); // make cell factSafe
        }

        if (sensorList[4]) { // sensed gold
            makeGlitter(explorerY,explorerX); // set glitter to true
        }
        if(sensorList[3]){

        }
    }

    private void unification(){
        // KnowledgeBase: unknown 0 , factSafe 1 , safe 2, factWumpus 3, wumpus 4, factPit 5, pit 6, obstacle 7, breeze 8, stench 9, glitter 10

        for(int i = 1; i < knowledgeBase.length - 1; i++) { //Loop through all knowledge bases for each postion in world
            for (int j = 1; j < knowledgeBase.length - 1; j++) {


                if (!isUnknown(i,j)) { //as long as we have visited a location
                    if (isFactSafe(i,j)) { //makes places next to factSafe just safe becuase we would have sensed a breeze or stench there
                        makeSafe(i, j - 1);
                        makeSafe(i, j + 1);
                        makeSafe(i - 1, j);
                        makeSafe(i + 1, j);
                    } else if (isStench(i,j) && isBreeze(i,j)){ //we sensed a stench and a breeze
                        makePit(i, j - 1); //make pit on cell below
                        makePossibleWumpus(i, j - 1); //make wumpus on cell below
                        makePit(i, j + 1);//make pit on cell above
                        makePossibleWumpus(i, j + 1); //make wunmpus on cell above
                        makePit(i - 1, j); // make pit on left cell
                        makePossibleWumpus(i - 1, j); // make wumpus on left cell
                        makePit(i + 1, j); // make pit on right cell
                        makePossibleWumpus(i + 1, j); //make wumpus on right cell
                    } else if (isStench(i,j)){ //we sensed a stench
                        makePossibleWumpus(i, j - 1); // make wumpus on cell below
                        makePossibleWumpus(i, j + 1); // make wumpus on cell above
                        makePossibleWumpus(i - 1, j); //make wumpus on cell left
                        makePossibleWumpus(i + 1, j); //make wumpus on cell right
                    } else if (isBreeze(i,j)){ //we sensed a breeze
                        makePit(i, j - 1); // make pit on cell below
                        makePit(i, j + 1); // make pit on cell above
                        makePit(i - 1, j); // make pit on left cell
                        makePit(i + 1, j); // make pit on right cell

                    } else if (isPit(i,j)){ // See if a pit is a guarenteed pit
                        //first check if any of the spots around it are guarenteed safe
                        if (isFactSafe(i, j - 1) || isFactSafe(i, j + 1) || isFactSafe(i - 1, j) || isFactSafe(i + 1, j)) {
                           makeSafe(i, j);
                           //next check if we have visited it
                        } else {
                            int c = 0;
                            if (isBreeze(i, j - 1)){
                                c++;
                            }
                            if (isBreeze(i, j + 1)){
                                c++;
                            }
                            if (isBreeze(i - 1, j)){
                                c++;
                            }
                            if (isBreeze(i + 1, j)){
                                c++;
                            }
                            if(c>1){
                                makeFactPit(i, j);
                            }
                        }

                    } else if (isWumpus(i,j)){ //see  if a wumpus is a guarenteed wumpus
                        //first check if any of the spots around it are guarenteed safe
                        if (isFactSafe(i, j - 1) || isFactSafe(i, j + 1) || isFactSafe(i - 1, j) || isFactSafe(i + 1, j)) {
                            makeSafe(i, j);
                            //next check if we have visited it
                        } else {
                            int c = 0;
                            if (isStench(i, j - 1)){
                                c++;
                            }
                            if (isStench(i, j + 1)){
                                c++;
                            }
                            if (isStench(i - 1, j)){
                                c++;
                            }
                            if (isStench(i + 1, j)){
                                c++;
                            }
                            if(c>1){
                                makeFactWumpus(i, j);
                            }
                        }
                    }
                }
            }
        }

    }


    private boolean[] sense() { // sense our current cell
            // sensor list: breeze, stench, bump, scream, gold

            boolean[] sensorList = new boolean[5];

            // breeze and stench at the cell to our left
            if(world[explorerY][explorerX-1] == 'p') {
                sensorList[0] = true;
            } else if(world[explorerY][explorerX-1] == 'w') {
                sensorList[1] = true;
            }
            // breeze and stench at the cell to our right
            if(world[explorerY][explorerX+1] == 'p') {
                sensorList[0] = true;
            } else if(world[explorerY][explorerX+1] == 'w') {
                sensorList[1] = true;
            }
            // breeze and stench at the cell north
            if(world[explorerY-1][explorerX] == 'p') {
                sensorList[0] = true;
            } else if(world[explorerY-1][explorerX] == 'w') {
                sensorList[1] = true;
            }
            // breeze and stench at the cell south
            if(world[explorerY+1][explorerX] == 'p') {
                sensorList[0] = true;
            } else if(world[explorerY+1][explorerX] == 'w') {
                sensorList[1] = true;
            }
            //sensed gold
            if(world[explorerY][explorerX] == 'g') {   // gold
                sensorList[4] = true;
            }
            //return our senses
            return sensorList;
        }

    private boolean turnLeft() {//turn left and update direction
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

    private boolean turnRight() { //turn right and update direction
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

    private boolean goForward() { //move forward
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

    private boolean shootArrow() { //shoot an arrow
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
        cost = 0;
        while (!reachedGold){
//            printWorld1();
//            System.out.println();
//            System.out.println("X: " + explorerX);
//            System.out.println("Y: " + explorerY);
            cost--;
            if(world[explorerY][explorerX] == 'g'){
                goldFoundReactive++;
//                System.out.println("Gold found");
                reachedGold = true;
                printWorld();
                cost = cost + 1000;
                return cost;

            }
            else if(world[explorerY][explorerX] == 'p'){
                pitFoundReactive++;
//                System.out.println("Death by pit");
                reachedGold = true;
                cost = cost - 10000;
                printWorld();
                return cost;

            }
            else if(world[explorerY][explorerX] == 'w'){
                wumpusFoundReactive++;
//                System.out.println("Death by wumpus");
                cost = cost - 10000;
                printWorld();
                return cost;
            }
            else if(cost <= -100000000){
//                System.out.println("Death by suicide");
                printWorld();
                explorerReactiveSuicide++;
                cost = cost - 10000;
                return cost;

            } else{
                world[explorerY][explorerX] = 'v';
//                printWorld();
//                System.out.println();
//                System.out.println("X: " + explorerX);
//                System.out.println("Y: " + explorerY);

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

//            ExplorerKBSuicide
                if(true){
                int move = (int) (Math.random() * 4);

                if (move == 0) {
//                    System.out.println("move North");
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
//                    System.out.println("move East");
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
//                    System.out.println("move south");
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
//                    System.out.println("move West");
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

    private void makeFactSafe(int y, int x){ // first order logic to set a place to guarenteed safe
        try{// catch out of bounds exception for edge of world
            knowledgeBase[y][x][0] = false;
            knowledgeBase[y][x][1] = true;
            knowledgeBase[y][x][2] = true;
            knowledgeBase[y][x][3] = false;
            knowledgeBase[y][x][4] = false;
            knowledgeBase[y][x][5] = false;
            knowledgeBase[y][x][6] = false;


        }catch(IndexOutOfBoundsException e){}

    }
    private void makeSafe(int y, int x){ //first order logic to set a decently safe spot but we are getting close to pit or wumpus
        try{// catch out of bounds exception for edge of world
            makeFactSafe(y,x);
            knowledgeBase[y][x][2] = true;
            knowledgeBase[y][x][1] = false;
        }catch(IndexOutOfBoundsException e){}
    }
    private void makeFactPit(int y, int x){ //first order to logic to set a guarenteed pit
        try{ // catch out of bounds exception for edge of world
            knowledgeBase[y][x][0] = false;
            knowledgeBase[y][x][5] = true;
            knowledgeBase[y][x][4] = false;
            knowledgeBase[y][x][6] = false;
        }catch(IndexOutOfBoundsException e){}
    }

    private void makePit(int y, int x){ // first order logic to set a suspected pit
        try{// catch out of bounds exception for edge of world
            if(!knowledgeBase[y][x][1] && !knowledgeBase[x][y][2] && !knowledgeBase[x][y][7]){
                knowledgeBase[y][x][0] = false;
                knowledgeBase[y][x][6] = true;
            }
        }catch(IndexOutOfBoundsException e){}
    }

    private void makeFactWumpus(int y, int x){ //first order logic to set a guarenteed wumpus
        try{// catch out of bounds exception for edge of world
            knowledgeBase[y][x][0] = false;
            knowledgeBase[y][x][3] = true;
            knowledgeBase[y][x][4] = false;
            knowledgeBase[y][x][6] = false;
        }catch(IndexOutOfBoundsException e){}
    }

    private void makePossibleWumpus(int y, int x){ //first order logic to set a suspected wumpus
        try{// catch out of bounds exception for edge of world
            if(!knowledgeBase[y][x][1] && !knowledgeBase[y][x][2] && !knowledgeBase[y][x][7]){
                knowledgeBase[y][x][0] = false;
                knowledgeBase[y][x][4] = true;
            }
        }catch(IndexOutOfBoundsException e){}
    }

    private void makeObstacle(int y, int x){ //first order logic to set an obstacle
        try{// catch out of bounds exception for edge of world
            knowledgeBase[y][x][0] = false;
            knowledgeBase[y][x][7] = true;
            knowledgeBase[y][x][4] = false;
            knowledgeBase[y][x][6] = false;
        }catch(IndexOutOfBoundsException e){}
    }

    private void makeGlitter(int y, int x){
        try {
            knowledgeBase[y][x][10] = true;
        }catch(IndexOutOfBoundsException e){}
    }
    private void makeBreeze(int y, int x){
        try {
            knowledgeBase[explorerY][explorerX][8] = true; // set breeze to true
            knowledgeBase[explorerY][explorerX][2] = true; //set safe to true
        }catch(IndexOutOfBoundsException e){}
    }
    private void makeStench(int y, int x){
        try {
            knowledgeBase[explorerY][explorerX][9] = true; //set stench to true
            knowledgeBase[explorerY][explorerX][2] = true; // set safe to true
        }catch(IndexOutOfBoundsException e){}
    }

    // KnowledgeBase: unknown 0 , factSafe 1 , safe 2, factWumpus 3, wumpus 4, factPit 5, pit 6, obstacle 7, breeze 8, stench 9, glitter 10
    private boolean isUnknown(int y, int x){ //check if we have visited a place
        try{
            return knowledgeBase[y][x][0]; //True if unknown
        }catch(IndexOutOfBoundsException e){ // catch out of bounds exception for edge of world
            return false;
        }
    }
    private boolean isFactSafe(int y, int x){ //check if a place is guarenteed safe
        try{// catch out of bounds exception for edge of world
            return knowledgeBase[y][x][1]; //True if guarenteed safe
        }catch(IndexOutOfBoundsException e){
            return false;
        }
    }
    private boolean isSafe(int y, int x){ //check if a place is safe but we are nearing a wumpus or pit
        try{
            return knowledgeBase[y][x][2]; //True if safe but we are nearing a wumpus or pit
        }catch(IndexOutOfBoundsException e){ // catch out of bounds exception for edge of world
            return false;
        }
    }
    private boolean isFactWumpus(int y, int x){ //check for guarenteed wumpus
        try{
            return knowledgeBase[y][x][3]; //True if Guarenteed wumpus
        }catch(IndexOutOfBoundsException e){ // catch out of bounds exception for edge of world
            return false;
        }
    }
    private boolean isWumpus(int y, int x){ //check for suspected wumpus
        try{
            return knowledgeBase[y][x][4]; //True if suspected wumpus
        }catch(IndexOutOfBoundsException e){ // catch out of bounds exception for edge of world
            return false;
        }
    }
    private boolean isFactPit(int y, int x){ //check for guarenteed pit
        try{
            return knowledgeBase[y][x][5]; //True if guarenteed pit
        }catch(IndexOutOfBoundsException e){ // catch out of bounds exception for edge of world
            return false;
        }
    }
    private boolean isPit(int y, int x){ //check for suspected pit
        try{
            return knowledgeBase[y][x][6]; //True if suspected pit
        }catch(IndexOutOfBoundsException e){ // catch out of bounds exception for edge of world
            return false;
        }
    }
    private boolean isOstacle(int y, int x){ //check for obstacle
        try{
            return knowledgeBase[y][x][7]; //True if obstacle
        }catch(IndexOutOfBoundsException e){ // catch out of bounds exception for edge of world
            return false;
        }
    }
    private boolean isBreeze(int y, int x){ //check for breeze
        try{
            return knowledgeBase[y][x][8]; //True if breeze detected
        }catch(IndexOutOfBoundsException e){ // catch out of bounds exception for edge of world
            return false;
        }
    }
    private boolean isStench(int y, int x){ //check for stench
        try{
            return knowledgeBase[y][x][9]; //True if stench detected
        }catch(IndexOutOfBoundsException e){ // catch out of bounds exception for edge of world
            return false;
        }
    }
    private boolean isGlitter(int y, int x){ //check for glitter
        try{
            return knowledgeBase[y][x][10]; //True if glitter detected
        }catch(IndexOutOfBoundsException e){ // catch out of bounds exception for edge of world
            return false;
        }
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

    public int getCellsExploredKB() {
        return cellsExploredKB;
    }

    public int getGoldFoundKB() {
        return goldFoundKB;
    }

    public int getWumpusFoundKB() {
        return wumpusFoundKB;
    }

    public int getWumpusKilledKB() {
        return wumpusKilledKB;
    }

    public int getExplorerKBSuicide() {
        return explorerKBSuicide;
    }

    public int getPitFoundKB() {
        return pitFoundKB;
    }

    public int getCost() {
        return cost;
    }
    public int getCostKB() {
        return costKB;
    }

}
