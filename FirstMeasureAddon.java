import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FirstMeasureAddon extends Measure {
    private BufferedImage trebleClefImage;
    private BufferedImage bassClefImage;
    private BufferedImage timeSignatureImage;

    private int trebleScale = 10;
    private int bassScale = 25;
    private int timeScale = 20;

    private final double TREBLE_CLEF_HORIZONTAL_POSITION_RATIO = 1.0 / 3.0;
    private final double TREBLE_CLEF_VERTICAL_POSITION_RATIO = 2.0 / 6.0;

    private final double BASS_CLEF_HORIZONTAL_POSITION_RATIO = 1.0 / 3.0;
    private final double BASS_CLEF_VERTICAL_POSITION_RATIO = 4.0 / 6.0;

    private final double TIME_SIGNATURE_HORIZONTAL_POSITION_RATIO = 2.0 / 3.0;
    private final double TIME_SIGNATURE_VERTICAL_POSITION_RATIO_1 = 2.0 / 6.0;
    private final double TIME_SIGNATURE_VERTICAL_POSITION_RATIO_2 = 4.0 / 6.0;

    public FirstMeasureAddon() {
        // Load images
        try {
            trebleClefImage = ImageIO.read(new File("Sprites/TrebleClef.png"));
            bassClefImage = ImageIO.read(new File("Sprites/BassCleff.png"));
            timeSignatureImage = ImageIO.read(new File("Sprites/4-4.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Remove mouse listeners from inherited constructor
        for (MouseListener listener : getMouseListeners()) {
            removeMouseListener(listener);
        }
        for (MouseMotionListener listener : getMouseMotionListeners()) {
            removeMouseMotionListener(listener);
        }
    }

    @Override
    public void drawVerticalBar(Graphics g, int x, int y, int height) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawClefsAndTimeSignature(g);
    }

    private void drawClefsAndTimeSignature(Graphics g) {
        // Calculate scaled dimensions for each image
        int scaledTrebleClefWidth = trebleClefImage.getWidth() / trebleScale;
        int scaledTrebleClefHeight = trebleClefImage.getHeight() / trebleScale;

        int scaledBassClefWidth = bassClefImage.getWidth() / bassScale;
        int scaledBassClefHeight = bassClefImage.getHeight() / bassScale;

        int scaledTimeSignatureWidth = timeSignatureImage.getWidth() / timeScale;
        int scaledTimeSignatureHeight = timeSignatureImage.getHeight() / timeScale;

        // Calculate positions
        int measureWidth = getWidth();
        int measureHeight = getHeight();

        // Treble Clef positions
        int trebleClefX = (int) (measureWidth * TREBLE_CLEF_HORIZONTAL_POSITION_RATIO);
        int trebleClefY = (int) (measureHeight * TREBLE_CLEF_VERTICAL_POSITION_RATIO) - scaledTrebleClefHeight / 2;

        // Bass Clef positions
        int bassClefX = (int) (measureWidth * BASS_CLEF_HORIZONTAL_POSITION_RATIO);
        int bassClefY = (int) (measureHeight * BASS_CLEF_VERTICAL_POSITION_RATIO) - scaledBassClefHeight / 2;

        // Time Signature positions
        int timeSignatureX = (int) (measureWidth * TIME_SIGNATURE_HORIZONTAL_POSITION_RATIO);
        int timeSignatureY1 = (int) (measureHeight * TIME_SIGNATURE_VERTICAL_POSITION_RATIO_1) - scaledTimeSignatureHeight / 2;
        int timeSignatureY2 = (int) (measureHeight * TIME_SIGNATURE_VERTICAL_POSITION_RATIO_2) - scaledTimeSignatureHeight / 2;

        // Draw treble clef
        if (trebleClefImage != null) {
            g.drawImage(trebleClefImage, trebleClefX - (scaledTrebleClefWidth / 2), trebleClefY, scaledTrebleClefWidth, scaledTrebleClefHeight, this);
        }

        // Draw bass clef
        if (bassClefImage != null) {
            g.drawImage(bassClefImage, bassClefX - (scaledBassClefWidth / 2), bassClefY, scaledBassClefWidth, scaledBassClefHeight, this);
        }

        // Draw time signature
        if (timeSignatureImage != null) {
            g.drawImage(timeSignatureImage, timeSignatureX - (scaledTimeSignatureWidth / 2), timeSignatureY1, scaledTimeSignatureWidth, scaledTimeSignatureHeight, this);
            g.drawImage(timeSignatureImage, timeSignatureX - (scaledTimeSignatureWidth / 2), timeSignatureY2, scaledTimeSignatureWidth, scaledTimeSignatureHeight, this);
        }
    }
}
