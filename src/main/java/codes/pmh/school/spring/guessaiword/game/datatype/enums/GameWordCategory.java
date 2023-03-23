package codes.pmh.school.spring.guessaiword.game.datatype.enums;

public enum GameWordCategory {
    ANY ("ANY") {},

    ANIMAL ("ANIMAL") {},

    FOOD ("FOOD") {},

    FURNITURE ("FURNITURE") {},

    TOOLS ("TOOLS") {};

    private final String stringified;

    GameWordCategory(String stringified) {
        this.stringified = stringified;
    }

    public String toString () {
        return this.stringified;
    }
}
