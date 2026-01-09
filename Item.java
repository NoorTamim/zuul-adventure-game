
/**
 * This class represents an item which may be put
 * in a room in the game of Zuul.
 * 
 * @author Noor Karabala
 */
public class Item
{
    // description of the item
    private String description;
    
    // weight of the item in kilograms
    private double weight;
    
    // name of item
    private String name;

    /**
     * Constructor for objects of class Item.
     * 
     * @param name The name of the item
     * @param description The description of the item
     * @param weight The weight of the item
     */
    public Item(String name, String description, double weight)
    {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    /**
     * Returns a description of the item, including its
     * name and description and weight.
     * 
     * @return  A description of the item
     */
    public String getDescription()
    {
        return name +": " + description + " that weighs " + weight + "kg.";
    }
    
    /**
     * Returns the of the name of the item.
     * 
     * @return  the name of the item
     */
    public String getName()
    {
        return this.name;
    }
}
