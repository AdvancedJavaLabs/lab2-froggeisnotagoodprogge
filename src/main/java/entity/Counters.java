package entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Counters implements Serializable {
    public long wordCount;
    public Map<String, Integer> wordFreq;
    public int sentimentScore;
    public String anonymizedText;
    public List<String> sortedSentences;

    public Counters() {
    }

    public Counters(long wordCount,
                    Map<String, Integer> wordFreq,
                    int sentimentScore,
                    String anonymizedText,
                    List<String> sortedSentences) {
        this.wordCount = wordCount;
        this.wordFreq = wordFreq;
        this.sentimentScore = sentimentScore;
        this.anonymizedText = anonymizedText;
        this.sortedSentences = sortedSentences;
    }
}