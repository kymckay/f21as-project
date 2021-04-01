package sim.views;

import static javax.swing.SwingConstants.HORIZONTAL;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Hashtable;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import sim.interfaces.Observer;
import sim.model.Customer;
import sim.model.MenuItem;
import sim.model.Server;

public class ServerGUI extends JPanel implements Observer {
    private Server server;

    private JTextArea serverArea;
    private JSlider serverSlider;
    private JLabel label;

    static final int MIN = -5;
    static final int MAX = 5;
    static final int INIT = 0;

    public ServerGUI(Server server) {
        this.server = server;
        server.registerObserver(this);
        setLayout(new GridLayout(2,1));
        add(setupControls());


        setup();
    }

    public void setup() {

        serverArea = new JTextArea();
        serverArea.setEditable(false);
        serverArea.setText("Waiting for orders...");
        serverArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        Border border1 = BorderFactory.createTitledBorder("Server " + server.getNumber());
        JScrollPane serverPane = new JScrollPane(serverArea);
        setBorder(border1);
        add(serverPane);
    }


    public JPanel setupControls() {
        JPanel controls = new JPanel();
        Border margin = new EmptyBorder(5,0,0,0);
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));

        // displays serving speed of a server
        label = new JLabel();
        label.setFont(new Font(null, Font.BOLD, 13));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBackground(new Color(238,238,238,255));
        label.setBorder(margin);

        // Default speed label value on init
        label.setText(String.format("Serving Speed: %ds/Item", Server.BASE_SPEED / 1000));

        // add slider and slider label to a JPanel
        controls.add(label);
        controls.add(setupSlider());

        return controls;
    }

    // sets up server speed controls (i.e., the slider)
    public JSlider setupSlider() {
        serverSlider = new JSlider(HORIZONTAL, MIN, MAX, INIT);

        // slider formatting
        serverSlider.setMajorTickSpacing(5);
        serverSlider.setPaintTicks(true);
        serverSlider.setPaintLabels(true);

        // create a table with custom slider labels
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put( MIN, new JLabel("Slow") );
        labelTable.put( INIT, new JLabel("Normal") );
        labelTable.put( MAX, new JLabel("Fast") );
        serverSlider.setLabelTable( labelTable );

        return serverSlider;
    }

    // Add listener when slider set
    public void addSetListener(ChangeListener ce) {
        serverSlider.addChangeListener(ce);
    }

    public int getSpeed() {
        return serverSlider.getValue();
    }

    public void update() {
        // Update speed label
        label.setText(String.format("Serving Speed: %ds/Item", server.getSpeed() / 1000));

        Optional<Customer> customer = server.getCurrentCustomer();

        // updates the customer queue section
        if (customer.isPresent()) {
            Customer serving = customer.get();

            StringBuilder currentOrder = new StringBuilder();

            currentOrder.append("Customer being served: \n");
            currentOrder.append("   " + serving.getName() + "\n");
            currentOrder.append("Ordered items: \n");
            for (MenuItem item : serving.getOrder()) {
                // capitalize the order name
                currentOrder.append("   " + Character.toUpperCase(item.getName().charAt(0)) + item.getName().substring(1) + "\n");
            }

            serverArea.setText(currentOrder.toString());
        } else {
            serverArea.setText("Waiting for customers...");
        }

        if (server.isDone()) {
            serverArea.setText("Task Complete");
        }
    }

    public Server getModel() {
        return server;
    }
}
