package codes.pmh.school.spring.guessaime.ai.api;

public class AIAPIResult {
    private AIAPIResultChoice[] choices;

    public String getContent() {
        return choices[0].getMessage().getContent();
    }
}
