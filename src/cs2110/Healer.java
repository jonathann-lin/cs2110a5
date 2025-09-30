package cs2110;

public class Healer extends Mage {

    public Healer(String name, GameEngine engine) {
        super(name, engine);
    }

    @Override
    protected void castSpell() {

    }

    private final String SPELL_NAME = "healing spell";

}
