package codes.pmh.school.spring.guessaiword.dictionary;

import codes.pmh.school.spring.guessaiword.dictionary.dto.DictionaryFileContentDto;
import codes.pmh.school.spring.guessaiword.dictionary.enums.DictionaryCategory;

import java.util.List;

public interface DictionaryRandomable {
    public DictionaryFileContentDto getRandom (DictionaryCategory category);

    public List<DictionaryFileContentDto> getRandoms (DictionaryCategory category, int count);
}
