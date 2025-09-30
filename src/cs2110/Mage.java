package cs2110;

public abstract class Mage extends Player {
/*

 */
    private String SPELL_NAME;

    public Mage(String name, GameEngine engine){
        super(name,engine);
    }

    @Override
    public boolean chooseAction(){
        System.out.println("Would you like to cast a " + SPELL_NAME + " (yes/no)? ");
        if (engine.getInputLine().equals("yes")) {
            //Spell cast, do not attack
            return false;
        }
        else{
            //NO spell cast, go to attack
            return true;
        }
    }
}
