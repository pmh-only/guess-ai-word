package codes.pmh.school.spring.guessaime.game.api;

public class GameAPICreationResult {
    private String gameToken;

    private int wordCount;

    private int qnaCount;

    public String getGameToken() {
        return gameToken;
    }

    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getQnaCount() {
        return qnaCount;
    }

    public void setQnaCount(int qnaCount) {
        this.qnaCount = qnaCount;
    }
}
