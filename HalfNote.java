public class HalfNote extends Note {
    private final static String SPRITE_PATH = "Sprites/HalfNote.png";
    private final static int SCALE = 6;
    private final static int OFFSET_X = 0;
    private final static int OFFSET_Y = 11;
    private final static int VALUE = 2;

    public HalfNote(int x, int y,int pitch) {
        super(x, y, pitch, SPRITE_PATH, SCALE, OFFSET_X, OFFSET_Y, VALUE);
    }
}