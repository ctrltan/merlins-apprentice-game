import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Iterator;
import java.util.Set;

/**
 * Stephanie Lawal
 * K20012105
 * 
 * Enemy Class in Merlin's Apprentice.
 * 
 * This class controls the enemies throughout the game and allows us to monitor,
 * modify and set attribute contents of enemies.
 *
 * @author Stephanie Lawal
 * 
 */
public class Enemy
{
    private Room currentRoom;
    private String name;
    private int health;
    private int damage;
    private int lifeAwarded;
    private HashMap<String, Item> hiddenItems;
    
    /**
     * Constructor for objects of class Enemies. All enemies have a
     * name, damage, health and life awarded. Hidden items are added
     * when game is created.
     */
    public Enemy(String name, int damage, int health, int lifeAwarded)
    {
        hiddenItems = new HashMap<>();
        this.name = name;
        this.damage = damage;
        this.health = health;
        this.lifeAwarded = lifeAwarded;
    }
    
    /**
     * @return the enemy's hidden items in the form of a string
     */
    public String getHiddenItems()
    {
        String returnString = "";
        Set<String> allHidden = hiddenItems.keySet();
        for(String hidden : allHidden) {
            returnString += " " + hidden + ",";
        }
        returnString = returnString.substring(0, returnString.length() - 1);
        return returnString;
    }

    /**
     * @return enemy damage
     */
    public int getDamage()
    {
        return damage;
    }
    
    /**
     * @return enemy name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * @return enemy health
     */
    public int getHealth()
    {
        return health;
    }
    
    /**
     * Sets new health for the the enemy and prints a statement
     * displaying the health.
     * @param value An integer to reduce the health by
     */
    public void reduceHealth(int value)
    {
        health -= value;
        System.out.println(name + " health " + health);
    }
    
    /**
     * @return lifeAwarded attribute
     */
    public int lifeAwarded()
    {
        return lifeAwarded;
    }
    
    /**
     * @return enemy's current room
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }
    
    /**
     * Set the current enemy room
     * @param room object
     */
    public void setCurrentRoom(Room room)
    {
        currentRoom = room;
    }
    
    /**
     * Add an item to the enemy HashMap
     * @param name of the item and the reference for the item object
     */
    public void attachItem(String itemName, Item item)
    {
        hiddenItems.put(itemName, item);
    }
    
    /**
     * Take a string of exits and chooses one at random. If the
     * exit is locked or has an enemy in it then choose a new exit.
     * Set a new current room for the enemy and remove it from the previous
     * room.
     */
    public void move()
    {
        String[] exits = currentRoom.stringExits().trim().split(" ");
        Random rand = new Random();
        int indx = rand.nextInt(exits.length);
        Room nextRoom = currentRoom.getExit(exits[indx]);
        
        if(nextRoom.checkLocked() == true || !nextRoom.noEnemies()) {
            indx = rand.nextInt(exits.length);
            nextRoom = currentRoom.getExit(exits[indx]);
        }
        
        nextRoom.addEnemy(currentRoom.getEnemy(name));
        currentRoom.removeEnemy(name);
        currentRoom = nextRoom;
    }
    
    /**
     * A player's health is reduced when enemy is engaging in an attack
     * @param playerHealth which is a player's health obtained from Player itself.
     * @return the player's health parameter
     */
    public int attack(int playerHealth)
    {
        playerHealth -= damage;
        return playerHealth;
    }
    
    /**
     * All items in enemy HashMap are put in current room
     */
    private void drop() 
    {
        currentRoom.getAllItems().putAll(hiddenItems);
    }
    
    /**
     * Drops items if the enemy has by calling private method
     * drop().
     * Clear all the enemy items and remove it from the current room.
     */
    public void death()
    {
        if(!hiddenItems.isEmpty()) {
            System.out.println(name + " dropped:" + getHiddenItems());
            drop();
        }
        currentRoom.removeEnemy(name);
        hiddenItems.clear();
    }
}
