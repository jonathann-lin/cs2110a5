package cs2110;

public class Healer extends Mage {

    private final String SPELL_NAME = "healing spell";

    public Healer(String name, GameEngine engine) {
        super(name, engine);
    }

    /**
     * Casts a heal spell at a player target chosen by the user. The amount of health points
     * restored is a random roll 'int' roll between 0 and this healer's power level. The health of
     * the player target is increased by the rolled amount, up to the max health of the target.
     */
    @Override
    protected void castSpell() {
        Actor target = engine.selectPlayerTarget();
        int healRoll = engine.diceRoll(0, this.power());
        target.heal(healRoll);
    }


}
