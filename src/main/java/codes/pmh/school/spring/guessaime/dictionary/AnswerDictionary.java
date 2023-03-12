package codes.pmh.school.spring.guessaime.dictionary;

import org.springframework.stereotype.Component;

@Component
public class AnswerDictionary extends Dictionary {
    @Override
    protected String getResourcePath() {
        return "classpath:dictionaries/answer.dict";
    }
}
