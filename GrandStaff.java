import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GrandStaff extends JPanel {
    public static final int MEASURE_AMOUNT = 4;

    public GrandStaff() {
        setPreferredSize(new Dimension(800, 200));

        setLayout(new GridBagLayout());  // Use GridBagLayout for more control

        GridBagConstraints gbc = new GridBagConstraints();

        // Adding first measure addon with half the width
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        FirstMeasureAddon firstMeasureAddon = new FirstMeasureAddon();
        add(firstMeasureAddon, gbc);

        // Reset constraints for the measures
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        // Adding measures
        for (int i = 0; i < MEASURE_AMOUNT; i++) {
            Measure measure = new Measure();
            add(measure, gbc);
        }
    }

    public HashMap<Double, ArrayList<Note>> getNotes() {
        int count = 0;
        HashMap<Double, ArrayList<Note>> notes = new HashMap<Double, ArrayList<Note>>();
        for (Component c : getComponents()) {
            if (!(c instanceof FirstMeasureAddon)) {
                for (HashMap.Entry<Double, ArrayList<Note>> e : ((Measure) c).getNotes().entrySet()) {
                    notes.put(e.getKey() + count, e.getValue());
                }
                count += 1;
            }
        }
        return notes;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Component c : getComponents()) {
            c.repaint();
        }
    }
}