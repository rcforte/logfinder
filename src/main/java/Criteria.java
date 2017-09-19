import java.util.*;

public enum Criteria {
    AND() {
        @Override
        public boolean find(Map<String, Boolean> wordsFound) {
            boolean found = true;
            for (Boolean b : wordsFound.values()) {
                found = found && b;
            }
            return found; 
        }
    }, 
    OR() {
        @Override
        public boolean find(Map<String, Boolean> wordsFound) {
            boolean found = false;
            for (Boolean b : wordsFound.values()) {
                found = found || b;
            }
            return found; 
        }
    };

    public abstract boolean find(Map<String, Boolean> wordsFound);
}
