package codes.pmh.school.spring.guessaiword.dictionary;

import codes.pmh.school.spring.guessaiword.dictionary.dto.DictionaryFileContentDto;
import codes.pmh.school.spring.guessaiword.dictionary.enums.DictionaryCategory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service("askPromptDictionary")
public class DictionaryServiceAskPromptImpl implements DictionaryRandomable {
    private static final String FILE_CLASS_PATH = "classpath:dicts/ask_prompt.json";

    private DictionaryServiceImpl dictionaryService;

    public DictionaryServiceAskPromptImpl() throws IOException {
        this.dictionaryService = new DictionaryServiceImpl(FILE_CLASS_PATH);
    }

    @Override
    public List<DictionaryFileContentDto> getRandoms(DictionaryCategory category, int count) {
        return dictionaryService.getRandoms(category, count);
    }

    @Override
    public DictionaryFileContentDto getRandom(DictionaryCategory category) {
        return dictionaryService.getRandom(category);
    }
}
