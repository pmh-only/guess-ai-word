package codes.pmh.school.spring.guessaime.game;

import codes.pmh.school.spring.guessaime.ai.AIAskerResult;
import codes.pmh.school.spring.guessaime.ai.AIAskerThread;
import codes.pmh.school.spring.guessaime.dictionary.AnswerDictionary;
import codes.pmh.school.spring.guessaime.dictionary.QuestionDictionary;
import codes.pmh.school.spring.guessaime.util.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("gameService")
public class GameService {
    @Autowired
    private AnswerDictionary answerDictionary;

    @Autowired
    private QuestionDictionary questionDictionary;

    public Game createNewGame () throws IOException {
        Game game = new Game();
        String answer = getAnswer();
        List<String> questions = parseQuestions(getQuestionsMany(5), answer);
        List<AIAskerResult> results = askQuestions(questions);

        game.setCreatedAt(new Date());
        game.setAnswer(answer);
        game.setAiResults(results);
        game.maskAiResults();

        return game;
    }

    private String getAnswer () throws IOException {
        return answerDictionary.getRandomWord();
    }

    private List<String> getQuestionsMany (int count) throws IOException {
        List<String> questions = new ArrayList<>();

        for (int i = 0; i < count; i++)
            questions.add(questionDictionary.getRandomWord());

        return questions;
    }

    private List<String> parseQuestions (List<String> questions, String answer) {
        List<String> parsedQuestions = new ArrayList<>();

        for (String question : questions)
            parsedQuestions.add(String.format(question, answer));

        return parsedQuestions;
    }

    private List<AIAskerResult> askQuestions (List<String> questions) {
        List<Thread> threads = createAskThreads(questions);

        ThreadUtil.runThreads(threads);
        ThreadUtil.waitThreads(threads);

        return getAskThreadResults(threads);
    }

    private List<Thread> createAskThreads (List<String> questions) {
        List<Thread> askThreads = new ArrayList<>();

        for (String question : questions) {
            Thread thread = AIAskerThread.createAskTread(question);

            askThreads.add(thread);
        }

        return askThreads;
    }

    private List<AIAskerResult> getAskThreadResults (List<Thread> threads) {
        List<AIAskerResult> askerResults = new ArrayList<>();

        for (Thread thread : threads) {
            AIAskerThread askerThread = (AIAskerThread) thread;
            AIAskerResult askerResult = askerThread.getResult();

            askerResults.add(askerResult);
        }

        return askerResults;
    }
}
