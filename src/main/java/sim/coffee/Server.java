package sim.coffee;

public class Server implements Runnable{
    private SharedQueue queue;
    private Logger log;

    public Server(SharedQueue queue) {
        this.queue = queue;
        log = Logger.getInstance();
    }

    @Override
    public void run() {
        // Service continues as long as customers are still due to arrive or customers
        // are in the queue
        while (!queue.getDone() || !queue.isEmpty()) {
            Order [] order = queue.getCustomerOrder();
        	try {
                // Tries to simulate no. of orders * time it take to prepare one order
                int orderSize = order.length;
                Thread.sleep(5000 * orderSize);
                
            } catch (InterruptedException e) {}
        }
        log.writeReport("log.txt");
    }
}
