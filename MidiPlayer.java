import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.midi.*;

public class MidiPlayer {
    private static final int DEFAULT_INSTRUMENT = 0; // Default instrument (piano)
    private static final int DEFAULT_VELOCITY = 64; // Default velocity (volume)

    private static final int NOTE_ON = 0x90; // MIDI message for note on
    private static final int NOTE_OFF = 0x80; // MIDI message for note off

    private static Sequencer sequencer;
    private static Sequence sequence;
    private static Track track;

    static {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4); // 4 ticks per quarter note
            track = sequence.createTrack();
            sequencer.setSequence(sequence);

            sequencer.addMetaEventListener(new MetaEventListener() {
                @Override
                public void meta(MetaMessage meta) {
                    if (meta.getType() == 47) { // End of track event
                        clearSequence(); // Clear the sequence
                    }
                }
            });
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public static void playSong(HashMap<Double, ArrayList<Note>> notes) {
        for (HashMap.Entry<Double, ArrayList<Note>> e : notes.entrySet()) {
            for (Note n : e.getValue()) {
                addNoteToSequence(n, (long) Math.floor(e.getKey() * Measure.NOTES_PER_MEASURE));
            }
        }
        playSequence();
    }


    public static void addNoteToSequence(Note note, long index) {
        if (sequence == null) {
            try {
                sequence = new Sequence(Sequence.PPQ, 4); // 4 ticks per quarter note
                track = sequence.createTrack();
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
                return;
            }
        }

        int channel = 0; // MIDI channel (0-15)
        int noteValue = note.getPitch();
        int velocity = DEFAULT_VELOCITY;

        // Calculate the tick value based on the index and the tempo of the sequence
        long tick = index * sequence.getResolution();

        try {
            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(NOTE_ON, channel, noteValue, velocity);
            track.add(new MidiEvent(noteOn, tick)); // Note on at the specified tick

            // Calculate the duration of the note in ticks
            long durationTicks = (long) (sequence.getResolution() * note.getDuration());

            // Adding a note off event after the calculated duration
            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(NOTE_OFF, channel, noteValue, 0);
            track.add(new MidiEvent(noteOff, tick + durationTicks)); // Note off after the calculated duration
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public static void clearSequence() {
        sequence.deleteTrack(track); // Delete the track from the sequence
        track = sequence.createTrack(); // Recreate the track
    }

    public static void playSequence() {
        if (sequencer != null) {
            if (sequencer.isRunning()) {
                sequencer.stop();
            }
            sequencer.setMicrosecondPosition(0); // Reset the sequencer position
            try {
                sequencer.setSequence(sequence);
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
                return;
            }
            sequencer.start();
        }
    }

    public static void stopSequence() {
        if (sequencer != null && sequencer.isRunning()) {
            sequencer.stop();
            sequencer.setMicrosecondPosition(0); // Reset the sequencer position
        }
    }

    public static void close() {
        if (sequencer != null) {
            sequencer.close();
            sequence = null; // Reset sequence when closing
        }
    }
}
