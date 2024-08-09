import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Map;

public class UI {
    private JFrame frame;
    private JPanel grandStaffPanel;

    public UI(JFrame frame) {
        this.frame = frame;
    }

    public void initUI() {

        // Initialize and add the tool bar
        ToolBar toolBar = new ToolBar(this);
        frame.add(toolBar, BorderLayout.NORTH);

        // Create a panel to hold the grand staffs
        grandStaffPanel = new JPanel(new GridBagLayout());

        // Add the initial grand staff
        addNewGrandStaff(grandStaffPanel, 0);

        // Create a scroll pane to hold the grand staff panel
        JScrollPane scrollPane = new JScrollPane(grandStaffPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Increase scroll speed
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(20); // Increase unit increment
        verticalScrollBar.setBlockIncrement(100); // Increase block increment

        // Add a small gap on each side
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10)); // Adjust gap size as needed

        // Add the scroll pane to the frame
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add the "+" button
        JButton addButton = new JButton("+");
        addButton.addActionListener(e -> addNewGrandStaff(grandStaffPanel, grandStaffPanel.getComponentCount()));
        frame.add(addButton, BorderLayout.SOUTH);
    }

    private void addNewGrandStaff(JPanel grandStaffPanel, int gridy) {
        // Create a new GrandStaff and add it to the panel
        GrandStaff grandStaff = new GrandStaff();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        grandStaffPanel.add(grandStaff, gbc);
        grandStaffPanel.revalidate();
        grandStaffPanel.repaint();
    }

    public void playSong() {
        int count = 0;
        HashMap<Double, ArrayList<Note>> notes = new HashMap<Double, ArrayList<Note>>();
        for (Component c : grandStaffPanel.getComponents()) {
            if (c instanceof GrandStaff) {
                for (HashMap.Entry<Double, ArrayList<Note>> e : ((GrandStaff) c).getNotes().entrySet()) {
                    notes.put(e.getKey() + count, e.getValue());
                }
                count += GrandStaff.MEASURE_AMOUNT;
            }
        }

        MidiPlayer.playSong(notes);
    }
}