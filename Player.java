import java.util.HashMap;
import java.util.Stack;
import java.util.Set;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;

/**
 * Stephanie Lawal
 * K20012105
 * 
 * Player Class in Merlin's Apprentice
 * 
 * This class checks player's drawstring, teleports player, sets and modifies
 * attributes and handles commands of player such as using items and moving
 * through rooms.
 *
 * @author Stephanie Lawal
 * 
 */
public class Player
{
    private final int drawstringCapacity = 200;
    private int currentCapacity;
    private int health = 100;
    private int damage;
    private ArrayList<Room> allRooms;
    private Room currentRoom;
    private Room previousRoom;
    private Stack<Room> visitedRooms;
    private HashMap<String, Item> drawstring;

    /**
     * Create a player with damage. Remaining attributes are set
     * when the game created.
     */
    public Player(int damage)
    {
        this.damage = damage;
        allRooms = new ArrayList<>();
        visitedRooms = new Stack<>();
        drawstring = new HashMap<>();
    }

    /** 
     * Try to go to one direction. 
     * If there is an exit, enter the new room.
     * If the room has no exits then an error mesaage is printed.
     * If the player wants to move out of a room, noEnemies() method
     * is called. If false then an attack message is returned with the enemy names.
     * If the room is locked then hasKey() is called to see if player
     * has the key that unlocks this room before entering.
     * Moves the enemies.
     * 
     * @param command User command entered in the terminal
     */
    public void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord().trim().toLowerCase();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else if(!currentRoom.noEnemies()) {
            System.out.print("You're being attacked!");
            System.out.println(currentRoom.getEnemiesString());
            return;
        }
        else if(nextRoom.checkLocked()) {
            hasKey(nextRoom);
        }
        else {
            previousRoom = currentRoom;
            if(nextRoom.getShortDescription().contains("teleport")) {
                System.out.println("You are being teleported...");
                teleport();
                System.out.println(currentRoom.getLongDescription());
            }
            else {
                currentRoom = nextRoom;
                visitedRooms.push(previousRoom);
                System.out.println(currentRoom.getLongDescription());
            }
            enemyMoveCheck();
        }
    }
    
    /**
     * Try to take the item in the command. If the item is not in the room
     * then print error message. 
     * Otherwise, checks if item is takeable, will fit in the drawstring and whether
     * the player meets conditions to take the spell book. Otherwise, add
     * increase current capacity of drawstring and health. 
     * 
     * Item is removed from the drawstring when taken.
     * 
     * @param command User command entered in the terminal
     */
    public void takeItem(Command command)
    {
        if(!command.hasSecondWord()) {
            System.out.println("Take what?");
            return;
        }
        
        String item = command.concatCommand(command);
        
        if (!currentRoom.hasItem(item)){
            System.out.println("No sign of that item here");
            return;
        }
        
        //temporary capacity to check what the drawstring capacity would become if the item is taken
        int newCapacity = currentCapacity + currentRoom.getItem(item).getWeight();
        if(currentRoom.getItem(item).isTakeable()) {
            if(newCapacity > drawstringCapacity) {
                System.out.println("Your drawstring is full!");
            }
            else if(item.equals("spell book") && health < 130) {
                System.out.print("You do not have enough power to use the spell book!");
                return;
            }
            else {
                currentCapacity = newCapacity;
                health += currentRoom.getItem(item).getProtection();
                drawstring.put(item, currentRoom.getItem(item));
                currentRoom.removeItem(item);
                System.out.println(item + " has been added to you drawstring!");
                if(drawstring.get(item).getProtection() > 0) System.out.println("+" + drawstring.get(item).getProtection() + " health");
                if(item.equals("spell book")) {
                    damage = drawstring.get(item).getDamage();
                    System.out.println("You can now inflict " + damage + " points of damage");
                }
                combos(item);
            }
        }
        else if(currentRoom.getItem(item).getWeight() > drawstringCapacity) {
            System.out.print(currentRoom.getItem(item).getItemDescription());
        }
        else {
            System.out.println("You cannot take this item");
        }
    }
    
    /**
     * 
     */
    public void dropItem(Command command)
    {
        if(!command.hasSecondWord()) {
            System.out.println("Drop what?");
            return;
        }
        
        String item = command.concatCommand(command);
        
        if(!drawstring.containsKey(item))
        {
            System.out.println("This item is not in your drawstring!");
            return;
        }
        else {
            currentCapacity -= drawstring.get(item).getWeight();
            health -= drawstring.get(item).getProtection();
            currentRoom.addItem(item, drawstring.get(item));
            drawstring.remove(item);
            System.out.println("You dropped " + item);
        }
    }
    
    /**
     * If there is an enemy of the same name as stated in the command
     * then enemy attribute lifeAwarded is stored and enemy health is reduced via
     * reduceHealth() method.
     * Health is printed along with enemy health fron reduceHealth() method.
     * If enemy health is still greater than 0 then enemy attacks.
     * Otherwise, player defeats enemy and receives extra health.
     * If no enemy in command is not in the room but a different one is then
     * error message is printed and the enemy attacks
     */
    public void attackEnemy(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Attack what?");
            Set<String> all = currentRoom.getEnemies().keySet();
            for(String enemies : all) {
                health = currentRoom.getEnemy(enemies).attack(health);
                System.out.println("Your health: " + health);
            }
            return;
        }
        
        String enemyName = command.concatCommand(command);
        if(currentRoom.getEnemy(enemyName) != null) {
            int potentialHealth = currentRoom.getEnemy(enemyName).lifeAwarded();
            currentRoom.getEnemy(enemyName).reduceHealth(damage);
            
            if(currentRoom.getEnemy(enemyName).getHealth() > 0) {
                health = currentRoom.getEnemy(enemyName).attack(health);
                System.out.println("Your health: " + health);
            }
            else {
                health += potentialHealth;
                if(currentRoom.getEnemy(enemyName).lifeAwarded() == 0) {
                    System.out.println("You defeated " + enemyName + "!");
                    currentRoom.getEnemy(enemyName).death();
                }
                else {
                    System.out.println("You defeated " + enemyName + "! You have been rewarded with +" + currentRoom.getEnemy(enemyName).lifeAwarded() + " health!");
                    currentRoom.getEnemy(enemyName).death();
                }
            }
        }
        else
        {
            System.out.println("Wrong enemy!");
            Set<String> all = currentRoom.getEnemies().keySet();
            for(String enemies : all) {
                health = currentRoom.getEnemy(enemies).attack(health);
                System.out.println("Your health: " + health);
            }
        }
    }
    
    /**
     * Checks if the player has an item in from the command in the drawstring.
     * If the drawtstring item returns null then prints error message.
     * Try to use item from drawstring.
     * If the item is a looking glass then it can be used. If there are 
     * no enemies in the room then return error mesaage. If item does not
     * do damage to enemy then enemy attacks the player.
     * Reduces item usage and does not allow item to be used if usage is at 0.
     * Item can be used to attack enemy in the room.
     * 
     * @param command from terminal
     */
    public void useItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Use what?");
            return;
        }
        
        String item = command.concatCommand(command);
        if(drawstring.get(item) == null) {
            System.out.println("You do not have that item");
        }
        else if(currentRoom.noEnemies() || drawstring.get(item).getDamage() == 0) {
            if(item.equals("looking glass")) {
                lookNearby();
            }
            else if(!currentRoom.noEnemies()) {
                System.out.println("Well that didn't do anything...");
                Set<String> all = currentRoom.getEnemies().keySet();
                for(String enemies : all) {
                    if(currentRoom.getEnemy(enemies).getHealth() > 0) {
                    health = currentRoom.getEnemy(enemies).attack(health);
                    System.out.println("Your health: " + health);
                    }
                }
            }
            else {
                System.out.println("Well that didn't do anything...");
            }
        }
        else if(drawstring.get(item) == null) {
            System.out.println("You do not have that item");
        }
        else if(drawstring.get(item).getUsage() == 0) {
            System.out.println("You've used up this item!");
            drawstring.remove(item);
        }
        else {
            Set<String> all = currentRoom.getEnemies().keySet();
            for(String enemies : all) {
                int potentialHealth = currentRoom.getEnemy(enemies).lifeAwarded();
                currentRoom.getEnemy(enemies).reduceHealth(drawstring.get(item).getDamage());
                if(currentRoom.getEnemy(enemies).getHealth() > 0) {
                    health = currentRoom.getEnemy(enemies).attack(health);
                    System.out.println("Your health: " + health);
                }
                else {
                    health += potentialHealth;
                    System.out.println("You defeated " + enemies + "! You have been rewarded with +" + currentRoom.getEnemy(enemies).lifeAwarded() + " health!");
                    currentRoom.getEnemy(enemies).death();
                }
            }
            drawstring.get(item).reduceUsage();
        }
    }
    
    /**
     * Takes the attribute from the command and prints its details.
     * The default prints either the info of item's in the room and in the drawstring.
     * Otherwise, prints error message.
     * 
     * @param command from terminal
     */
    public void see(Command command)
    {
        if(!command.hasSecondWord()) {
            System.out.println("See what?");
            return;
        }
        
        String toView = command.concatCommand(command);
        
        switch(toView) {
            case "health":
                System.out.println(health);
                break;
            case "damage":
                System.out.println(damage);
                break;
            case "drawstring":
            case "inventory":
                drawstringString();
                System.out.println(drawstringCapacity - currentCapacity + " space left");
                break;
            default:
                if(currentRoom.getAllItems().containsKey(toView)) {
                    currentRoom.getItem(toView).roomItemInfo();
                }
                else if(drawstring.containsKey(toView)) {
                    drawstring.get(toView).itemInfo();
                }
                else {
                    System.out.println("You can't see that!");
                } 
                break;
        }
    }
    
    /**
     * Checks if there are any rooms to go back to and that there are no
     * enemies in the room.
     * Pops the previous room from the stack and moves the enemyies.
     */
    public void goBack(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("You can only go back");
            return;
        }
        else if(visitedRooms.empty())
        {
            System.out.println(currentRoom.getLongDescription());
        }
        else if(!currentRoom.noEnemies()) {
            System.out.print("You're being attacked!");
            System.out.println(currentRoom.getEnemiesString());
            return;
        }
        else {
           currentRoom = visitedRooms.pop();
           previousRoom = currentRoom; 
           System.out.println(currentRoom.getLongDescription());
           Iterator<Room> all = allRooms.iterator();
           enemyMoveCheck();
        }
        
    }
    
    /**
     * Generates random integer from 0 to the number of rooms in the game,
     * excluding the teleport room.
     * Returns a room and checks if the room is locked, if true then
     * method calls itself to find another room.
     * Otherwise, the player can now enter the room and the previous room is stored
     */
    private void teleport()
    {
        Random rand = new Random();
        int roomNum = rand.nextInt(allRooms.size());
        currentRoom = allRooms.get(roomNum);
        if(currentRoom.checkLocked() && drawstring.containsValue(currentRoom.getKey()) == false) {
            teleport();
        }
        else {
            visitedRooms.push(previousRoom);
        }
    }
    
    /**
     * Iterates over all rooms in the game and then iterates over
     * all enemies in each room.
     * If the enemy is null then exit for loop.
     * Otherwise, move the enemy
     */
    private void enemyMoveCheck()
    {
        Iterator<Room> all = allRooms.iterator();
        while(all.hasNext()) {
            Room room = all.next();
            if(!room.noEnemies()) {
                String[] enemies = (room.enemiesListed()).replace(",", "").trim().split(" ");
                for(String enemy : enemies) {
                    try {
                        room.getEnemy(enemy).move();
                    }
                    catch (Exception e) {
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * @return player damage
     */
    public int getDamage()
    {
        return damage;
    }
    
    /**
     * @return player health
     */
    public int getHealth()
    {
        return health;
    }
    
    /**
     * @return String of space remaining in player's drawstring
     */
    public String getCurrentCapacity()
    {
        return "You have " + currentCapacity + " units of space in your drawstring";
    }
    
    /**
     * @return player's current room
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }
    
    /**
     * Sets player's curren room to room in parameter
     * 
     * @param Room object room
     */
    public void setCurrentRoom(Room room)
    {
        currentRoom = room;
    }
    
    /**
     * @return player's previous room
     */
    public Room getPreviousRoom()
    {
        return previousRoom;
    }
    
    /**
     * Sets player's previous room to room in parameter
     * 
     * @param Room object room
     */
    public void setPreviousRoom(Room room)
    {
        previousRoom = room;
    }
    
    /**
     * @return allRooms ArrayList
     */
    public ArrayList getAllRooms()
    {
        return allRooms;
    }
    
    /**
     * Adds a room to the allRooms ArrayList
     * 
     * @param number which is index for room, room which is the Room object
     */
    public void addRoom(int number, Room room)
    {
        allRooms.add(number, room);
    }
    
    /**
     * Prints all items in drawstring in string format
     * If drawstring is empty, then prints error message
     */
    private void drawstringString()
    {
        if(!drawstring.isEmpty()) {
            String itemString = "Drawstring:";
            Set<String> names = drawstring.keySet();
            for(String name : names) {
                itemString += " " + name + ",";
            }
            System.out.println(itemString.substring(0, itemString.length() - 1));
        }
        else {
            System.out.println("Your drawstring is empty!");
        }
    }
    
    /**
     * @return boolean indicating if spell book object is in player drawstring
     */
    private boolean hasSpellBook()
    {
        return drawstring.containsKey("spell book");
    }
    
    /**
     * @return boolean indicating if merlin's wand is in player's drawstring
     */
    public boolean hasWand()
    {
        return drawstring.containsKey("merlin's wand");
    }
    
    /**
     * Checks if the user has all the objects needed to instantiate new
     * items.
     * If a combination can be made, then object is instantiated and case items are removed from
     * the player's drawstring.
     * 
     * @param the name of an item
     */
    public void combos(String item)
    {
        switch(item) {
            case "guffin wings":
            case "goat's eye":
            case "rotting apple":
            case "frog legs":
            case "broile's saliva":
            case "spell book":
                if(drawstring.containsKey("guffin wings") && drawstring.containsKey("goat's eye") && drawstring.containsKey("rotting apple") && drawstring.containsKey("frog legs") && drawstring.containsKey("broile's saliva") && drawstring.containsKey("spell book")) {
                    Item mordusSpell = new Item("The Mordus Spell is a potent spell known to break even the strongest of item and room protection spells", 0, 0, 0, 1, true);
                    drawstring.put("mordus spell", mordusSpell);
                    System.out.println("mordus spell has been added to your drawstring");
                    if(mordusSpell.getProtection() > 0) System.out.println("+" + mordusSpell.getProtection() + " health");
                    currentCapacity -= drawstring.get("guffin wings").getWeight();
                    drawstring.remove("guffin wings");
                    currentCapacity -= drawstring.get("goat's eye").getWeight();
                    drawstring.remove("goat's eye");
                    currentCapacity -= drawstring.get("rotting apple").getWeight();
                    drawstring.remove("rotting apple");
                    currentCapacity -= drawstring.get("frog legs").getWeight();
                    drawstring.remove("frog legs");
                }
                break;
            case "cloth":
            case "fire torch":
            case "wine":
                if(drawstring.containsKey("cloth") && drawstring.containsKey("fire torch") && drawstring.containsKey("wine")) {
                    Item flamingBottle = new Item("A bottle of wine set on fire. Be careful with this!", 100, 0, 7, 1, true);
                    drawstring.put("flaming bottle", flamingBottle);
                    currentCapacity += flamingBottle.getWeight();
                    System.out.println("flaming bottle has been added to your drawstring");
                    currentCapacity -= drawstring.get("cloth").getWeight();
                    drawstring.remove("cloth");
                    currentCapacity -= drawstring.get("fire torch").getWeight();
                    drawstring.remove("fire torch");
                    currentCapacity -= drawstring.get("wine").getWeight();
                    drawstring.remove("wine");
                }
                break;
            case "glass shard":
            case "broken mirror":
                if(drawstring.containsKey("glass shard") && drawstring.containsKey("broken mirror")) {
                    Item lookingGlass = new Item("In this mirror, you can see a blurred bird's eye view of the very castle you stand in. Strange shadows move through the building...", 0, 0, 30, 10, true);
                    drawstring.put("looking glass", lookingGlass);
                    currentCapacity += lookingGlass.getWeight();
                    System.out.println("looking glass has been added to your drawstring");
                    currentCapacity -= drawstring.get("glass shard").getWeight();
                    drawstring.remove("glass shard");
                    currentCapacity -= drawstring.get("broken mirror").getWeight();
                    drawstring.remove("broken mirror");
                }
                break;
        }
    }
    
    /**
     * Iterates over exits obtained from stringExits() in Room class.
     * If enemies are in the room nearby they it prints the string:
     * [exit]
     * ----------------
     * [enemy]
     */
    private void lookNearby()
    {
        String[] exits = currentRoom.stringExits().trim().split(" ");
        for(String exit : exits) {
            if(!(currentRoom.getExit(exit)).noEnemies()) {
                System.out.println(exit + "\n------------------- " + currentRoom.getExit(exit).getEnemiesString());
            }
            else {
                System.out.println(exit + "\n-----------------" + "\nNo enemies nearby");
            }
            
        }
    }
    
    /**
     * Calls the getKey() method of a room object and returns either an
     * object of type Item or 'null'
     * If there is no key then print an error message.
     * If Item returned check player drawstring for the same item.
     * If player has item then unlock the room and go into the room.
     * Otherwise, an error message is printed.
     */
    private void hasKey(Room nextRoom)
    {
        if(nextRoom.getKey() != null) {
            if(drawstring.containsValue(nextRoom.getKey())) {
                nextRoom.unlock();
                System.out.println(nextRoom.getShortDescription() + " has been unlocked");
                previousRoom = currentRoom;
                currentRoom = nextRoom;
                visitedRooms.push(previousRoom);
                System.out.println(currentRoom.getLongDescription());
            }
            else {
                System.out.println("You can't open " + nextRoom.getShortDescription() + " door!");
            }
        }
        else {
            System.out.println("You can't open " + nextRoom.getShortDescription() + " door!");
        }
    }
}
