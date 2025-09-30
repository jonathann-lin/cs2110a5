package cs2110;

/**
 * An actor that is controlled by the user through console input.
 */
public abstract class Player extends Actor {

    /**
     * The base power level of this player.
     */
    private final int basePower;

    /**
     * The base toughness level of this player.
     */
    private final int baseToughness;

    /**
     * Constructs a new player with the given `name` and initializes their health, power, and
     * toughness levels.
     */
    public Player(String name, GameEngine engine) {
        super(name, engine);
        this.engine = engine;
        basePower = engine.diceRoll(10, 20);
        baseToughness = engine.diceRoll(10, 20);
    }

    @Override
    public int power() {
        return basePower;
    }

    @Override
    public int toughness() {
        return baseToughness;
    }

    /**
     * Uses the console to query the user for which action they would like to take on their turn.
     * Depending on which action is selected, the player's turn may also include an attack phase.
     */
    @Override
    public void takeTurn() {
        if (chooseAction()) {
            Actor target = engine.selectMonsterTarget();
            attack(target);
        }
    }

    @Override
    protected void processDeath() {
        assert health() == 0;
        System.out.println(name() + " has been defeated.");
        engine.processPlayerDeath(this);
    }

    /**
     * Uses the console to query the user for which action they would like to take on their turn,
     * and potentially carries out the action of their choice. The available actions are determined
     * by the Player's subtype. Returns `true` if the chosen action is followed by an attack phase
     * and `false` if the chosen action completes the player's turn.
     */
    public abstract boolean chooseAction();
}
