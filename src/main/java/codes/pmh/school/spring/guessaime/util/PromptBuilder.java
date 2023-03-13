package codes.pmh.school.spring.guessaime.util;

public class PromptBuilder {
    private static final String PROMPT_TEMPLATE =
            "Choose %d %s word randomly and make %d q&a examples per word. output in json array format not object format. chosen word in \"word\" field, q&a in \"qna\" field, questions are in \"result[].question\" field and answers are in \"result[].answer\". no other messages. no code block. in korean.";

    private String wordCategory = "ANY";

    private int wordCount = 1;

    private int qnaCount = 5;

    public PromptBuilder setWordCategory(String wordCategory) {
        this.wordCategory = wordCategory;
        return this;
    }

    public PromptBuilder setWordCount(int wordCount) {
        this.wordCount = wordCount;
        return this;
    }

    public PromptBuilder setQnaCount(int qnaCount) {
        this.qnaCount = qnaCount;
        return this;
    }

    public String build () {
        return String.format(
                PromptBuilder.PROMPT_TEMPLATE,
                this.wordCount,
                this.wordCategory,
                this.qnaCount);
    }
}
