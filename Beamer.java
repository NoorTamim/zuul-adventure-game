
/**
 * The Beamer class represents a device that can be charged and fired to transport the player.
 * It is a subclass of the Item class and adds specific functionality for charging and firing the beamer.
 *
 * @author Noor Karabala
 */
public class Beamer extends Item
{
    
    private boolean isCharged;
    private Room chargingRoom;
    
    /**
     * Constructor for objects of class Beamer
     */
    public Beamer(String name, String description, double weight)
    {
        super(name, description, weight);
        this.isCharged = false;
        this.chargingRoom = null;
    }

    /**
     * Charges the beamer with the current room.
     *
     * @param currentRoom The room to charge the beamer in.
     * 
     * @return true if the beamer was successfully charged, false if it was already charged.
     */
    public boolean charge(Room currentRoom)
    {
        if(isCharged){
            System.out.println("Beamer is already charged!");
            return false;
        }else{
            this.chargingRoom = currentRoom;
            isCharged = true;
            return true;
        }
    }
    
    /**
     * Returns the current charge status of the beamer. 
     *
     * @return true if the beamer is charged, false otherwise.
     */
    public boolean getIsCharged()
    {
        return this.isCharged;
    }
    
    /**
     * Fires the beamer, transporting the player to the room it was charged in.
     *
     * @return The room the beamer was charged with, or null if the beamer is not charged.
     */
    public Room fire()
    {
        if (isCharged) {
            
            Room destination = this.chargingRoom;
            this.chargingRoom = null;
            this.isCharged = false;
            return destination;
        }
        return null;
    }
}
