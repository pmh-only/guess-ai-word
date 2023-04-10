package codes.pmh.school.spring.guessaiword.dictionary;

import codes.pmh.school.spring.guessaiword.dictionary.dto.DictionaryFileContentDto;
import codes.pmh.school.spring.guessaiword.dictionary.enums.DictionaryCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("dictionaryService")
public class DictionaryService {
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private List<DictionaryFileContentDto> words;

    private List<DictionaryFileContentDto> askPrompts;

    public DictionaryService () throws IOException {
        loadDictionaryResource();
    }

    private void loadDictionaryResource () throws IOException {
        Resource wordDictionaryResource =
                resourceLoader.getResource("classpath:dicts/word.json");

        Resource askPromptsDictionaryResource =
                resourceLoader.getResource("classpath:dicts/ask_prompt.json");

        this.words =
                parseDictionaryResource(wordDictionaryResource);

        this.askPrompts =
                parseDictionaryResource(askPromptsDictionaryResource);
    }

    private List<DictionaryFileContentDto> parseDictionaryResource (Resource dictionaryResource) throws IOException {
        InputStream dictionaryInputStream = dictionaryResource.getInputStream();
        String dictionaryString = parseDictionaryInputStream(dictionaryInputStream);

        return parseDictionaryString(dictionaryString);
    }

    private List<DictionaryFileContentDto> parseDictionaryString (String dictionaryString) throws JsonProcessingException {
        return new ObjectMapper().readValue(
                        dictionaryString,
                        new TypeReference<List<DictionaryFileContentDto>>() {});
    }

    private String parseDictionaryInputStream (InputStream dictionaryInputStream) throws IOException {
        byte[] dictionaryBytes = dictionaryInputStream.readAllBytes();
        return new String(dictionaryBytes, StandardCharsets.UTF_8);
    }

    public String getRandomWord (DictionaryCategory category) {
        List<DictionaryFileContentDto> filteredDictionary = this.words;

        if (category != DictionaryCategory.ANY)
                filteredDictionary = this.words
                    .stream()
                    .filter(v -> v.getCategory() == category)
                    .collect(Collectors.toList());

        Collections.shuffle(filteredDictionary);

        return filteredDictionary.get(0).getContent();
    }

    public String getRandomAskPrompt (DictionaryCategory category) {
        List<DictionaryFileContentDto> filteredDictionary = this.askPrompts;

        if (category != DictionaryCategory.ANY)
            filteredDictionary = this.askPrompts
                    .stream()
                    .filter(v -> v.getCategory() == category)
                    .collect(Collectors.toList());

        Collections.shuffle(filteredDictionary);

        return filteredDictionary.get(0).getContent();
    }
}
