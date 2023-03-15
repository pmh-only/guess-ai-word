package codes.pmh.school.spring.guessaiword.game.datatype.api;

public class GameAPICreationResult {
    private int wordCount;

    private int qnaCount;

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
