package sim.coffee;

public class Server implements Runnable{
    private SharedQueue queue;

    public Server(SharedQueue queue) {
        this.queue = queue;
    }

    /**
     * Run only while its not done and queue isn't empty
     */
    @Override
    public void run() {
        int queueSize = queue.getCustomerOrder().length;
        while (!queue.getDone() && queueSize > 0) {
            try {
                // Tries to simulate no. of orders * time it take to prepare one order
                Thread.sleep(2000*queueSize);
            } catch (InterruptedException e) {}
        }
    }
}
