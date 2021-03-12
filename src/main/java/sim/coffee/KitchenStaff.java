package sim.coffee;

public class KitchenStaff implements Runnable{
    private SharedQueue queue;

    public KitchenStaff(SharedQueue queue) {
        this.queue = queue;
    }


}