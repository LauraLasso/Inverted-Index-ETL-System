package software.ulpgc.bigData.InvertedIndex.CleanBooks;

import javax.jms.JMSException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        software.ulpgc.bigData.InvertedIndex.CleanBooks.Controller controller = new Controller();
        executorService.execute(() -> {
            try {
                controller.execute();
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
