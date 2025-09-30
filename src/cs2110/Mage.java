package cs2110;


/**
 * A specific subtype of player that has the option of casting a spell unique to each mage subtype.
 * Casting a spell replaces the mage's attack for that turn.
 */
public abstract class Mage extends Player {

    /**
     * Name of spell that this mage can use
     */
    private final String SPELL_NAME;

    public Mage(String name, GameEngine engine, String spellName) {
        super(name, engine);
        this.SPELL_NAME = spellName;
    }


    /**
     * Prompts the user to cast a spell. If the user inputs "no" (or anything other than "yes"), no
     * spell is cast, and returns true (the mage proceeds to attack). If the user inputs "yes," cast
     * a spell based on the subclass of the mage, then return false, indicating that no attack phase
     * will follow.
     *
     */
    @Override
    public boolean chooseAction() {
        System.out.print("Would you like to cast a " + SPELL_NAME + " (yes/no)? ");
        if (engine.getInputLine().equals("yes")) {
            //Spell cast, do not attack
            castSpell();
            return false;
        } else {
            //NO spell cast, go to attack
            return true;
        }
    }


    /**
     * A hook method that enables subclasses of Mage to implement unique spell behavior.
     */
    protected abstract void castSpell();
}
