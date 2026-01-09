# Text Adventure Game (Java)

A command-line adventure game written in Java where the player explores rooms, interacts with items, and uses special mechanics such as teleportation and random room transport.

## Features
- Navigate through rooms using text commands
- Pick up and drop items (one item at a time)
- Eat food items to enable further actions
- Use a **beamer** item to teleport back to a saved room
- Encounter a **transporter room** that randomly moves the player
- Menu-driven command system

## Game Mechanics
- The player can carry only one item at a time
- Food items must be eaten before picking up other objects
- After eating food, the player can carry a limited number of items before becoming hungry again
- The beamer must be charged before it can be fired
- Transporter rooms send the player to a random room when exiting

## Object-Oriented Design
- Built using core OOP principles
- Inheritance and polymorphism are used:
  - `Beamer` extends `Item`
  - `TransporterRoom` extends `Room`
- Clear separation between game logic, items, rooms, and commands

## Files
- `Game.java` — Main game loop and command handling
- `Room.java` — Base room class
- `TransporterRoom.java` — Special room with random transport behavior
- `Item.java` — Base class for items
- `Beamer.java` — Teleportation item
- `CommandWords.java` — Supported game commands
- `Assign2A.png` — UML class diagram

javac *.java
java Game
