package app.survey;


public enum SurveyElementType {

    TEXT(1),  PERSONALDATA(2),  CLOSEDQUESTION(3), OPENQUESTION(4),  SCORETABLE(5);

    private int value;

    SurveyElementType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}