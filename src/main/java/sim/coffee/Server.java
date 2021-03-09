package sim.coffee;

public class Server implements Runnable{
    private Queue queue;

    public Server(Queue queue) {
        this.queue = queue;
    }

    public void run() {
        while (!queue.getDone) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
        queue.setDone()
    }
    
}
