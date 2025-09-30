package cs2110;

public class FireMage extends Mage {

    private final String SPELL_NAME = "fire spell";

    public FireMage(String name, GameEngine engine) {
        super(name, engine);
    }


    /**
     * Casts a fireball at a monster chosen by the player. Fireball deals 2 times the damage of a
     * normal attack roll. The damage of the fireball is still subject to the defense calculation.
     * The user of fireball always takes damage equal to one quarter (rounded down) of the
     * fireball's damage.
     *
     */
    @Override
    protected void castSpell() {
        Actor target = engine.selectMonsterTarget();
        int attackRoll = 2 * engine.diceRoll(1, power());
        target.defend(attackRoll);
        this.takeDamage((int) (attackRoll * 0.25));
    }


}
