package cs2110;

public class Monster extends Actor {

    /**
     * The power level of this monster.
     */
    private final int power;

    /**
     * The toughness level of this monster.
     */
    private final int toughness;

    /**
     * Constructs a new monster with the given `name` and randomly initializes its health, power,
     * and toughness levels.
     */
    public Monster(String name, GameEngine engine) {
        super(name, engine);
        power = engine.diceRoll(10, 20);
        toughness = engine.diceRoll(10, 20);
    }

    @Override
    public int power() {
        return power;
    }

    @Override
    public int toughness() {
        return toughness;
    }

    /**
     * Launches an attack against a random living player.
     */
    @Override
    public void takeTurn() {
        Player target = engine.randomLivingPlayer();
        System.out.println(name() + " chooses to attack " + target.name() + ".");
        attack(target);
    }

    @Override
    protected void processDeath() {
        assert health() == 0;
        System.out.println(name() + " has been defeated.");
        engine.processMonsterDeath(this);
    }
}
