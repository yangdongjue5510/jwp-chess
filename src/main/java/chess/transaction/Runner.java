package chess.transaction;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements ApplicationRunner {

    private final OuterService outerService;

    public Runner(OuterService outerService) {
        this.outerService = outerService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {

            outerService.doSomething();
        } catch (Exception e) {
            System.out.println("catched!");
        }
    }
}
