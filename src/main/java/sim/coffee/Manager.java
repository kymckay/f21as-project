package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;

public class Manager {
    public static void main(String[] args) {
        // Queue of orders populated by producer for staff to serve
        SharedQueue orders = new SharedQueue();

        // If input menu or orders file isn't found program cannot continue
        try {
            // Producer inserts input file of orders into shared queue for staff
            Thread producer = new Thread(new Producer(
                new File("data/orders.csv"),
                new Menu("data/menu.csv"),
                orders
            ));

            producer.start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Thread server1 = new Thread(new Server(orders));
        Thread server2 = new Thread(new Server(orders));

        server1.start();
        server2.start();
    }
}