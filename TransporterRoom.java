import java.util.Random;
import java.util.ArrayList;

/**
 * TransporterRoom is a special type of Room where players are transported to a random room
 * when they attempt to leave. The destination room is selected randomly from all available rooms
 * in the game, independent of the chosen exit direction.
 *
 * @author Noor Karabala
 */
public class TransporterRoom extends Room {
    
    private Random randomGenerator;
    
    /**
     * Constructs a new TransporterRoom with the specified description.
     * 
     * @param description The description of the room.
     */
    public TransporterRoom(String description) {
        super(description);
        randomGenerator = new Random();
    }

    /** 
     * getExit overrides the getExit method to always return a random room.
     * 
     * @param direction The direction the player wishes to go (ignored in this implementation).
     * 
     * @return A randomly chosen room.
     */
    @Override
    public Room getExit(String direction) {
        return findRandomRoom(); // Always go to a random room
    }

    /**
     * Chooses a random room from the list of all game rooms.
     * 
     * @return A randomly selected room.
     */
    private Room findRandomRoom() {
        
        ArrayList<Room> rooms = Room.getGameRooms(); // Get the list of all room
        
        return rooms.get(randomGenerator.nextInt(rooms.size()));
    }
}
