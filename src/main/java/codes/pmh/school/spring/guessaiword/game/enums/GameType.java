package codes.pmh.school.spring.guessaiword.game.enums;

public enum GameType {
    NORMAL (1, 5, 3),
    SPEED_RUN (5, 5, 2);

    private int maxRoundCount = 0;

    private int maxAskableCount = 0;

    private int candidateCount = 0;

    private GameType (int maxRoundCount, int maxAskableCount, int candidateCount) {
        this.maxRoundCount = maxRoundCount;
        this.maxAskableCount = maxAskableCount;
        this.candidateCount = candidateCount;
    }

    public int getMaxRoundCount () {
        return this.maxRoundCount;
    }

    public int getMaxAskableCount() {
        return  this.maxAskableCount;
    }

    public int getCandidateCount() {
        return candidateCount;
    }
}
