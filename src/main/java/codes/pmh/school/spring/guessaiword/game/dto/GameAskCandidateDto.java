package codes.pmh.school.spring.guessaiword.game.dto;

public class GameAskCandidateDto {
    private int id;

    private String askPrompt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAskPrompt() {
        return askPrompt;
    }

    public void setAskPrompt(String askPrompt) {
        this.askPrompt = askPrompt;
    }
}
