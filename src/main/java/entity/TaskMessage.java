package entity;

public class TaskMessage {
    public String jobId;
    public int sectionId;
    public int totalSections;
    public String text;

    public TaskMessage() {
    }

    public TaskMessage(String jobId, int sectionId, int totalSections, String text) {
        this.jobId = jobId;
        this.sectionId = sectionId;
        this.totalSections = totalSections;
        this.text = text;
    }
}