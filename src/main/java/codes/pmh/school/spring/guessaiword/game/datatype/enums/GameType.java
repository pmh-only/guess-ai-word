package codes.pmh.school.spring.guessaiword.game.datatype.enums;

public enum GameType {
    NORMAL_GAME () {
        @Override
        public int getRoundCount() {
            return 1;
        }

        @Override
        public int getAIResultCount() {
            return 5;
        }
    },

    SPEED_RUN () {
        @Override
        public int getRoundCount() {
            return 5;
        }

        @Override
        public int getAIResultCount() {
            return 5;
        }
    };

    public int getRoundCount()  {
        return 0;
    }

    public int getAIResultCount() {
        return 0;
    }
}
