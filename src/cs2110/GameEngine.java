package cs2110;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Manages the state of our game simulation by creating and keeping track of players and monsters
 * and facilitating the turn order.
 */
public class GameEngine {

    /**
     * The array of players in this game simulation. Within this array, the first `numLivingPlayers`
     * entries reference `Player` objects with `health() > 0` and the remaining entries reference
     * `Player` objects with `health == 0`.
     */
    private Player[] players;

    /**
     * The number of players who are currently alive in the game. Must have `0 <= numLivingPlayers <
     * players.length`.
     */
    private int numLivingPlayers;

    /**
     * The array of monsters in this game simulation. Within this array, the first
     * `numLivingMonsters` entries reference `Monster` objects with `health() > 0` and the remaining
     * entries reference `Monster` objects with `health == 0`.
     */
    private Monster[] monsters;

    /**
     * The number of monsters who are currently alive in the game. Must have `0 <= numLivingMonsters
     * < monsters.length`.
     */
    private int numLivingMonsters;

    /**
     * The weapons that fighters can equip during this game simulation.
     */
    private Weapon[] weapons;

    /**
     * The random number generator that is used to model random events in this game.
     */
    private final Random rng;

    /**
     * The Scanner used to accept player inputs
     */
    private final Scanner sc;

    /**
     * Whether user inputs should be echoed to the output stream (true for file mode, false for
     * console mode)
     */
    private final boolean echo;

    /**
     * Constructs a new game engine with a seeded random number generator and the given Scanner `sc`
     * to process user inputs.
     */
    public GameEngine(Scanner sc, boolean echo) {
        rng = new Random(123456L);
        this.sc = sc;
        this.echo = echo;
    }

    /**
     * Carries out the simulation of our dungeon battle game by initializing the players and
     * monsters before entering the main game loop.
     */
    private void simulateGame() {
        System.out.println("*** Welcome to the Dungeons of Dragon Day! ***\n");

        initializePlayers();
        initializeWeapons();
        initializeMonsters();
        mainGameLoop();
    }

    /**
     * Queries for console input to set up the players of this game simulation.
     */
    private void initializePlayers() {
        while (players == null) {
            try {
                System.out.print("How many players will you have? ");
                numLivingPlayers = Integer.parseInt(getInputLine());
                players = new Player[numLivingPlayers];
            } catch (Exception e) {
                System.out.println("Your input couldn't be parsed successfully. Try again.");
            }
        }

        System.out.println("Enter the player names, one at a time.");
        for (int i = 0; i < numLivingPlayers; i++) {
            while (players[i] == null) {
                try {
                    System.out.print((i + 1) + ": ");
                    String name = getInputLine();
                    System.out.println("  What type of player is " + name + "?");
                    System.out.print("  [0=Fighter, 1=Fire Mage, 2=Healer]: ");
                    int type = Integer.parseInt(getInputLine());
                    players[i] = createPlayer(name, type);
                } catch (Exception e) {
                    System.out.println("Your input couldn't be parsed successfully. Try again.");
                }
            }
        }
    }

