package sim.views;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import sim.interfaces.Observer;
import sim.model.Customer;
import sim.model.SharedQueue;

public class QueueLaneGUI extends JPanel implements Observer {

    SharedQueue queue;
    JTextArea display;
    int lane;

    public QueueLaneGUI(SharedQueue queue, int lane, String title) {
        this.queue = queue;
        this.lane = lane;
        queue.registerObserver(this);
        setLayout(new GridLayout(1,1)); // ensures JPanel takes up all available space in the GUI
        setup(title);
    }

    public void setup(String title) {
        // Text area provides convenient wrapping
        display = new JTextArea();
        display.setEditable(false);
        // fixed width font ensure String.format alignment functions correctly
        display.setFont(new Font("Monospaced", Font.PLAIN, 13));

        // Scroll pane remove limit on content height
        JScrollPane queuePane = new JScrollPane(display);

        // Identify the queue for the user
        Border border = BorderFactory.createTitledBorder(title);
        queuePane.setBorder(border);

        add(queuePane);

        // Fill initial state
        update();
    }

    @Override
    public void update() {
        List<Customer> currentQueue = queue.getLane(lane);

        StringBuilder queueLog = new StringBuilder();
         queueLog.append(String.format("Orders in the queue: %d%n", currentQueue.size()));

         for (Customer c : currentQueue) {
             queueLog.append(String.format("%-21s", c.getName()));
             queueLog.append(c.getOrder().length);
             queueLog.append(" Item(s) \n");
         }
        display.setText(queueLog.toString());
    }

}
