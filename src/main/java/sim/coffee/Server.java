package sim.coffee;

public class Server implements Runnable{
    private SharedQueue queue;
    private Logger log;

    public Server(SharedQueue queue) {
        this.queue = queue;
        log = Logger.getInstance();
    }

    /**
     * Run only while its not done and queue isn't empty
     */
    @Override
    public void run() {
        while (!queue.getDone()) {
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
