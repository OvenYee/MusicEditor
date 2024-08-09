import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Measure extends JComponent {

    protected static final int CLEF_LINES_COUNT = 5;
    protected static final int CLEF_LINES_GAP = 10;
    protected static final int CLEF_GAP = 25;
    protected static final int VERTICAL_BAR_WIDTH = 1;

    public static final int NOTES_PER_MEASURE = 4;
    private double HORIZONTAL_NOTE_RATIO = 1.0 / 8.0;

    private HashMap<Double, ArrayList<Note>> notes = new HashMap<Double, ArrayList<Note>>();

    private Note hoveredNote = null;
    private boolean isDraggingNote = false;
    private Note dragNote = null;

    private ArrayList<Integer> pitchList = new ArrayList<Integer>();

    public Measure() {

        // Define the array of MIDI numbers without sharps
        Integer[] midiArray = {
                28, 29, 31, 33, 35,
                36, 38, 40, 41, 43, 45, 47,
                48, 50, 52, 53, 55, 57, 59,
                60, 60, 62, 64, 65, 67, 69,
                71, 72, 74, 76, 77, 79, 81,
                83, 84, 86, 88, 89, 91, 93
        };

        // Add all MIDI numbers without sharps from the array to the ArrayList
        pitchList.addAll(Arrays.asList(midiArray));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Left mouse button clicked
                if (SwingUtilities.isLeftMouseButton(e)) {
                    //Check for note under
                    if(isNoteUnder(e)){
                        isDraggingNote = true;
                        deleteNote(dragNote.getX(), dragNote.getY());
                    }
                    else{
                        placeNote(e.getX(), e.getY(),true, getCurrentNoteType()); // Call placeNote function
                    }
                }
                // Right mouse button clicked
                else if (SwingUtilities.isRightMouseButton(e)) {
                    deleteNote(e.getX(), e.getY()); // Call deleteNote function
                }
            }
        });

        // Add mouse motion listener for dragging notes
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(isDraggingNote){

                    //Create hover for dragging note
                    placeNote(e.getX(),e.getY(),false,getDragNoteType());
                    repaint(); // Repaint to reflect the change

                }
                // Ensure the event is not consumed
                super.mouseDragged(e);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(isDraggingNote){
                    placeNote(e.getX(), e.getY(), true, getDragNoteType());
                    isDraggingNote = false;
                    dragNote = null;
                }

            }
        });

        // Add hovered note
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Constructor<? extends Note> noteType = getCurrentNoteType();
                if(isDraggingNote){
                    noteType = getDragNoteType();
                }
                placeNote(e.getX(),e.getY(),false,noteType);
                repaint(); // Repaint to reflect the change
            }
        });

        //Remove hovered note on leave
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredNote = null; // Set hoveredNote to null when mouse exits the component bounds
                repaint(); // Repaint to reflect the change
            }
        });

    }

    private Constructor<? extends Note> getCurrentNoteType() {
        if (ToolBar.selectedNoteType != null) {
            try {
                // Get note class from ToolBar
                return ToolBar.selectedNoteType.getDeclaredConstructor(int.class, int.class, int.class);
            } catch (Exception ex) {
                ex.printStackTrace(); // Print the exception stack trace for debugging
            }
        }
        return null; // Return null if no note type is selected or if an exception occurs
    }

    private Constructor<? extends Note> getDragNoteType(){
        try {
            // Get note class from dragNote
            return dragNote.getClass().getDeclaredConstructor(int.class, int.class, int.class);
        } catch (Exception ex) {
            ex.printStackTrace(); // Print the exception stack trace for debugging
        }
        return null;
    }

    private boolean isNoteUnder(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        int distanceThreshold = 4;
    
        // Iterate through all notes to check if any is under the mouse position
        for (ArrayList<Note> notesInColumn : notes.values()) {
            for (Note note : notesInColumn) {
                // Calculate the distance between the mouse position and the note's position
                double distance = Math.sqrt(Math.pow(mouseX - note.getX(), 2) + Math.pow(mouseY - note.getY(), 2));
                // Check if the distance is within the threshold
                if (distance <= distanceThreshold) {
                    dragNote = note;
                    return true;
                }
            }
        }
    
        // No note found under the mouse position within the threshold
        return false;
    }

    private void placeNote(int x, int y, boolean real, Constructor<? extends Note> noteType) {
        double horizontalShift = HORIZONTAL_NOTE_RATIO * getWidth();
        int newX = (int) (snappedXPosition(x) * getWidth() / NOTES_PER_MEASURE + horizontalShift);
        int newY = snappedYPosition(y);
        int pitch = pitchList.get(39 - (newY - 2) / 5);
    
        if (ToolBar.selectedNoteType != null) {
            try {
                // Get note class from ToolBar
                Note note = noteType.newInstance(newX, newY, pitch);
                if (real) {
                    // Check if placement of note is legal
                    if (snappedXPosition(x) + note.getDuration() > NOTES_PER_MEASURE) {
                        return; // Exceeds measure width
                    }
    
                    // Iterate through each column in the measure
                    for (int i = 0; i < NOTES_PER_MEASURE; i++) {
                        ArrayList<Note> notesInColumn = notes.get((double) i / NOTES_PER_MEASURE);
                        if (notesInColumn != null) {
                            for (Note n : notesInColumn) {
                                // Check if the pitch matches an existing note in the column
                                if (n.getPitch() == pitch) {
                                    int columnInMeasure = n.getX() * NOTES_PER_MEASURE / getWidth();
                                    if (columnInMeasure == snappedXPosition(x)) {
                                        return; // Overlaps with an existing note
                                    } else if (columnInMeasure < snappedXPosition(x)) {
                                        if (columnInMeasure + n.getDuration() > snappedXPosition(x)) {
                                            return; // Overlaps with an existing note before
                                        }
                                    } else {
                                        if (snappedXPosition(x) + note.getDuration() > columnInMeasure) {
                                            return; // Overlaps with an existing note after
                                        }
                                    }
                                }
                            }
                        }
                    }
    
                    // Add the note if no overlap is found
                    addNote(snappedXPosition(x), note);
                } else {
                    hoveredNote = note;
                }
    
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    

    private void deleteNote(int x, int y) {
        // Find the column index based on the snapped X position
        int columnIndex = snappedXPosition(x);
        ArrayList<Note> notesInColumn = notes.get((double) columnIndex / NOTES_PER_MEASURE);
    
        if (notesInColumn != null) {
            // Initialize variables to track the closest note and its distance
            Note closestNote = null;
            double minDistance = Double.MAX_VALUE;
    
            // Iterate through the notes in the column to find the closest one to the given Y position
            for (Note note : notesInColumn) {
                double distance = Math.abs(note.getY() - y);
                if (distance < minDistance) {
                    // Update the closest note and its distance
                    closestNote = note;
                    minDistance = distance;
                }
            }
    
            // If a closest note is found, remove it from the column list
            if (closestNote != null) {
                notesInColumn.remove(closestNote);
                repaint(); // Repaint the component to reflect the changes
            }
    
            if (notesInColumn.isEmpty()) {
                notes.remove((double) columnIndex / NOTES_PER_MEASURE);
            }
        }
    }

    private void addNote(int column,Note note) {

        if (notes.containsKey((double) column / NOTES_PER_MEASURE)) {
            notes.get((double) column / NOTES_PER_MEASURE).add(note);
        }
        else {
            ArrayList<Note> notesInColumn = new ArrayList<Note>();
            notesInColumn.add(note);
            notes.put((double) column / NOTES_PER_MEASURE, notesInColumn);
        }
    }

    public HashMap<Double, ArrayList<Note>> getNotes() {
        return notes;
    }

    private int snappedXPosition(int xPos) {
        // Calculate the snapped X position based on the spacing of notes
        int columnWidth = getWidth() / NOTES_PER_MEASURE;

        // Calculate the column index and ensure it is within the range [0, 3]
        int columnIndex = (xPos / columnWidth) % NOTES_PER_MEASURE;

        return columnIndex;
    }

    private int snappedYPosition(int yPos) {
        // Calculate the distance from yPos to the nearest line
        int distanceToLine = yPos % 5;
        // Snap yPos to the nearest line or open space
        if (distanceToLine <= 5) {
            // Snap down to the nearest line
            yPos -= distanceToLine;
        } else {
            // Snap up to the nearest line
            yPos += (5 - distanceToLine);
        }
        return yPos+2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw notes in measure
        for (ArrayList<Note> notesInColumn : notes.values()) {
            for (Note note : notesInColumn) {
                note.draw(g); // Assuming the Note class has a draw method
            }
        }

        if (hoveredNote != null) {
            hoveredNote.draw(g); // Draw the hovered note
        }

        // Draw clefs
        draw(g);
    }

    private void draw(Graphics g) {
        int totalHeight = CLEF_GAP + CLEF_LINES_GAP * (CLEF_LINES_COUNT - 1) * 2;
        int y = getHeight() / 2 - totalHeight / 2;

        // Draw treble clef
        drawClefLines(g, y);

        // Draw bass clef with a gap
        drawClefLines(g, y + CLEF_GAP + CLEF_LINES_GAP * (CLEF_LINES_COUNT - 1));

        // Draw vertical bar at the end of the measure
        drawVerticalBar(g, getWidth() - VERTICAL_BAR_WIDTH / 2 - 1, y, totalHeight);
    }

    private void drawClefLines(Graphics g, int y) {
        // Draw the clef lines
        for (int i = 0; i < CLEF_LINES_COUNT; i++) {
            g.drawLine(0, y + i * CLEF_LINES_GAP, getWidth(), y + i * CLEF_LINES_GAP);
        }
    }

    protected void drawVerticalBar(Graphics g, int x, int y, int height) {
        Graphics2D g2d = (Graphics2D) g;
        Stroke originalStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(VERTICAL_BAR_WIDTH));
        g2d.drawLine(x, y + VERTICAL_BAR_WIDTH / 2, x, y + height - VERTICAL_BAR_WIDTH / 2);
        g2d.setStroke(originalStroke);
    }
}