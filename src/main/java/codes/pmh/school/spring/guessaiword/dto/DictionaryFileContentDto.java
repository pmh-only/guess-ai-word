package codes.pmh.school.spring.guessaiword.dto;

import codes.pmh.school.spring.guessaiword.enums.DictionaryCategory;

public class DictionaryFileContentDto {
    private DictionaryCategory category;

    private String content;

    public DictionaryCategory getCategory() {
        return category;
    }

    public void setCategory(DictionaryCategory category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
