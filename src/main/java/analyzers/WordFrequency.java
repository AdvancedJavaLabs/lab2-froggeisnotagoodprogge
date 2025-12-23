package analyzers;

import java.util.HashMap;
import java.util.Map;

public class WordFrequency {
    public Map<String, Integer> countFrequency(String text) {
        Map<String, Integer> freq = new HashMap<>();
        for (String word : text.toLowerCase().split("\\W+")) {
            if (word.isEmpty()) continue;
            freq.put(word, freq.getOrDefault(word, 0) + 1);
        }
        return freq;
    }
}