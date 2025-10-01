import java.util.Set;
import java.util.HashMap;

/**
 * Stephanie Lawal
 * K20012105
 * 
 * Class Room - a room in Merlin's Apprentice.
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits. Each room stores a reference to
 * all enemies in the room and items. For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * 
 * 
 */

public class Room 
{
    private String description;
    private boolean locked;
    private Item key;
    private HashMap<String, Enemy> enemies;
    private HashMap<String, Room> exits; // stores exits of this room.
    private HashMap<String, Item> items;
    
    /**
     * Create a room described "description" with exits, items, enemies and a key if
     * the room is locked.
     * Initially, there are no items, exits, enemies or key.
     * @param description The room's description.
     * @param locked Whether the room is locked or not
     */
    public Room(String description, boolean locked) 
    {
        this.locked = locked;
        this.description = description;
        this.key = key;
        enemies = new HashMap<>();
        exits = new HashMap<>();
        items = new HashMap<>();
    }

    /**
     * Define an exit for this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }
    
    /**
     * Define a key for this room if it is locked.
     * @param item The item object that can unlock the door
     */
    public void setKey(Item item)
    {
        key = item;
    }
    
    /**
     * Define an enemy for this room.
     * @param enemy The enemy object that is placed in this room.
     */
    public void addEnemy(Enemy enemy) {
        String enemyName = enemy.getName();
        enemies.put(enemyName, enemy);
    }
    
    /**
     * Removes an enemy from the room
     * @param enemy The enemy object in the room
     */
    public void removeEnemy(String enemyName) {
        enemies.remove(enemyName);
    }
    
    /**
     * @return key An Item object which is the key to the room
     */
    public Item getKey()
    {
        return key;
    }
    
    /**
     * @return whether or not room has enemies
     */
    public boolean noEnemies()
    {
        return enemies.isEmpty();
    }
    
    /**
     * @return HashMap of all enemies in room
     */
    public HashMap getEnemies()
    {
        return enemies;
    }
    
    /**
     * @param a string enemyName
     * @return Enemy object in the room. returns null if there is
     * no enemy with that name
     */
    public Enemy getEnemy(String enemyName)
    {
        return enemies.get(enemyName);
    }
    
    /**
     * @return list of enemies in the room in string form
     */
    public String getEnemiesString()
    {
        String returnString = "\nEnemies in this room:";
        Set<String> keys = enemies.keySet();
        for(String enemy : keys) {
            returnString += " " + enemy + ",";
        }
        returnString = returnString.substring(0, returnString.length() - 1);
        return returnString;
    }
    
    /**
     * @return list of enemies on its own
     */
    public String enemiesListed()
    {
        String returnString = "";
        Set<String> keys = enemies.keySet();
        for(String enemy : keys) {
            returnString += " " + enemy + ",";
        }
        returnString = returnString.substring(0, returnString.length() - 1);
        return returnString;
    }

    /**
     * Removes the preposition and verb from the room description
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        String newDescription = ((description.replace("in ", "")).replace("standing", "")).trim();
        return newDescription;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        String longDescription = "You are " + description + ".\n" + getExitString();
        if(!items.isEmpty()) {
            longDescription += "\n" + getItemsString();
        }
        return longDescription;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    public String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }
    
    /**
     * @return list of items in the room in string form
     */
    public String getItemsString()
    {
        String returnString = "Items:";
        Set<String> keys = items.keySet();
        for(String item : keys) {
            returnString += " " + item + ",";
        }
        returnString = returnString.substring(0, returnString.length() - 1);
        return returnString;
    }
    
    /**
     * @return locked A boolean attribute of the room which indicates if
     * the room is locked or not.
     */
    public boolean checkLocked()
    {
        return locked;
    }
    
    /**
     * Change room's locked attribute to false.
     */
    public void unlock()
    {
        locked = false;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * @return all room exits on their own
     */
    public String stringExits()
    {
        String returnString = "";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }
    
    /**
     * Add item to room
     * @param the name of the item and an Item object
     */
    public void addItem(String name, Item item)
    {
        items.put(name, item);
    }
    
    /**
     * Remove item with key 'name'
     * @param name of the item
     */
    public void removeItem(String name)
    {
        items.remove(name);
    }
    
    /**
     * @return item with key same as the item name. Returns null
     * if no key is found.
     */
    public Item getItem(String name)
    {
        return items.get(name);
    }
    
    /**
     * @return room item HashMap
     */
    
    public HashMap getAllItems()
    {
        return items;
    }
    
    /**
     * Checks if room has an item with same key as item name provided
     * @returns true or false
     */
    public boolean hasItem(String name) {
        return items.containsKey(name);
    }
}

