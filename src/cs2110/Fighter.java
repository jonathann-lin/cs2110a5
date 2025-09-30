package cs2110;

public class Fighter extends Player {

    private Weapon currentWeapon;


    public Fighter(String name, GameEngine engine) {
        super(name, engine);
        currentWeapon = null;
    }


    /**
     Prompts the user to decide whether to change their currently equipped weapon.
     *
     * If the user inputs "no", the method skips weapon selection and proceeds to the attack phase.
     * If the user chooses to change their weapon by inputting "yes":
     * - If the selection is null, the current weapon (if any) is unequipped and
     *      and currentWeapon  is updated to null.
     * - If the player already has a weapon equipped and chooses a different one, the current weapon is unequipped,
     *      the new one is equipped, and currentWeapon is updated.
     * - If the player does not have a weapon equipped and selects one, the chosen weapon is equipped
     *      and the current weapon reference is updated.
     *  Assumes that user will only input either yes or no. Inputs that are not "no" are treated as responding "yes"
     *
     *  Method always returns true once the weapon change has been processed.
     */
    @Override
    public boolean chooseAction() {
        System.out.println("Would you like to change your current weapon (yes/no)? ");
        //if user does not want to change weapon, proceed to attack phase
        if (engine.getInputLine().equals("no")) {
            return true;
        }

        //if user wants to change weapon, get weapon choice
        Weapon chosenWeapon = engine.selectWeapon();

        //if weapon choice == null, then unequip current weapon
        //if currentWeapon is already null, then do nothing when user tries to unequip
        if (chosenWeapon == null) {
            if(currentWeapon!=null){
                currentWeapon.unequip();
                currentWeapon = null;
            }
        }

        //if user chooses an alternative and currently has a weapon equipped,
        // then unequip current weapon and equip new weapon
        else if (currentWeapon != null) {
            currentWeapon.unequip();
            chosenWeapon.equip();
            currentWeapon = chosenWeapon;
        }

        //user does not currently have a weapon and wants to equip a weapon
        else{
            chosenWeapon.equip();
            currentWeapon = chosenWeapon;
        }

        return true;

    }


    /**
     * returns power modified by the power value provided by a potential weapon
     */
    @Override
    public int power(){
        if (currentWeapon!=null){
            return super.power() + currentWeapon.power();
        }
        return super.power();
    }

    /**
     * returns toughness modified by the toughness value provided by a potential weapon
     */
    @Override
    public int toughness(){
        if (currentWeapon!=null){
            return super.toughness() + currentWeapon.toughness();
        }
        return super.toughness();
    }
}
