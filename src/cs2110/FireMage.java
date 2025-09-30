package cs2110;


/**
 * A subclass of Mage that can cast  a fire spell at a monster of the user's
 * choice that does increased damage at the cost of the mage taking recoil damage.
 */
public class FireMage extends Mage {

    public FireMage(String name, GameEngine engine) {
        super(name, engine, "fire spell");
    }


    /**
     * Casts a fireball at a monster target chosen by the user. Fireball deals 2 times the damage of
     * a normal attack roll. The damage of the fireball is still subject to the defense calculation.
     * The user of fireball always takes damage equal to one quarter (rounded down) of the
     * fireball's damage.
     *
     */
    @Override
    protected void castSpell() {
        Actor target = engine.selectMonsterTarget();
        int attackRoll = 2 * engine.diceRoll(1, this.power());
        target.defend(attackRoll);
        this.takeDamage((int) (attackRoll * 0.25));
    }


}
