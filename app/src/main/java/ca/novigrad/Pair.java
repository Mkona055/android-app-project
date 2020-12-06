package ca.novigrad;

public class Pair {
    private String fieldName;
    private String filling;
    public  static String TO_FILL = "To fill";
    public Pair(String fieldName){
        this.fieldName = fieldName;
        this.filling = TO_FILL;
    }
    public Pair(String fieldName, String filling){
        this.fieldName = fieldName;
        this.filling = filling;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFilling() {
        return filling;
    }

    public void setFilling(String filling) {
        this.filling = filling;
    }
}
