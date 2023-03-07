package codes.pmh.school.spring.guessaime.ai;

public class AIAskResult {
    private AIAskResultChoice[] choices;

    public String getContent() {
        return choices[0].getMessage().getContent();
    }
}
