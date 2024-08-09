import java.awt.*;
import javax.swing.*;

public class ToolBar extends JToolBar {

    public static Class<? extends Note> selectedNoteType = WholeNote.class; // Track the selected note type
    private UI ui;

    public ToolBar(UI ui) {
        this.ui = ui;
        setFloatable(false);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        addNoteButtons();
    }

    private void addNoteButtons() {
        // Whole Note Button
        ImageIcon wholeNoteIcon = new ImageIcon("Sprites/WholeNote.png");
        JButton wholeNoteButton = new JButton(scaleImageIcon(wholeNoteIcon, 4)); // Scale by a factor of 4
        wholeNoteButton.setActionCommand("Whole Note");
        wholeNoteButton.addActionListener(e -> selectedNoteType = WholeNote.class); // Set the note type when clicked
        add(wholeNoteButton);

        // Half Note Button
        ImageIcon halfNoteIcon = new ImageIcon("Sprites/HalfNote.png");
        JButton halfNoteButton = new JButton(scaleImageIcon(halfNoteIcon, 4)); // Scale by a factor of 4
        halfNoteButton.setActionCommand("Half Note");
        halfNoteButton.addActionListener(e -> selectedNoteType = HalfNote.class); // Set the note type when clicked
        add(halfNoteButton);

        // Quarter Note Button
        ImageIcon quarterNoteIcon = new ImageIcon("Sprites/QuarterNote.png");
        JButton quarterNoteButton = new JButton(scaleImageIcon(quarterNoteIcon, 4)); // Scale by a factor of 4
        quarterNoteButton.setActionCommand("Quarter Note");
        quarterNoteButton.addActionListener(e -> selectedNoteType = QuarterNote.class); // Set the note type when clicked
        add(quarterNoteButton);

        // Play Button
        ImageIcon PlayIcon = new ImageIcon("Sprites/Play.png");
        JButton PlayButton = new JButton(scaleImageIcon(PlayIcon, 10)); // Scale by a factor of 4
        PlayButton.setActionCommand("Play Button");
        PlayButton.addActionListener(e -> {
            ui.playSong();
        });
        add(PlayButton);
    }

    // Method to scale ImageIcon
    private ImageIcon scaleImageIcon(ImageIcon icon, int scaleFactor) {
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(icon.getIconWidth() / scaleFactor, icon.getIconHeight() / scaleFactor, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }
}
