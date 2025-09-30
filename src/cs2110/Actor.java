package cs2110;

/**
 * An individual who takes turns during the game simulation.
 */
public abstract class Actor {

    /**
     * The starting value for the actor's health.
     */
    public static final int STARTING_HEALTH = 20;

    /**
     * The name of this actor.
     */
    private final String name;

    /**
     * The current health level of this actor. Must be non-negative.
     */
    private int health;

    /**
     * The game engine that created this Actor.
     */
    protected GameEngine engine;

    /**
     * Constructs a new actor with the given `name` and `STARTING_HEALTH` value, associated with
     * the engine that created it.
     */
    public Actor(String name, GameEngine engine) {
        this.name = name;
        health = STARTING_HEALTH;
        this.engine = engine;
    }

    /**
     * Returns the name of this Actor.
     */
    public String name() {
        return name;
    }

    /**
     * Returns the current health level for this Actor.
     */
    public int health() {
        return health;
    }

    /**
     * Increases the current health of this actor by up to the given number of `points`, capped at
     * their `STARTING_HEALTH`.
     */
    protected void heal(int points) {
        health = Math.min(health + points, STARTING_HEALTH);
        System.out.println(name + " has been healed by " + points + " points!");
        System.out.println(name + " is now at " + health + " health.");
    }

    /**
     * Launches an attack against the given `target`. A random attack roll is calculated using this
     * actor's `power()` and sent to the `target` to `defend()`.
     */
    protected final void attack(Actor target) {
        int attackRoll = engine.diceRoll(1, power());
        target.defend(attackRoll);
    }

    /**
     * Responds to an attack with the given `attackRoll`. A random `defenseRoll` is calculated using
     * this actor's `toughness()`. If `attackRoll >= defenseRoll`, then the attack is successful and
     * this actor takes damage equal to the `attackRoll`. If the `attackRoll < defenseRoll`, then no
     * damage is taken and the successful defense is reported to the user.
     */
    protected final void defend(int attackRoll) {
        int defenseRoll = engine.diceRoll(1, toughness());
        if (attackRoll >= defenseRoll) {
            takeDamage(attackRoll);
        } else {
            System.out.println(name + " successfully defended, no damage was taken.");
        }
    }

    /**
     * Reduced this actor's health by the `damageAmount` or reduces their health to 0, whichever is
     * lesser. If the actor's health is reduced to 0, then their death is processed; otherwise,
     * their new health total is printed.
     */
    protected void takeDamage(int damageAmount) {
        damageAmount = Math.min(damageAmount, health);
        health -= damageAmount;
        System.out.println(name() + " took " + damageAmount + " points of damage.");
        if (health == 0) {
            processDeath();
        } else {
            System.out.println(name() + " is now at " + health + " health.");
        }
    }

    @Override
    public final String toString() {
        return name + " [power = " + power() + ", toughness = " + toughness()
                + ", health = " + health + "]";
    }

    /**
     * Returns the power level of this Actor.
     */
    public abstract int power();

    /**
     * Returns the toughness level of this Actor.
     */
    public abstract int toughness();

    /**
     * Simulates the actions that take place during one turn for this actor.
     */
    public abstract void takeTurn();

    /**
     * Updates the state of the game in response to this actor's death. Requires that this actor's
     * `health` is 0.
     */
    protected abstract void processDeath();
}
