package cs2110;

public class Weapon {

    /**
     * The name of this weapon.
     */
    private final String name;

    /**
     * The power modifier provided by this weapon.
     */
    private final int power;

    /**
     * The toughness modifier provided by this weapon.
     */
    private final int toughness;

    /**
     * `true` if this weapon is currently equipped by a player, `false` otherwise.
     */
    private boolean equipped;

    /**
     * Constructs a new weapon with the given `name` and `power` level.
     */
    public Weapon(String name, GameEngine engine) {
        this.name = name;
        this.power = engine.diceRoll(1, 14) - 5; // -5 to 8
        this.toughness = 3 - power; // -5 to 8
        this.equipped = false;
    }

    /**
     * Returns the name of this weapon.
     */
    public String name() {
        return name;
    }

    /**
     * Returns whether this weapon is currently equipped by a player.
     */
    public boolean isEquipped() {
        return equipped;
    }

    /**
     * Returns the power modifier of this weapon.
     */
    public int power() {
        return power;
    }

    /**
     * Returns the toughness modifier of this weapon.
     */
    public int toughness() {
        return toughness;
    }

    /**
     * Marks that this weapon has been equipped by another player. Requires that this weapon is not
     * already equipped by a fighter.
     */
    public void equip() {
        assert !equipped;
        equipped = true;
    }

    /**
     * Marks that this weapon has been unequipped by another player. Requires that this weapon is
     * currently equipped by a fighter.
     */
    public void unequip() {
        assert equipped;
        equipped = false;
    }

    @Override
    public String toString() {
        return name + " [power = " + power + ", toughness = " + toughness + "]";
    }
}
