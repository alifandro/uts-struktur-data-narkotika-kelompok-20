package app;

public class Atribut {
    private String caseId;
    private String fieldName;
    private String value;
    private String sourceFile;
    private String sourcePage;
    private String extractor;
    private String reviewer;
    private String status;

    public Atribut(String caseId, String fieldName, String value, String sourceFile,
                   String sourcePage, String extractor, String reviewer, String status) {
        this.caseId = caseId;
        this.fieldName = fieldName;
        this.value = value;
        this.sourceFile = sourceFile;
        this.sourcePage = sourcePage;
        this.extractor = extractor;
        this.reviewer = reviewer;
        this.status = status;
    }

    public String getCaseId() { return caseId; }
    public String getFieldName() { return fieldName; }
    public String getValue() { return value; }
    public String getSourceFile() { return sourceFile; }
    public String getSourcePage() { return sourcePage; }
    public String getExtractor() { return extractor; }
    public String getReviewer() { return reviewer; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return String.format("%s | %s = %s (File: %s, Hal: %s, Status: %s)",
                caseId, fieldName, value, sourceFile, sourcePage, status);
    }
}