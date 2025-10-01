import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stephanie Lawal
 * K20012105
 * 
 *  This is Merlin's Apprentice
 *  
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, players and enemies, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * 
 * 
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room previousRoom;
    private Room teleportCloset;
    private Player player1;
    private Enemy goblin;
    private Enemy mordle;
    private Enemy hobgoblin;
    private Enemy guffin;
    private Enemy broileBeast;
    private Enemy morgana;
    /**
     * Creates the game and initialises the players and enemies
     */
    public Game() 
    {
        player1 = new Player(10);
        goblin = new Enemy("goblin", 10, 30, 30);
        mordle = new Enemy("mordle", 10, 40, 40);
        hobgoblin = new Enemy("hobgoblin", 10, 50, 50);
        guffin = new Enemy("guffin", 30, 200, 50);
        broileBeast = new Enemy("broile beast", 40, 300, 60);
        morgana = new Enemy("morgana", 100, 2400, 0);
        createRooms();
        parser = new Parser();
    }

    /**
     * Creates all the rooms and starting items featured throughout the game as well as
     * adding all the rooms to an array in the game's Player object.
     * Each room is instantiated with a description and a boolean which indicated whether the door
     * to the room is locked.
     * Each item has a description, its damage, protection (which is the health it provides the player),
     * weight and the number of times the item can be used.
     * This method also puts some starting items into the rooms and assigns others to enemies.
     */
    private void createRooms()
    {
        Room foyer, greatHall, kitchen, library, upperLibrary, teleportCloset, upstairsHallway, chamber, arsenal, merlinsStudy, basementHallway, cell1, cell2, broileCell;
        
        // create the rooms
        foyer = new Room("in the castle foyer", false);
        greatHall = new Room("in the Great Hall", false);
        kitchen = new Room("in the castle kitchen", false);
        library = new Room("in Merlin's library", false);
        upperLibrary = new Room("in the upper floor of Merlin's library", false);
        teleportCloset = new Room("in teleport closet", false);
        upstairsHallway = new Room("standing in the upstairs hallway", false);
        chamber = new Room("in the Chamber Room", false);
        arsenal = new Room("in Merlin's arsenal", true);
        merlinsStudy = new Room("in Merlin's study", true);
        basementHallway = new Room("standing in the basement hallway", true);
        cell1 = new Room("standing in cell 1", true);
        cell2 = new Room("standing in cell 2", true);
        broileCell = new Room("standing in the Broile beast's cell", true);
        
        //add all rooms to player1 list
        player1.addRoom(0, foyer);
        player1.addRoom(1, greatHall);
        player1.addRoom(2, kitchen);
        player1.addRoom(3, library);
        player1.addRoom(4, upperLibrary);
        player1.addRoom(5, upstairsHallway);
        player1.addRoom(6, chamber);
        player1.addRoom(7, arsenal);
        player1.addRoom(8, merlinsStudy);
        player1.addRoom(9, basementHallway);
        player1.addRoom(10, cell1);
        player1.addRoom(11, cell2);
        player1.addRoom(12, broileCell);
        
        // initialise room exits
        foyer.setExit("up", upstairsHallway);
        foyer.setExit("east", greatHall);
        foyer.setExit("west", kitchen);

        greatHall.setExit("north", library);
        greatHall.setExit("west", foyer);
        
        library.setExit("south", greatHall);
        library.setExit("up", upperLibrary);
        
        upperLibrary.setExit("south", merlinsStudy);
        upperLibrary.setExit("west", upstairsHallway);
        upperLibrary.setExit("down", library);
        upperLibrary.setExit("east", teleportCloset);
        
        upstairsHallway.setExit("north", chamber);
        upstairsHallway.setExit("west", arsenal);
        upstairsHallway.setExit("down", foyer);
        upstairsHallway.setExit("east", upperLibrary);
        
        chamber.setExit("south", upstairsHallway);
        
        arsenal.setExit("east", upstairsHallway);
        
        merlinsStudy.setExit("north", upperLibrary);
        
        kitchen.setExit("east", foyer);
        kitchen.setExit("down", basementHallway);
        
        basementHallway.setExit("north", cell1);
        basementHallway.setExit("east", cell2);
        basementHallway.setExit("west", broileCell);
        basementHallway.setExit("up", kitchen);
        
        cell1.setExit("south", basementHallway);
        cell2.setExit("west", basementHallway);
        broileCell.setExit("east", basementHallway);

        //player starts in the foyer
        player1.setCurrentRoom(foyer);
        player1.setPreviousRoom(foyer);
        
        Item broomstick, morticaFlower, armour, spellBook, basementKey, arsenalKey, glassShard, brokenMirror;
        Item fireTorch, merlinsStatue, goldenDagger, frogLegs, rottingApple, broileSaliva, mordusSpell, goatsEye, cloth;
        Item guffinWings, pestleMortar, wine, merlinsWand;
        
        //create items
        broomstick = new Item("An old broomstick, caked in dust... still packs a punch", 20, 0, 30, 30, true);
        morticaFlower = new Item("The Mortica flower is a common plant used as the base of all potions and spells", 5, 3, 1, 3, true);
        armour = new Item("An enchanted armour covered in purple glowing veins. What creature would require an armour this powerful?...", 0, 100, 70, 1, true);
        spellBook = new Item("Merlin's spell book is a large, pristine, dark leather book adorned with ornate patterns. \nThis book will allow you to automatically combine spell ingredients and make stronger attacks", 25, 50, 40, 20, true);
        basementKey = new Item("The key to the basement...", 0, 0, 4, 1, true);
        arsenalKey = new Item("The key to the arsenal...", 0, 0, 4, 1, true);
        glassShard = new Item("A shard of glass. The edge is rounded", 2, 0, 1, 3, true);
        brokenMirror = new Item("A round, handheld, broken mirror", 0, 0, 10, 1, true);
        fireTorch = new Item("A fire torch", 20, 0, 20, 3, true);
        merlinsStatue = new Item("Seeing Merlin's face you remember! The wand is in the study, but you need the spell ingredients to unlock it...", 0, 0, 1000, 0, false);
        goldenDagger = new Item("A dagger made of solid gold. You bring it to you ear and it hums quietly", 50, 0, 50, 10, true);
        frogLegs = new Item("Frozen frog legs. Well that's disgusting", 0, 0, 2, 3, true);
        rottingApple = new Item("An apple, blackened and fragile", 0, 0, 2, 3, true);
        broileSaliva = new Item("Broile's Saliva", 0, 0, 2, 1, true);
        mordusSpell = new Item("The Mordus Spell is a potent spell known to break even the strongest of item and room protection spells", 0, 0, 0, 1, true);
        goatsEye = new Item("Goat's eye. Okay, this may be worse than frog legs", 0, 0, 2, 3, true);
        cloth = new Item("A thick, square-shaped cloth", 0, 0, 3, 1, true);
        guffinWings = new Item("Guffin wings", 0, 0, 5, 1, true);
        wine = new Item("A bottle of wine", 3, 0, 7, 1, true);
        merlinsWand = new Item("You should not be able to wield Merlin's wand yet here it is in your bare hands", 800, 600, 20, 3, true);
        
        //add items to rooms
        foyer.addItem("broomstick", broomstick);
        foyer.addItem("mortica flower", morticaFlower);
        
        greatHall.addItem("fire torch", fireTorch);
        greatHall.addItem("merlin's statue", merlinsStatue);
        
        library.addItem("broken mirror", brokenMirror);
        
        upperLibrary.addItem("spell book", spellBook);
        
        chamber.addItem("glass shard", glassShard);
        chamber.addItem("arsenal key", arsenalKey);
        chamber.addItem("cloth", cloth);
        
        arsenal.addItem("enchanted armour", armour);
        arsenal.addItem("golden dagger", goldenDagger);
        arsenal.addItem("basement key", basementKey);
        
        merlinsStudy.addItem("merlin's wand", merlinsWand);
        
        kitchen.addItem("frog legs", frogLegs);
        kitchen.addItem("rotting apple", rottingApple);
        kitchen.addItem("goat's eye", goatsEye);
        
        basementHallway.addItem("wine", wine);
        
        //initialise room keys
        arsenal.setKey(arsenalKey);
        basementHallway.setKey(basementKey);
        cell1.setKey(basementKey);
        cell2.setKey(basementKey);
        broileCell.setKey(basementKey);
        merlinsStudy.setKey(mordusSpell);
        
        //initialise hidden enemy items
        guffin.attachItem("guffin wings", guffinWings);
        broileBeast.attachItem("broile's saliva", broileSaliva);
        
        //initialise enemy start rooms
        goblin.setCurrentRoom(foyer);
        foyer.addEnemy(goblin);
        mordle.setCurrentRoom(cell2);
        cell2.addEnemy(mordle);
        hobgoblin.setCurrentRoom(chamber);
        chamber.addEnemy(hobgoblin);
        guffin.setCurrentRoom(kitchen);
        kitchen.addEnemy(guffin);
        broileBeast.setCurrentRoom(broileCell);
        broileCell.addEnemy(broileBeast);
    }

    /**
     *  The main play routine loops until end of play which is indicated by the boolean 'finished'.
     *  The game terminates if the player's health is 0 or below. Using the player's hasWand and getCurrentRoom
     *  methods, the foyer is cleared of all enemies and the final boss enemy is added for the player
     *  to battle. The game also terminates if the enemy's health is below 0.
     *  This routine outputs 3 different messages upon termination.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            if(player1.getHealth() <= 0) {
                finished = true;
            }
            else if(player1.hasWand() && player1.getCurrentRoom().getShortDescription().contains("foyer")) {
                player1.getCurrentRoom().getEnemies().clear();
                morgana.setCurrentRoom(player1.getCurrentRoom());
                player1.getCurrentRoom().addEnemy(morgana);
                if(morgana.getHealth() > 0 && player1.getHealth() > 0) {
                    Command command = parser.getCommand();
                    finished = processCommand(command);
                }
                else {
                    finished = true;
                }
            }
            else {
                Command command = parser.getCommand();
                finished = processCommand(command);
            }
        }
        if(player1.getHealth() <= 0) {
            System.out.println("\nOh no! You were defeated!");
        }
        else if(player1.hasWand() && player1.getHealth() > 0) {
            System.out.print("Congratulations! You won!");
        }
        else {
            System.out.println("Thank you for playing. Good bye.");
        }
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Merlin's Apprentice! \n----------------------------");
        System.out.println("The witch Morgana has taken your family and entire village hostage!");
        System.out.println("Morgana has travelled far and wide, absorbing the life force from magical beings.");
        System.out.println("Even Merlin could not defeat her with his own magin and as a result, he too is held captive!");
        System.out.println("Merlin has trusted you, a young wizard, to find retrieve the last magical item that can");
        System.out.println("defeat Morgana - his wand. Your magic alone will not be enough to defeat Morgana;");
        System.out.println("you will need the assistance of other magical items in order to increase your health and power.");
        System.out.println("Merlin's castle has been infiltrated by dark creatures who will try to stop you in your search");
        System.out.println("for the wand. \n\n Once Morgana senses the wand, she will try to stop you. You must hurry! \n");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player1.getCurrentRoom().getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            player1.goRoom(command);
        }
        else if (commandWord.equals("back")) {
            player1.goBack(command);
        }
        else if (commandWord.equals("take")) {
            player1.takeItem(command);
        }
        else if (commandWord.equals("drop")) {
            player1.dropItem(command);
        }
        else if (commandWord.equals("attack")) {
            player1.attackEnemy(command);
        }
        else if (commandWord.equals("use")) {
            player1.useItem(command);
        }
        else if (commandWord.equals("see")) {
            player1.see(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out a short synopsis of what the objective is and prints command words.
     */
    private void printHelp() 
    {
        System.out.println("The witch Morgana has taken your entire family and your village captive");
        System.out.println("You wander around Merlin's castle in search of his wand");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