    /**
     * A factory method to produce players of different types.
     */
    private Player createPlayer(String name, int type) {
        if (type == 0) {
            // Uncomment this line after you have implemented this class.
            // return new Fighter(name, this);
            return null;
        } else if (type == 1) {
            // Uncomment this line after you have implemented this class.
            // return new FireMage(name, this);
            return null;
        } else if (type == 2) {
            // Uncomment this line after you have implemented this class.
            // return new Healer(name, this);
            return null;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns a randomly selected weapon name from the "weapons.txt" file.
     */
    private String getRandomWeaponName() {
        try {
            int line = rng.nextInt(100);
            return Files.readAllLines(Paths.get("weapons.txt")).get(line + 1);
        } catch (IOException e) {
            return "Weapon";
        }
    }

    /**
     * Creates the weapons for this game simulation and prints their information to the console.
     */
    private void initializeWeapons() {
        System.out.println("\nYour fighters will have acccess to these weapons for their battle.");

        weapons = new Weapon[numLivingPlayers + 1];
        for (int i = 0; i < numLivingPlayers + 1; i++) {
            Weapon weapon = new Weapon(getRandomWeaponName(), this);
            weapons[i] = weapon;
            System.out.println("[" + i + "] " + weapons[i]);
        }
        System.out.println();
    }


    /**
     * Returns a randomly selected monster name from the "monsters.txt" file.
     */
    private String getRandomMonsterName() {
        try {
            int line = rng.nextInt(100);
            return Files.readAllLines(Paths.get("monsters.txt")).get(line + 1);
        } catch (IOException e) {
            return "Monster";
        }
    }

    /**
     * Creates the monsters for this game simulation and prints their information to the console.
     */
    private void initializeMonsters() {
        numLivingMonsters = (players.length / 2) + 1;

        System.out.println("\nYou'll battle against " + numLivingMonsters + " monsters.");

        monsters = new Monster[numLivingMonsters];
        for (int i = 0; i < numLivingMonsters; i++) {
            monsters[i] = new Monster(getRandomMonsterName(), this);
            System.out.println("[" + i + "] " + monsters[i]);
        }
        System.out.println();
    }

    /**
     * Runs the main game loop. Terminates when the Players have won (all Monsters are dead), or
     * when the Players have lost (all Players are dead). While there are still living Players and
     * Monsters, generates a turn order for all active Actors at the start of each turn, and
     * executes each Actor's turn in the generated order. At the end of the game, if the Players
     * have won, the message "Congratulations! You defeated the monsters!" is printed. Otherwise, if
     * the Players have lost, the message "The monsters defeated you. Better luck next time!" is
     * printed.
     */
    private void mainGameLoop() {
        int round = 1;

        while (numLivingPlayers > 0 && numLivingMonsters > 0) {
            System.out.println("==========================================");
            System.out.println("Starting Round " + round + "\n");

            Actor[] actors; // contains living Actors in their turn order for this round
            actors = new Actor[numLivingMonsters + numLivingPlayers];
            System.arraycopy(players, 0, actors, 0, numLivingPlayers);
            System.arraycopy(monsters, 0, actors, numLivingPlayers, numLivingMonsters);
            shuffle(actors);

            System.out.println("The turn order will be: ");
            for (int i = 0; i < actors.length; i++) {
                System.out.println((i + 1) + ": " + actors[i]);
            }

            for (int j = 0; j < actors.length; j++) {
                if (actors[j].health() > 0 && numLivingMonsters > 0 && numLivingPlayers > 0) {
                    System.out.println("------------------------------------------");
                    System.out.println("Starting " + actors[j].name() + "'s Turn:\n");
                    actors[j].takeTurn();
                }
            }

            round++;
        }

        if (numLivingMonsters == 0) {
            System.out.println("Congratulations! You defeated the monsters!");
        } else {
            System.out.println("The monsters defeated you. Better luck next time!");
        }
    }

    /**
     * Performs a Fisher-Yates shuffle on this array of actors
     */
    public void shuffle(Actor[] actors) {
        for (int i = 0; i < actors.length; i += 1) {
            int j = rng.nextInt(i, actors.length);
            swap(actors, i, j);
        }
    }

    /**
     * Swaps the actors at positions `x` and `y` in the given array.
     */
    public void swap(Actor[] actors, int x, int y) {
        Actor temp = actors[x];
        actors[x] = actors[y];
        actors[y] = temp;
    }

    /**
     * Returns a reference to a random player that is currently alive in this simulation.
     */
    public Player randomLivingPlayer() {
        int randomIndex = rng.nextInt(numLivingPlayers);
        return players[randomIndex];
    }

    /**
     * Returns a reference to an array copy containing references to all living monsters.
     */
    private Monster[] livingMonsters() {
        return Arrays.copyOf(monsters, numLivingMonsters);
    }

    /**
     * Returns a reference to an array copy containing references to all living players.
     */
    private Player[] livingPlayers() {
        return Arrays.copyOf(players, numLivingPlayers);
    }

    /**
     * Select a monster from the living monsters. Returns the chosen monster.
     */
    public Monster selectMonsterTarget() {
        System.out.println("Select the number of the monster you'd like to target: ");

        Monster[] livingMonsters = livingMonsters();
        for (int i = 0; i < livingMonsters.length; i++) {
            System.out.println("[" + i + "] " + livingMonsters[i]);
        }

        Monster target = null;
        while (target == null) {
            try {
                System.out.print("Selection: ");
                target = livingMonsters[Integer.parseInt(getInputLine())];
            } catch (Exception e) { // either input was not a number or was an invalid index
                System.out.println(
                        "Your input couldn't be parsed successfully. Try again.");
            }
        }

        return target;
    }

    /**
     * Select a player from the living players. Returns the chosen player.
     */
    public Player selectPlayerTarget() {
        System.out.println("Select the number of the player you'd like to target: ");

        Player[] livingPlayers = livingPlayers();
        for (int i = 0; i < livingPlayers.length; i++) {
            System.out.println("[" + i + "] " + livingPlayers[i]);
        }

        Player target = null;
        while (target == null) {
            try {
                System.out.print("Selection: ");
                target = livingPlayers[Integer.parseInt(getInputLine())];
            } catch (Exception e) { // either input was not a number or was an invalid index
                System.out.println(
                        "Your input couldn't be parsed successfully. Try again.");
            }
        }

        return target;
    }

    /**
     * Select a weapon from available weapons. Returns the chosen weapon, or null if no weapon is
     * chosen.
     */
    public Weapon selectWeapon() {
        Weapon[] weapons = availableWeapons();
        System.out.println(
                "\nHere is the selection of available weapons. Please select a weapon to "
                        + "equip, or enter `-1` if you would like to unequip current equipment.");
        for (int i = 0; i < weapons.length; i++) {
            System.out.println("[" + i + "] " + weapons[i]);
        }
        System.out.print("Selection: ");

        while (true) {
            try {
                int input = Integer.parseInt(getInputLine());
                if (input == -1) {
                    return null;
                } else {
                    return weapons[input];
                }
            } catch (Exception e) { // either input was not a number or was an invalid index
                System.out.println(
                        "Your input couldn't be parsed successfully. Try again.");
            }
        }
    }

    /**
     * Returns an array of the weapons that are currently not equipped by any player.
     */
    public Weapon[] availableWeapons() {
        int j = 0; // number of available weapons found
        Weapon[] available = new Weapon[weapons.length];

        for (int i = 0; i < weapons.length; i++) {
            if (!weapons[i].isEquipped()) {
                available[j] = weapons[i];
                j++;
            }
        }
        return Arrays.copyOfRange(available, 0, j);
    }

    /**
     * Reports that 'monster' has been defeated.
     */
    public void processMonsterDeath(Monster monster) {
        for (int i = 0; i < numLivingMonsters; i++) {
            if (monsters[i] == monster) { // found this monster
                swap(monsters, i, numLivingMonsters - 1);
                numLivingMonsters--;
                return;
            }
        }
    }

    /**
     * Reports that 'player' has been defeated.
     */
    public void processPlayerDeath(Player player) {
        for (int i = 0; i < numLivingPlayers; i++) {
            if (players[i] == player) { // found this monster
                swap(players, i, numLivingPlayers - 1);
                numLivingPlayers--;
                return;
            }
        }
    }

    /**
     * Returns the next line of input from the user.
     */
    public String getInputLine() {
        String line = sc.nextLine();
        if (echo) {
            System.out.println(line);
        }
        return line;
    }

    /**
     * Returns the random result of a dice roll between `min` and `max` (inclusive).
     */
    public int diceRoll(int min, int max) {
        return rng.nextInt(min, max+1);
    }

    /**
     * Runs this game simulation.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            new GameEngine(new Scanner(System.in), false).simulateGame();
        } else {
            File commandFile = new File(args[0]);
            try {
                new GameEngine(new Scanner(commandFile), true).simulateGame();
            } catch (FileNotFoundException e) {
                System.out.println("ERROR! File not found: " + commandFile);
                System.exit(1);
            }
        }
    }
}
