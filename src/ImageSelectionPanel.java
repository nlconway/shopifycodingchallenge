import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

//Panel displays options to interact with the database
//Can add image, get specific image by name or all images belonging to user
class ImageSelectionPanel extends JPanel {
    private RepositoryFrame repoFrame;
    private Connector connector;

    ImageSelectionPanel(RepositoryFrame repoFrame, Connector connector) {
        this.repoFrame = repoFrame;
        this.connector = connector;
    }

    //Add components of image selection panel
    void populate() {
        setLayout(null);
        JLabel addImageUrl = new JLabel();
        addImageUrl.setText("URL");
        addImageUrl.setBounds(10, 20, 80, 25);
        JTextField addImageUrlText = new JTextField(25);
        addImageUrlText.setBounds(100, 20, 165, 25);
        JLabel addImage = new JLabel();
        addImage.setText("Name");
        addImage.setBounds(10, 50, 80, 25);
        JTextField addImageText = new JTextField(25);
        addImageText.setBounds(100, 50, 165, 25);
        JButton addImageButton = new JButton("Add Image");
        addImageButton.setBounds(275, 50, 165, 25);

        JLabel getImage = new JLabel();
        getImage.setText("Image Name");
        getImage.setBounds(10, 80, 80, 25);
        JTextField getImageText = new JTextField(25);
        getImageText.setBounds(100, 80, 165, 25);
        JButton getImageButton = new JButton("Get Image");
        getImageButton.setBounds(275, 80, 165, 25);

        JLabel getAllImages = new JLabel();
        getAllImages.setText("Get All Images");
        getAllImages.setBounds(10, 120, 85, 25);
        JButton getAllImagesButton = new JButton("Get Images");
        getAllImagesButton.setBounds(100, 120, 165, 25);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(10, 150, 165, 25);

        add(addImageUrl);
        add(addImageUrlText);
        add(addImage);
        add(addImageText);
        add(addImageButton);
        add(getImage);
        add(getImageText);
        add(getImageButton);
        add(getAllImages);
        add(getAllImagesButton);
        add(logoutButton);

        //Checks if session is logged in (panel shouldn't be visible but extra security)
        //Passes image url and name to connector,
        //If adding image was successful sets text blank again
        addImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (Session.getStatus().equals(Session.Status.LOGOUT)) return;
                if (connector.addImageFromUrl(addImageUrlText.getText(), addImageText.getText()) > 0) {
                    addImageUrlText.setText("");
                    addImageText.setText("");
                }
                repoFrame.repaint();
            }
        });

        //Using the name passed, checks if there is any image by the name belonging to the user
        //If so text is set to blank
        //Display panel is created populated with the image
        getImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (Session.getStatus().equals(Session.Status.LOGOUT)) return;
                Image image = connector.getImage(getImageText.getText(), Session.getActiveUser().getId());
                if (image != null) {
                    java.util.List<Image> imageList = new ArrayList<>();
                    imageList.add(image);
                    ImageDisplayPanel imageDisplayPanel = repoFrame.getNewImageDisplayPanel();
                    imageDisplayPanel.populate(imageList);
                    getImageText.setText("");
                }
            }
        });

        //Gets all images via the connector,
        //Gets the new display panel from the main frame
        //Populates the new frame wth the image list
        getAllImagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (Session.getStatus().equals(Session.Status.LOGOUT)) return;
                java.util.List<Image> imageList = connector.getImages(Session.getActiveUser().getId());
                ImageDisplayPanel imageDisplayPanel = repoFrame.getNewImageDisplayPanel();
                imageDisplayPanel.populate(imageList);
                repoFrame.repaint();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Session.logout();
                repoFrame.repaint();
            }
        });

        repaint();
        setVisible(false);
    }

}
