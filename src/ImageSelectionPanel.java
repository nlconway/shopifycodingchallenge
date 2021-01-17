import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ImageSelectionPanel extends JPanel {
//    ImageDisplayPanel imageDisplayPanel;
    public ImageSelectionPanel(){

    }

    public void populate(RepositoryFrame repoFrame, ImageDisplayPanel imageDisplayPanel, Connector connector){
//        this.imageDisplayPanel = imageDisplayPanel;
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
        getImage.setText("Get Image");
        getImage.setBounds(10, 80, 80, 25);
        JTextField getImageText = new JTextField(25);
        getImageText.setBounds(100, 80, 165, 25);
        JButton getImageButton = new JButton("Get Image");
        getImageButton.setBounds(275, 80, 165, 25);

        JLabel getAllImages = new JLabel();
        getAllImages.setText("Get Images");
        getAllImages.setBounds(10, 120, 80, 25);
        JButton getAllImagesButton = new JButton("Get Images");
        getAllImagesButton.setBounds(100, 120, 165, 25);

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

        addImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (Session.getStatus().equals(Session.Status.LOGOUT)) return;
                if(connector.addImageFromUrl(addImageUrlText.getText(), addImageText.getText()) > 0) {
                    addImageUrlText.setText("");
                    addImageText.setText("");
                }
            }
        });

        getImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (Session.getStatus().equals(Session.Status.LOGOUT)) return;
                Image image = connector.getImage(getImageText.getText(), Session.getUser().getId());
                if (image != null) {
                    java.util.List<Image> imageList = new ArrayList<>();
                    imageList.add(image);
                    ImageDisplayPanel imageDisplayPanel = repoFrame.getNewImageDisplayPanel();
                    imageDisplayPanel.populate(imageList);
                    getImageText.setText("");
                }
            }
        });

        getAllImagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (Session.getStatus().equals(Session.Status.LOGOUT)) return;
                java.util.List<Image> imageList = connector.getImages(Session.getUser().getId());
                ImageDisplayPanel imageDisplayPanel = repoFrame.getNewImageDisplayPanel();
                imageDisplayPanel.populate(imageList);
            }
        });

        repaint();
        setVisible(false);
    }
}
