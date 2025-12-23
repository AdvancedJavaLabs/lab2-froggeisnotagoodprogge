import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class JobState {

    public final int totalSections;          // общее число секций для этого job
    public final AtomicInteger received;     // сколько секций уже обработано

    // Агрегированные результаты
    public final AtomicLong totalWordCount;  // суммарное количество слов
    public final Map<String, Integer> globalWordFreq; // глобальная частота слов
    public final AtomicInteger totalSentiment; // суммарная тональность
    public final StringBuilder anonymizedText; // объединённые тексты с заменой имён
    public final List<String> sortedSentences; // объединённые отсортированные предложения

    public JobState(int totalSections) {
        this.totalSections = totalSections;
        this.received = new AtomicInteger(0);

        this.totalWordCount = new AtomicLong(0);
        this.globalWordFreq = new HashMap<>();
        this.totalSentiment = new AtomicInteger(0);
        this.anonymizedText = new StringBuilder();
        this.sortedSentences = new ArrayList<>();
    }

    public static List<String> getTopN(Map<String, Integer> freqMap, int N) {
        return freqMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(N)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static List<Map.Entry<String, Integer>> getTopNWithCount(Map<String, Integer> freqMap, int N) {
        return freqMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(N).collect(Collectors.toList());
    }
}