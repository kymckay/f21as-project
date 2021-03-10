package sim.coffee;

public class Server implements Runnable{
    private Queue queue;

    public Server(Queue queue) {
        this.queue = queue;
    }

    public void run() {
        while (queue.getCustomerCount() == 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
        queue.getCustomerOrder();
        if (queue.getDone()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        } else {
            queue.setDone();
        }
    }
    
}
