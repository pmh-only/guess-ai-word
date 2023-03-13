package codes.pmh.school.spring.guessaime.game;

public enum GameWordCategory {
    ANY ("ANY") {},
    ANIMAL ("ANIMAL") {},
    FOOD ("FOOD") {};

    private final String stringified;

    private GameWordCategory (String stringified) {
        this.stringified = stringified;
    }

    public String toString () {
        return this.stringified;
    }
}
