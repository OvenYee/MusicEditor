import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

abstract class Note {
    // Position
    private int x;
    private int y;

    // Sprite
    private BufferedImage sprite;
    private int scale;
    private int offsetX;
    private int offsetY;

    private int pitch;
    private int value;

    public Note(int x, int y, int pitch, String p, int s, int oX, int oY, int v) {
        this.x = x;
        this.y = y;
        this.pitch = pitch;
        this.scale = s;
        this.offsetX = oX;
        this.offsetY = oY;
        this.value = v;
        try {
            this.sprite = ImageIO.read(new File(p));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        if (sprite != null) {
            // Shrink the sprite by half
            int width = sprite.getWidth() / scale;
            int height = sprite.getHeight() / scale;

            g.drawImage(sprite, x-width/2 -offsetX, y-height/2-offsetY, width, height, null);
        }
    }

    public int getPointValue() {
        return value;
    }

    public int getX() {
        return x;
    };

    public int getY() {
        return y;
    };

    public int getPitch(){return pitch;}

    public int getDuration(){return value;}
}