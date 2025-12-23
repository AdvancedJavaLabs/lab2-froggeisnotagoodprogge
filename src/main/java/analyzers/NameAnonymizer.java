package analyzers;

public class NameAnonymizer {
    public String anonymize(String text) {
        return text.replaceAll("\\b[A-Z][a-z]+\\b", "<NAME>");
    }
}