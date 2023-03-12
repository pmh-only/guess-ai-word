package codes.pmh.school.spring.guessaime.dictionary;

import org.springframework.stereotype.Component;

@Component
public class QuestionDictionary extends Dictionary {
    @Override
    protected String getResourcePath() {
        return "classpath:dictionaries/question.dict";
    }
}
