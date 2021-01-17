import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RepositoryFrame extends JFrame {
    private ImageDisplayPanel imageDisplayPanel;
    private ImageSelectionPanel imageSelectionPanel;
    private LoginPanel loginPanel;
    private String filePath;

    public RepositoryFrame(String title, String filePath, Connector connector) throws HeadlessException {
        super(title);

        this.filePath = filePath;
        this.imageDisplayPanel = new ImageDisplayPanel(filePath);
        this.imageSelectionPanel = new ImageSelectionPanel();
        this.loginPanel = new LoginPanel();

        getContentPane().add(loginPanel);
        getContentPane().add(imageSelectionPanel);
        getContentPane().add(imageDisplayPanel);

        getContentPane().setLayout(new GridLayout());
        getContentPane().setPreferredSize(new Dimension(500, 500));

        setSize(300, 150);
        pack();
//        repoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                connector.delete();
                System.exit(0);
            }
        });
        loginPanel.populate(this, imageSelectionPanel, connector);
        imageSelectionPanel.populate(this, imageDisplayPanel, connector);
        repaint();
        setVisible(true);

    }

    public ImageDisplayPanel getNewImageDisplayPanel() {
        if (imageDisplayPanel != null) getContentPane().remove(imageDisplayPanel);
        imageDisplayPanel = new ImageDisplayPanel(filePath);
        getContentPane().add(imageDisplayPanel);
        return imageDisplayPanel;
    }

}
