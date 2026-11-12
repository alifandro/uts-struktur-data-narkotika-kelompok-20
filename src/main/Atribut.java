
public class Atribut {

    private String fieldName;
    private Object value;

    private String sourceFile;
    private int sourcePage;
    private String extractor;
    private String reviewer;
    private String status;

    public Atribut(
            String fieldName,
            Object value,
            String sourceFile,
            int sourcePage,
            String extractor,
            String reviewer,
            String status
    ) {

        this.fieldName = fieldName;
        this.value = value;
        this.sourceFile = sourceFile;
        this.sourcePage = sourcePage;
        this.extractor = extractor;
        this.reviewer = reviewer;
        this.status = status;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getValue() {
        return value;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public int getSourcePage() {
        return sourcePage;
    }

    public String getExtractor() {
        return extractor;
    }

    public String getReviewer() {
        return reviewer;
    }

    public String getStatus() {
        return status;
    }
}
