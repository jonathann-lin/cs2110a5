package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Single top-level JUnit 5 test class.
 * No nested test classes; only helper methods.
 */
public class FullTest {

    /* ---------- helpers (no @Test here) ---------- */

    private static Scanner scannerFromLines(String... lines) {
        String joined = String.join(System.lineSeparator(), lines);
        return new Scanner(new ByteArrayInputStream(joined.getBytes(StandardCharsets.UTF_8)));
    }

    private static GameEngine newEngine(Scanner sc, boolean echo) {
        return new GameEngine(sc, echo);
    }

    private static String captureStdout(Runnable r) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bout));
        try { r.run(); } finally { System.setOut(oldOut); }
        return bout.toString();
    }

    private static void set(Object obj, String fieldName, Object value) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(obj, value);
        } catch (Exception e) {
            fail("reflection set failed: " + e);
        }
    }

    private static void installPlayers(GameEngine engine, Player... players) {
        set(engine, "players", players);
        set(engine, "numLivingPlayers", players.length);
    }

    private static void installMonsters(GameEngine engine, Monster... monsters) {
        set(engine, "monsters", monsters);
        set(engine, "numLivingMonsters", monsters.length);
    }

    private static void installWeapons(GameEngine engine, Weapon... weapons) {
        set(engine, "weapons", weapons);
    }

    /* ---------- shared fixtures ---------- */

    GameEngine engine;
    Fighter f1, f2;
    Weapon w0, w1, w2;
    FireMage fire;
    Healer healer;
    Fighter ally;
    Monster m0;

    @BeforeEach
    void setup() {
        engine = newEngine(scannerFromLines(), true);
        f1 = new Fighter("F1", engine);
        f2 = new Fighter("F2", engine);
        w0 = new Weapon("W0", engine);
        w1 = new Weapon("W1", engine);
        w2 = new Weapon("W2", engine);
        fire = new FireMage("FM", engine);
        healer = new Healer("HL", engine);
        ally = new Fighter("ALLY", engine);
        m0 = new Monster("M0", engine);
    }

    /* ---------- Fighter tests ---------- */

    @Test
    void fighter_noEquipmentChange_leavesStats() {
        int basePow = f1.power();
        int baseTuf = f1.toughness();

        engine = newEngine(scannerFromLines("no"), true);
        Fighter ff = new Fighter("F", engine);
        installPlayers(engine, ff, f2);
        installWeapons(engine, w0, w1, w2);

        assertTrue(ff.chooseAction());
        assertEquals(basePow, ff.power());
        assertEquals(baseTuf, ff.toughness());
        assertFalse(w0.isEquipped() || w1.isEquipped() || w2.isEquipped());
    }

    @Test
    void fighter_equipWeapon_updatesStats() {
        int basePow = f1.power();
        int baseTuf = f1.toughness();

        engine = newEngine(scannerFromLines("yes", "0"), true);
        Fighter ff = new Fighter("F", engine);
        installPlayers(engine, ff, f2);
        installWeapons(engine, w0, w1, w2);

        assertTrue(ff.chooseAction());
        assertTrue(w0.isEquipped());
        assertEquals(basePow + w0.power(), ff.power());
        assertEquals(baseTuf + w0.toughness(), ff.toughness());
    }

    @Test
    void fighter_swapWeapon_unequipsOld_equipsNew() {
        engine = newEngine(scannerFromLines("yes", "0"), true);
        f1.engine = engine;
        installPlayers(engine, f1, f2);
        installWeapons(engine, w0, w1, w2);
        assertTrue(f1.chooseAction());
        assertTrue(w0.isEquipped());

        engine = newEngine(scannerFromLines("yes", "1"), true);
        f1.engine = engine;
        installPlayers(engine, f1, f2);
        installWeapons(engine, w0, w1, w2);
        assertTrue(f1.chooseAction());

        assertFalse(w0.isEquipped());
        assertTrue(w1.isEquipped());
    }

    @Test
    void fighter_unequip_minusOne_restoresBase() {
        engine = newEngine(scannerFromLines("yes", "2"), true);
        f1.engine = engine;
        installPlayers(engine, f1, f2);
        installWeapons(engine, w0, w1, w2);
        assertTrue(f1.chooseAction());

        int basePow = f1.power() - w2.power();
        int baseTuf = f1.toughness() - w2.toughness();

        engine = newEngine(scannerFromLines("yes", "-1"), true);
        f1.engine = engine;
        installPlayers(engine, f1, f2);
        installWeapons(engine, w0, w1, w2);
        assertTrue(f1.chooseAction());

        assertFalse(w2.isEquipped());
        assertEquals(basePow, f1.power());
        assertEquals(baseTuf, f1.toughness());
    }

    @Test
    void fighter_prompt_exactText() {
        engine = newEngine(scannerFromLines("no"), true);
        f1.engine = engine;
        installPlayers(engine, f1, f2);
        installWeapons(engine, w0, w1, w2);
        String out = captureStdout(() -> f1.chooseAction());
        assertTrue(out.contains("Would you like to change your current equipment (yes/no)? "));
    }

    /* ---------- Mage tests ---------- */

    @Test
    void mage_yesCastsSpell_returnsFalse() {
        engine = newEngine(scannerFromLines("yes", "0"), true);
        fire.engine = engine;
        installPlayers(engine, fire, healer, ally);
        installMonsters(engine, m0);
        assertFalse(fire.chooseAction());
    }

    @Test
    void mage_noSkipsSpell_returnsTrue() {
        engine = newEngine(scannerFromLines("no"), true);
        healer.engine = engine;
        installPlayers(engine, healer, ally, fire);
        assertTrue(healer.chooseAction());
    }

    @Test
    void fireMage_recoil_applies() {
        int before = fire.health();
        engine = newEngine(scannerFromLines("yes", "0"), true);
        fire.engine = engine;
        installPlayers(engine, fire, healer, ally);
        installMonsters(engine, m0);
        captureStdout(() -> fire.chooseAction());
        assertTrue(fire.health() <= before);
    }

    @Test
    void healer_capsAtMax() {
        ally.takeDamage(15);
        engine = newEngine(scannerFromLines("yes", "1"), true);
        healer.engine = engine;
        installPlayers(engine, healer, ally, fire);
        int before = ally.health();
        captureStdout(() -> healer.chooseAction());
        assertTrue(ally.health() >= before);
        assertTrue(ally.health() <= Actor.STARTING_HEALTH);
    }

    @Test
    void mage_prompt_includesSpellName() {
        engine = newEngine(scannerFromLines("no"), true);
        fire.engine = engine;
        String out = captureStdout(() -> fire.chooseAction());
        assertTrue(out.contains("Would you like to cast a fire spell (yes/no)? "));
    }

    /* ---------- Integration test ---------- */

    @Test
    void fighter_takeTurn_flow() {
        engine = newEngine(scannerFromLines("no", "0"), true);
        Fighter f = new Fighter("F", engine);
        Monster m = new Monster("GOB", engine);
        installPlayers(engine, f);
        installMonsters(engine, m);
        String out = captureStdout(f::takeTurn);
        assertTrue(out.contains("Would you like to change your current equipment (yes/no)? "));
        assertTrue(out.contains("Select the number of the monster you'd like to target: "));
    }
}
