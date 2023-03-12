package codes.pmh.school.spring.guessaime.ai;

public class AIAskerThread extends Thread {
    private String question;

    private AIAskerResult result;
    
    private AIAskerThread () {}

    public static AIAskerThread createAskTread (String question) {
        AIAskerThread askerThread = new AIAskerThread();
        askerThread.question = question;

        return askerThread;
    }

    public AIAskerResult getResult() {
        return result;
    }

    @Override
    public void run() {
        result = new AIAsker().ask(question);
    }
}
