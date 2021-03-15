package sim.coffee;

public class KitchenStaff implements Runnable{
    private SharedQueue queue;

    public KitchenStaff(SharedQueue queue) {
        this.queue = queue;
    }

    /**
     * Run only while its not done and queue isn't empty.
     * Should be the same as server just that it takes longer to prepare
     * food as compare to serve food.
     */
    @Override
    public void run() {
        while (!queue.getDone() && !queue.isEmpty()) {
            try {
                // Tries to simulate no. of orders * time it take to prepare one order
                int orderSize = queue.getCustomerOrder().length;
                Thread.sleep(60000*orderSize);
            } catch (InterruptedException e) {}
        }
    }
}