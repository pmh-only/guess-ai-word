package codes.pmh.school.spring.guessaime.controller;

import codes.pmh.school.spring.guessaime.ai.AIAskResult;
import codes.pmh.school.spring.guessaime.ai.AIAsker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private AIAsker aiAsker;

    @GetMapping("/test")
    public AIAskResult getAIAsk (@RequestParam String q) {
        return aiAsker.ask(q);
    }
}
