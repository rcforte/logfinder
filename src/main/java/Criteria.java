import java.util.*;

public enum Criteria {
  AND() {
    @Override
    public boolean find(Map<String, Boolean> wordsFound) {
      return wordsFound.values().stream().allMatch(v -> v == true);
    }
  }, 
  OR() {
    @Override
    public boolean find(Map<String, Boolean> wordsFound) {
      return wordsFound.values().stream().anyMatch(v -> v == true);
    }
  };

  public abstract boolean find(Map<String, Boolean> wordsFound);
}
