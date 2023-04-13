package codes.pmh.school.spring.guessaiword.game.enums;

public enum GameType {
    NORMAL,
    SPEEDRUN {
        @Override
        public int getMaxRoundCount() {
            return 5;
        }

        @Override
        public int getMaxAskableCount() {
            return 100;
        }

        @Override
        public int getAskThrottleSecond() {
            return 1;
        }
    };

    public int getMaxRoundCount () {
        return 1;
    }

    public int getMaxAskableCount() {
        return 5;
    }

    public int getCandidateCount() {
        return 3;
    }

    public int getAskThrottleSecond() {
        return 5;
    }

    public int getAnswerSubmitThrottleSecond() {
        return 5;
    }
}
