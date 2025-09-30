package cs2110;

public abstract class Mage extends Player {
    /*

     */
    /**
     * Name of spell that this mage can use
     */
    private String SPELL_NAME;

    public Mage(String name, GameEngine engine) {
        super(name, engine);
    }


    /**
     * //TODO documentation
     * <p>
     * Prompts the user to cast a spell. If the user inputs "no" (or anything other than "yes") no
     * spell is cast and the mage proceeds to attack If the user inputs "yes" cast a spell based on
     * the subclass of the mage, then skip attack phase
     *
     */
    @Override
    public boolean chooseAction() {
        System.out.println("Would you like to cast a " + SPELL_NAME + " (yes/no)? ");
        if (engine.getInputLine().equals("yes")) {
            //Spell cast, do not attack
            castSpell();
            return false;
        } else {
            //NO spell cast, go to attack
            return true;
        }
    }

    protected abstract void castSpell();
}
