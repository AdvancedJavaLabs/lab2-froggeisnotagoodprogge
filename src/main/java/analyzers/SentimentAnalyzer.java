package analyzers;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SentimentAnalyzer {

    private static final Set<String> POSITIVE = new HashSet<>(Arrays.asList("good", "happy", "great", "love", "excellent", "sister"));

    private static final Set<String> NEGATIVE = new HashSet<>(Arrays.asList("bad", "sad", "terrible", "hate", "poor"));

    public int analyze(String text) {
        int score = 0;
        for (String word : text.toLowerCase().split("\\W+")) {
            if (POSITIVE.contains(word)) score++;
            if (NEGATIVE.contains(word)) score--;
        }
        return score;
    }
}