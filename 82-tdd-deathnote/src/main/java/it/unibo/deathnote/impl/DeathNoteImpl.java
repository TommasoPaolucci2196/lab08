package it.unibo.deathnote.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;


public class DeathNoteImpl implements DeathNote {

    private final List<String> rules = DeathNote.RULES;
    private final Map<String, Death> people;
    private String currentName; 

    public DeathNoteImpl() {
        people = new LinkedHashMap<>();
    }
    
    @Override
    public String getRule(final int ruleNumber) {
        if (ruleNumber < 1 || ruleNumber > rules.size()) {
            throw new IllegalArgumentException("This rule does not exist");
        }
        return rules.get(ruleNumber-1);
    }

    @Override
    public void writeName(final String name) {
        if (name == null) {
            throw new NullPointerException("Name is empty");
        }
        currentName = name;
        people.put(name,new Death());
    }

    @Override
    public boolean writeDeathCause(final String cause) {
        if (cause == null || currentName == null) {
            throw new IllegalStateException("No one is dead");
        }
        
        Death oldDeath = getDeath(currentName);
        Death newDeath = oldDeath.WriteCause(cause);

        if (newDeath == oldDeath) {
            return false;
        }

        people.put(currentName,newDeath);
        return true;

    }

    @Override
    public boolean writeDetails(final String details) {
        if (details == null || currentName == null) {
            throw new IllegalStateException("No one is dead");
        }
        
        Death oldDeath = getDeath(currentName);
        Death newDeath = oldDeath.WriteDetails(details);

        if (newDeath == oldDeath) {
            return false;
        }

        people.put(currentName,newDeath); //non ne crea uno nuovo, lo sostituisce
        return true;
    }

    @Override
    public String getDeathCause(final String name) {
        if (!isNameWritten(name)) {
            throw new IllegalArgumentException("Name not in DeathNote");
        }
        return getDeath(name).cause;
    }

    @Override
    public String getDeathDetails(final String name) {
        if (!isNameWritten(name)) {
            throw new IllegalArgumentException("Name not in DeathNote");
        }
        return getDeath(name).details;
    }

    @Override
    public boolean isNameWritten(final String name) {
        return people.containsKey(name);   
    }

    private Death getDeath(final String name) {
        final Death death = people.get(name);
        if (death == null) {
            throw new IllegalArgumentException("This person is not dead");
        }
        return death;
    }

    private static final class Death {
        private static final String DEFAULT_CAUSE = "heart attack";
        private static final byte VALID_CAUSE_TIMEOUT = 40;
        private static final short VALID_DETAILS_TIMEOUT = 6000 + VALID_CAUSE_TIMEOUT;
        private final String cause;
        private final String details;
        private final long timeOfDeath;

        private int hash;

        public Death(final String cause, final String details) {
            this.cause = cause;
            this.details = details;
            this.timeOfDeath = System.currentTimeMillis();
        }

        Death() {
            this(DEFAULT_CAUSE, "");
        }

        Death WriteCause(final String deathCause) {
            return System.currentTimeMillis() < timeOfDeath + VALID_CAUSE_TIMEOUT ? new Death(deathCause,this.details) : this;

        }

        Death WriteDetails(final String deathDetails) {
            return System.currentTimeMillis() < timeOfDeath + VALID_DETAILS_TIMEOUT ? new Death(this.cause,deathDetails) : this;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Death other)) {
                return false;
            }
            return Objects.equals(cause, other.cause)
                && Objects.equals(details, other.details)
                && timeOfDeath == other.timeOfDeath;
        }

        @Override
        public int hashCode() {
            if (hash == 0) {
                hash = Objects.hash(cause, details, timeOfDeath);
            }
            return hash;
        }

    }
}