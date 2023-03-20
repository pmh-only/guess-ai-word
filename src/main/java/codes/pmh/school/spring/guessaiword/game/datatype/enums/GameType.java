package codes.pmh.school.spring.guessaiword.game.datatype.enums;

public enum GameType {
    NORMAL_GAME () {
        @Override
        public int getWordCount() {
            return 1;
        }

        @Override
        public int getQnACount() {
            return 5;
        }
    },

    SPEED_RUN () {
        @Override
        public int getWordCount() {
            return 5;
        }

        @Override
        public int getQnACount() {
            return 5;
        }
    };

    public int getWordCount()  {
        return 0;
    }

    public int getQnACount() {
        return 0;
    }
}
