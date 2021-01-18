import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RepositoryFrame extends JFrame {
    private ImageDisplayPanel imageDisplayPanel;
    private ImageSelectionPanel imageSelectionPanel;
    private LoginPanel loginPanel;
    private String filePath;

    RepositoryFrame(String title, String filePath, Connector connector) throws HeadlessException {
        super(title);

        this.filePath = filePath;
        this.imageDisplayPanel = new ImageDisplayPanel(filePath, this);
        this.imageSelectionPanel = new ImageSelectionPanel(this, connector);
        this.loginPanel = new LoginPanel(this, connector);

        getContentPane().add(loginPanel);

        getContentPane().setLayout(new GridLayout());
        getContentPane().setPreferredSize(new Dimension(500, 500));

        setSize(300, 150);
        pack();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                connector.delete();
                System.exit(0);
            }
        });
        loginPanel.populate();
        imageSelectionPanel.populate();
        repaint();
        setVisible(true);

    }

    ImageDisplayPanel getNewImageDisplayPanel() {
        if (imageDisplayPanel != null) getContentPane().remove(imageDisplayPanel);
        imageDisplayPanel = new ImageDisplayPanel(filePath, this);
        getContentPane().add(imageDisplayPanel);
        return imageDisplayPanel;
    }

    void removeImageDisplayPanel(){
        getContentPane().remove(imageDisplayPanel);
        imageDisplayPanel = null;
    }

    public ImageDisplayPanel getImageDisplayPanel() {
        return imageDisplayPanel;
    }

    public ImageSelectionPanel getImageSelectionPanel() {
        return imageSelectionPanel;
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }
    @Override
    public void repaint(){

        if(Session.getStatus().equals(Session.Status.LOGIN) && !imageSelectionPanel.isVisible()){
            remove(loginPanel);
            add(imageSelectionPanel);
            imageSelectionPanel.setVisible(true);
            loginPanel.setVisible(false);
        }
        if(Session.getStatus().equals(Session.Status.LOGOUT) && !loginPanel.isVisible()){
            remove(imageSelectionPanel);
            add(loginPanel);
            loginPanel.setVisible(true);
            imageSelectionPanel.setVisible(false);
        }
        super.repaint();
    }
}
