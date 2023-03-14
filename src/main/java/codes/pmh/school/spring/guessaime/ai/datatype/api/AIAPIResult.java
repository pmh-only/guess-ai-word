package codes.pmh.school.spring.guessaime.ai.datatype.api;

public class AIAPIResult {
    private AIAPIResultChoice[] choices;

    public String getContent() {
        return choices[0].getMessage().getContent();
    }

    public void setChoices(AIAPIResultChoice[] choices) {
        this.choices = choices;
    }
}
