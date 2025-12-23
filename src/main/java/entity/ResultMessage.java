package entity;

public class ResultMessage {
    public String jobId;
    public int sectionId;
    public int totalSections;
    public Counters counters;

    public ResultMessage() {
    }

    public ResultMessage(String jobId, int sectionId, int totalSections, Counters counters) {
        this.jobId = jobId;
        this.sectionId = sectionId;
        this.totalSections = totalSections;
        this.counters = counters;
    }
}