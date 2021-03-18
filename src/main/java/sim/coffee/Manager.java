package sim.coffee;

import java.io.File;
import java.io.FileNotFoundException;

public class Manager {
    public static void main(String[] args) {
        // Queue of orders populated by producer for staff to serve
        SharedQueue orders = new SharedQueue(QueueType.CUSTOMER);
        SharedQueue kitchen = new SharedQueue(QueueType.KITCHEN);

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
        
        SimulationGUI gui = new SimulationGUI(orders, kitchen, 3); 
        

    }
}