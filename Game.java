import java.util.Stack;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a text based adventure game.  Users 
 *  can walk around some scenery. That's all.
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes 
 * @author Noor Karabala
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room previousRoom;
    private Stack<Room> previousRoomStack;
    
    private Item pickedItem;  
    private boolean hasEaten; // Track if the player has eaten a cookie
    private int itemsPickedUpSinceEating; // Track items picked up since eating a cookie
    
    
    /**
     * Create the game and initialise its internal map, as well
     * as the previous room (none) and previous room stack (empty).
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        previousRoom = null;
        previousRoomStack = new Stack<Room>();
        pickedItem = null;
        hasEaten = false;
        itemsPickedUpSinceEating = 0;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office;
        Item cookie1, cookie2, cookie3, chair1, chair2, bar, computer1, computer2, computer3, tree1, tree2;
        
        // create some items
        cookie1 = new Item("cookie","a chocolate cookie",2);
        cookie2 = new Item("cookie","a white chocolate cookie",2);
        cookie3 = new Item("cookie","a white chocolate cookie",2);
        chair1 = new Item("chair1","a wooden chair",5);
        chair2 = new Item("chair2","a wooden chair",5);
        bar = new Item("bar", "a long bar with stools",95.67);
        computer1 = new Item("computer1", "a PC",10);
        computer2 = new Item("computer2", "a Mac",5);
        computer3 = new Item("computer3", "a PC",10);
        tree1 = new Item("tree1", "a fir tree",500.5);
        tree2 = new Item("tree2","a fir tree",500.5);
        
        // create beamers
        Beamer beamer1 = new Beamer("beamer1", "a portable teleportation device", 3.5);
        Beamer beamer2 = new Beamer("beamer2", "a portable teleportation device", 3.5);
       
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        
        // Create transporter room
        TransporterRoom transporterRoom = new TransporterRoom("in a mysterious transporter room");
        
        // put items in the rooms
        outside.addItem(tree1);
        outside.addItem(tree2);
        theatre.addItem(cookie1);
        pub.addItem(bar);
        pub.addItem(cookie3);
        lab.addItem(chair1);
        lab.addItem(computer1);
        lab.addItem(chair2);
        lab.addItem(computer2);
        office.addItem(cookie2);
        office.addItem(computer3);
        
        // place beamers in rooms
        theatre.addItem(beamer1);
        pub.addItem(beamer2);
        
        // initialise room exits
        outside.setExit("east", theatre); 
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", transporterRoom); // choosing transporterRoom place
        transporterRoom.setExit("south", outside);
        
        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
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
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackBack")) {
            stackBack(command);
        }
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }
        else if (commandWord.equals("charge")) {
            charge(command);
        }
        else if (commandWord.equals("fire")) {
            fire(command);
        }
        
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * If we go to a new room, update previous room and previous room stack.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom; // store the previous room
            previousRoomStack.push(currentRoom); // and add to previous room stack
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            // output player item holding status
            if (pickedItem != null){    
                System.out.println("\nPlayer is holding " + pickedItem.getName());
            }else{
                System.out.println("\nPlayer is not holding anything");
            }
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
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
    
    /** 
     * "Look" was entered. Check the rest of the command to see
     * whether we really want to look.
     * 
     * @param command The command to be processed
     */
    private void look(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Look what?");
            return;
        }
        
        // output the long description of this room
        System.out.println(currentRoom.getLongDescription());
        
        // output player item holding status
        if (pickedItem != null){    
            System.out.println("\nPlayer is holding " + pickedItem.getName());
        }else{
            System.out.println("\nPlayer is not holding anything");
        }
        
    }
    
    /** 
     * "Eat" was entered. Check the rest of the command to see
     * whether we really want to eat.
     * 
     * @param command The command to be processed
     */
    private void eat(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Eat what?");
            return;
        }

        if (pickedItem == null) {
            System.out.println("You are not holding anything to eat.");
            return;
        }

        if (pickedItem.getName().equals("cookie")) {
            System.out.println("You ate the cookie! You can now pick up items. (5 at most)");
            hasEaten = true;
            itemsPickedUpSinceEating = 0; // Reset the counter
            pickedItem = null; // Remove the cookie from the player's hand
        } else {
            System.out.println("You can only eat a cookie.");
        }
    }
    
    /** 
     * "Back" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     */
    private void back(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Back what?");
        }
        else {
            // go back to the previous room, if possible
            if (previousRoom==null) {
                System.out.println("No room to go back to.");
            } else {
                // go back and swap previous and current rooms,
                // and put current room on previous room stack
                Room temp = currentRoom;
                currentRoom = previousRoom;
                previousRoom = temp;
                previousRoomStack.push(temp);
                // and print description
                System.out.println(currentRoom.getLongDescription());
            }
            
            // output player item holding status
            if (pickedItem != null){    
                System.out.println("\nPlayer is holding " + pickedItem.getName());
            }else{
                System.out.println("\nPlayer is not holding anything");
            }
        }
    }
    
    /** 
     * "StackBack" was entered. Check the rest of the command to see
     * whether we really want to stackBack.
     * 
     * @param command The command to be processed
     */
    private void stackBack(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("StackBack what?");
        }
        else {
            // step back one room in our stack of rooms history, if possible
            if (previousRoomStack.isEmpty()) {
                System.out.println("No room to go stack back to.");
            } else {
                // current room becomes previous room, and
                // current room is taken from the top of the stack
                previousRoom = currentRoom;
                currentRoom = previousRoomStack.pop();
                // and print description
                System.out.println(currentRoom.getLongDescription());
            }
            
            // output player item holding status
            if (pickedItem != null){    
                System.out.println("\nPlayer is holding " + pickedItem.getName());
            }else{
                System.out.println("\nPlayer is not holding anything");
            }
        }
    }
    
    /**
     * take allows the player to take an item from the current room.
     * 
     * @param command The command to be processed
     */
    private void take(Command command)
    {
        if(!command.hasSecondWord()){
            System.out.println("take what?");
            return;
        }
        
        // Check if the player is already holding something
        if (pickedItem != null) {
            System.out.println("You are already holding something. Drop it first.");
            return;
        }
        
        String itemName = command.getSecondWord();
        
        // Check if the player is trying to pick up a cookie
        if (itemName.equals("cookie")) {

            pickedItem = currentRoom.removeItem(itemName);
            if (pickedItem == null) {
                System.out.println("There is no cookie in this room.");
                return;
            }

            System.out.println("You picked up a cookie.");
            return;
        }

        // If the player hasn't eaten a cookie, they can't pick up other items
        if (!hasEaten) {
            System.out.println("You need to eat a cookie before you can pick up other items.");
            return;
        }

        // If the player has already picked up 5 items since eating a cookie, they need to eat another cookie
        if (itemsPickedUpSinceEating >= 5) {
            System.out.println("You are hungry again! Find and eat a cookie to pick up more items.");
            return;
        }

        // Try to pick up the item
        pickedItem = currentRoom.removeItem(itemName);
        if (pickedItem == null) {
            System.out.println("That item is not in the room.");
            return;
        }

        System.out.println("You picked up " + pickedItem.getName());
        itemsPickedUpSinceEating++; // Increment the count of items picked up
    }
        
    /**
     * Drops the currently held item into the current room.
     * 
     * @param command The command to be processed
     */
    private void drop(Command command)
    {
        if(command.hasSecondWord()){
            System.out.println("drop what?");
            return;
        }
        
        if(this.pickedItem == null){
            System.out.println("You are not holding anything.");
            return;
        }
        
        currentRoom.addItem(pickedItem);
        System.out.println("You dropped " + pickedItem.getName());
        pickedItem = null;
    }
    
    /**
     * Charges the beamer in the current room.  
     *  
     * @param command The command to be processed.
     */
    private void charge(Command command){
        
        if (command.hasSecondWord()) 
        {
            System.out.println("Charge what?");
            return;
        }
    
        if (pickedItem == null) 
        {
            System.out.println("You are not holding anything.");
            return;
        }
    
        if (pickedItem.getClass() == Beamer.class)   //check if pickedItem is a Beamer
        {
            Beamer beamer = (Beamer) pickedItem; // Cast to Beamer to apply Beamer methods
    
            if (beamer.getIsCharged()) 
            { 
                System.out.println("The beamer is already charged.");
            } else 
            {
                beamer.charge(currentRoom);  
                System.out.println("The beamer has been charged!");
            }
        } else 
        {
            System.out.println("You must be holding a beamer to charge it.");
        }
    }
    
    /**
     * Fires the beamer, transporting the player to the charged room. 
     *  
     * @param command The command to be processed.
     */
    private void fire(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Fire what?");
            return;
        }

        if (pickedItem == null) {
            System.out.println("You are not holding anything.");
            return;
        }

        if (pickedItem.getClass() == Beamer.class) //check if pickedItem is a Beamer
        {
            Beamer beamer = (Beamer) pickedItem; // Cast to Beamer to apply Beamer methods
            Room destination = beamer.fire();
            if (destination != null) 
            {
                System.out.println("Beamer fired! You are transported to " + destination.getShortDescription());
                previousRoom = currentRoom; // Store the current room as the previous room
                currentRoom = destination; // Move to the charged room
                System.out.println(currentRoom.getLongDescription());
            } else 
            {
                System.out.println("The beamer is not charged.");
            }
        } else 
        {
            System.out.println("You must be holding a beamer to fire it.");
        }
    }
    
    
}
