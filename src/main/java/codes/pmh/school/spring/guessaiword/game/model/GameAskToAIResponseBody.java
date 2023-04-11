package codes.pmh.school.spring.guessaiword.game.model;

public class GameAskToAIResponseBody {
    private String askPrompt;

    private String response;

    public String getAskPrompt() {
        return askPrompt;
    }

    public void setAskPrompt(String askPrompt) {
        this.askPrompt = askPrompt;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
