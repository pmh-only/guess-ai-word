package codes.pmh.school.spring.guessaiword.game.enums;

public enum GameType {
    NORMAL (1, 5),
    SPEED_RUN (5, 5);

    private int maxRoundCount = 0;

    private int maxAskableCount = 0;

    private GameType (int maxRoundCount, int maxAskableCount) {
        this.maxRoundCount = maxRoundCount;
        this.maxAskableCount = maxAskableCount;
    }

    public int getMaxRoundCount () {
        return this.maxRoundCount;
    }

    public int getMaxAskableCount() {
        return  this.maxAskableCount;
    }
}
