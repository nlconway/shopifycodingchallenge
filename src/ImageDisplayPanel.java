import com.sun.javafx.beans.IDProperty;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class ImageDisplayPanel extends JPanel {
    private int index = 0;
    private JLabel imageLabel;
    private JLabel imageText;
    private String filePath;
    private RepositoryFrame repoFrame;


     ImageDisplayPanel(String filePath, RepositoryFrame repositoryFrame){
        this.filePath = filePath;
        this.repoFrame = repositoryFrame;
    }


     void populate(java.util.List<Image> images){
        setLayout(null);
        JButton nextImageButton = new JButton("Next Image");
        nextImageButton.setBounds(20, 20, 165, 25);
        imageText = new JLabel();
        imageText.setBounds(20, 50, 80, 25);

        if(index < images.size()) {
            try {
                BufferedImage loadedImage = ImageIO.read(new File(filePath + images.get(index).getId()));
                imageText.setText(images.get(index).getName());
                imageLabel = new JLabel(new ImageIcon(loadedImage));
                imageLabel.setBounds(50, 50, 500, 500);
                add(imageLabel);
                index++;
            } catch (IOException e) {
                index++;
            }
        }

        add(imageText);
        add(nextImageButton);
        nextImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(index < images.size()){
                    try {
                        remove(imageLabel);
                        BufferedImage loadedImage = ImageIO.read(new File(filePath + images.get(index).getId()));
                        imageText.setText(images.get(index).getName());
                        imageLabel = new JLabel(new ImageIcon(loadedImage));
                        imageLabel.setBounds(50, 50, 500, 500);
                        add(imageLabel);
                        index++;
                        repaint();
                    } catch (IOException e) {
                        index++;
                    }
                }
                else{
                    setVisible(false);
                    repoFrame.removeImageDisplayPanel();
                    repaint();
                }
            }
        });

        setVisible(true);
        repaint();
        repoFrame.repaint();
    }
}
