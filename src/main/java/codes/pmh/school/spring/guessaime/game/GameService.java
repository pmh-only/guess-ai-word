package codes.pmh.school.spring.guessaime.game;

import codes.pmh.school.spring.guessaime.ai.AIAskResult;
import codes.pmh.school.spring.guessaime.ai.AIAsker;
import codes.pmh.school.spring.guessaime.util.PromptBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("gameService")
public class GameService {
    public String createGameToken (GameType type, GameWordCategory wordCategory) {
        AIAsker aiAsker = new AIAsker();
        String prompt = createPrompt(type, wordCategory);

        List<AIAskResult> askResults = aiAsker.ask(prompt);
        GameToken gameToken = new GameToken();

        gameToken.setAskResults(askResults);

        return gameToken.toString();
    }

    private String createPrompt (GameType type, GameWordCategory wordCategory) {
        return new PromptBuilder()
                .setWordCount(type.getWordCount())
                .setQnaCount(type.getQnACount())
                .setWordCategory(wordCategory.toString())
                .build();
    }
}
