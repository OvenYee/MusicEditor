import javax.swing.*;

public class App extends JFrame {

    public App() {
        setTitle("Music Notation Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        UI ui = new UI(this);
        ui.initUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            
            app.pack();
            app.setLocationRelativeTo(null); // Center the frame
            app.setVisible(true);
        });
    }
}
