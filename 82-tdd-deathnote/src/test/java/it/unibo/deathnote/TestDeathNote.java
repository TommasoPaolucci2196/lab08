package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;

class TestDeathNote {

    @Test 
    void TestInvalidRules() {
        DeathNote note = new DeathNoteImpl();

        IllegalArgumentException exception0 = assertThrows(IllegalArgumentException.class, () -> note.getRule(0));
        assertNotNull(exception0.getMessage());
        assertFalse(exception0.getMessage().isBlank());

        IllegalArgumentException exceptionNeg = assertThrows(IllegalArgumentException.class, () -> note.getRule(-1));
        assertNotNull(exceptionNeg.getMessage());
        assertFalse(exceptionNeg.getMessage().isBlank());
    }
    
    @Test
    void TestEmptyRules() {
        DeathNote note = new DeathNoteImpl();
        for (int i=1; i<= DeathNote.RULES.size(); i++) {
            String rule = note.getRule(i);
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }

    }

    @Test
    void TestHumanDeath() {
        DeathNote note = new DeathNoteImpl();
        String name = "Michele Santi";

        assertFalse(note.isNameWritten(name));
        note.writeName(name);

        assertTrue(note.isNameWritten(name));
        assertFalse(note.isNameWritten("Lomba"));
        assertFalse(note.isNameWritten(""));

    } 

    @Test 
    void TestDeathCause() throws InterruptedException{
        DeathNote note = new DeathNoteImpl();

        assertThrows(IllegalStateException.class, () -> note.writeDeathCause("died of death"));
        note.writeName("Michele Santi");
        assertEquals("heart attack",note.getDeathCause("Michele Santi"));
        note.writeName("Simo Lomba");
        assertTrue(note.writeDeathCause("karting accident"));
        assertEquals("karting accident",note.getDeathCause("Simo Lomba"));
        Thread.sleep(100);
        assertFalse(() -> (note.writeDeathCause("died of death")));
        assertEquals("karting accident",note.getDeathCause("Simo Lomba"));

    }

    @Test 
    void TestDeathDetails() throws InterruptedException{
        DeathNote note = new DeathNoteImpl();

        assertThrows(IllegalStateException.class, () -> note.writeDetails("died of death"));
        note.writeName("Michele Santi");
        assertEquals("", note.getDeathDetails("Light Yagami"));
        assertTrue(note.writeDetails("ran for too long"));
        assertEquals("ran for too long", note.getDeathDetails("Light Yagami"));
        note.writeName("L");
        assertFalse(() -> (note.writeDetails("drowned")));
        assertEquals("",note.getDeathDetails("L"));


        Thread.sleep(6100);


    }

}