package codes.pmh.school.spring.guessaime.ai;

public class AIAskerResult {
    private String question;

    private String result;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void mask (String answer) {
        maskQuestion(answer);
        maskResult(answer);
    }

    private void maskQuestion (String answer) {
        question = question.replaceAll(answer, "{{}}");
    }

    private void maskResult (String answer) {
        result = result.replaceAll(answer, "{{}}");
    }
}
