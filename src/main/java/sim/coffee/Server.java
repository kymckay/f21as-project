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
        while (!queue.getDone() && !queue.isEmpty()) {
            try {
                // Tries to simulate no. of orders * time it take to prepare
                int queueSize = queue.getCustomerOrder().length;
                Thread.sleep(200*queueSize);
            } catch (InterruptedException e) {}
        }
    }
}
