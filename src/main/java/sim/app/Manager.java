package sim.app;

import java.io.File;
import sim.view.*;
import sim.model.*;

public class Manager {
    public static void main(String[] args) {
        // Queue of orders populated by producer for staff to serve
        SharedQueue orders = new SharedQueue(QueueType.CUSTOMER);
        SharedQueue kitchen = new SharedQueue(QueueType.KITCHEN);

        // Producer inserts input file of orders into shared queue for staff
        Thread producer = new Thread(new Producer(new File("data/orders.csv"), orders));

        producer.start();

        new SimulationGUI(orders, kitchen, 3);
    }
}