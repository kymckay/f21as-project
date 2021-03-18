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
                // Time to process order depends on number of items
                Thread.sleep(10000l * order.length);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        	log.add(order, Logger.OrderState.PROCESSED);
        }
        log.writeReport("log.txt");
    }
}
