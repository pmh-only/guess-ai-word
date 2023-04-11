package codes.pmh.school.spring.guessaiword.dictionary;

import codes.pmh.school.spring.guessaiword.dictionary.dto.DictionaryFileContentDto;
import codes.pmh.school.spring.guessaiword.dictionary.enums.DictionaryCategory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("askPromptDictionary")
public class DictionaryServiceAskPromptImpl implements DictionaryRandomable {
    private static final String FILE_CLASS_PATH = "classpath:dicts/ask_prompt.json";

    private DictionaryServiceImpl dictionaryService;

    public DictionaryServiceAskPromptImpl() throws IOException {
        this.dictionaryService = new DictionaryServiceImpl(FILE_CLASS_PATH);
    }

    @Override
    public DictionaryFileContentDto getRandom(DictionaryCategory category) {
        return this.dictionaryService.getRandom(category);
    }
}
