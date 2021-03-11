package sim.coffee;

public class Server implements Runnable{
    private SharedQueue queue;

    public Server(SharedQueue queue) {
        this.queue = queue;
    }

    public void run() {
        while (!queue.getDone()) {
            try {
                queue.getCustomerOrder();
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
        
    }
}
