public class WholeNote extends Note {
    private final static String SPRITE_PATH = "Sprites/WholeNote.png";
    private final static int SCALE = 12;
    private final static int OFFSET_X = 0;
    private final static int OFFSET_Y = -1;
    private final static int VALUE = 4;

    public WholeNote(int x, int y,int pitch) {
        super(x, y, pitch, SPRITE_PATH, SCALE, OFFSET_X, OFFSET_Y, VALUE);
    }
}