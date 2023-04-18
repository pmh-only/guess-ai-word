package codes.pmh.school.spring.guessaiword.dictionary;

import codes.pmh.school.spring.guessaiword.dictionary.dto.DictionaryFileContentDto;
import codes.pmh.school.spring.guessaiword.dictionary.enums.DictionaryCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DictionaryServiceImpl implements DictionaryRandomable {
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private List<DictionaryFileContentDto> dictionary;

    public DictionaryServiceImpl(String classpath) throws IOException {
        loadDictionaryResource(classpath);
    }

    private void loadDictionaryResource (String classpath) throws IOException {
        Resource dictionaryResource =
                resourceLoader.getResource(classpath);

        this.dictionary =
                parseDictionaryResource(dictionaryResource);
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

    public List<DictionaryFileContentDto> getRandoms(DictionaryCategory category, int count) {
        List<DictionaryFileContentDto> filteredDictionary = this.dictionary;

        if (category != DictionaryCategory.ANY)
            filteredDictionary = this.dictionary
                    .stream()
                    .filter(v -> v.getCategory() == category)
                    .collect(Collectors.toList());

        Collections.shuffle(filteredDictionary);

        return filteredDictionary.subList(0, count);
    }

    public DictionaryFileContentDto getRandom(DictionaryCategory category) {
        return this.getRandoms(category, 1).get(0);
    }
}
