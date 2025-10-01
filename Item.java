
/**
 * Stephanie Lawal
 * K20012105
 * 
 * Item Class in Merlin's Apprentice.
 * 
 * This class allows us to monitor the attributes of items in the game,
 * all of which remain unchanged throughout the game's run. 
 *
 * @author Stephanie Lawal
 * 
 */
public class Item
{
    private String description;
    private int damage;
    private int protection;
    private int weight;
    private int usage;
    private boolean takeable;

    /**
     * Constructor for objects of class Item.
     * Initialises all attributes of the item.
     */
    public Item(String description, int damage, int protection, int weight, int usage, boolean takeable)
    {
        this.description = description;
        this.weight = weight;
        this.damage = damage;
        this.protection = protection;
        this.usage = usage;
        this.takeable = takeable;
    }

    /**
     * @return item weight
     */
    public int getWeight()
    {
        return weight;
    }
    
    /**
     * @return item description
     */
    public String getItemDescription()
    {
        return description;
    }
    
    /**
     * @return damage item inflicts
     */
    public int getDamage()
    {
        return damage;
    }
    
    /**
     * @return how many times the item can be used
     */
    public int getUsage()
    {
        return usage;
    }
    
    /**
     * @return the decerement of the item usage
     */
    public int reduceUsage()
    {
        usage--;
        return usage;
    }
    
    /**
     * @return protection that teh item provides player in the form of
     * additional health.
     */
    public int getProtection()
    {
        return protection;
    }
    
    /**
     * @return whether item can be taken by players
     */
    public boolean isTakeable()
    {
        return takeable;
    }
    
    /**
     * Prints all information on the item in string format
     */
    public void itemInfo()
    {
        System.out.println(description + "\n-------------------------------------------------------------------------");
        if(damage > 0) System.out.println("Damage: " + damage);
        if(protection > 0) System.out.println("Health Provided: " + protection);
        if(weight > 0) System.out.println("Weight: " + weight);
        System.out.println("Uses Left: " + usage);
    }
    
    /**
     * Prints all information about item in a room in string format
     */
    public void roomItemInfo()
    {
        if(damage > 0) System.out.println("Damage: " + damage);
        if(protection > 0) System.out.println("Health Provided: " + protection);
        if(weight > 0) System.out.println("Weight: " + weight);
        System.out.println("Uses: " + usage);
    }
}
