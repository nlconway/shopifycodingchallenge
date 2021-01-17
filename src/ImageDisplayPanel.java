import com.sun.javafx.beans.IDProperty;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageDisplayPanel extends JPanel {
    int index = 0;
    JLabel label;
    String filePath;

    public ImageDisplayPanel(String filePath){
      this.filePath = filePath;
    }

    public void populate(java.util.List<Image> images){
        setLayout(null);
        JButton nextImageButton = new JButton("Next Image");
        nextImageButton.setBounds(20, 20, 165, 25);

        if(index < images.size()) {
            try {
                BufferedImage loadedImage = ImageIO.read(new File(filePath + images.get(index).getName()));
                label = new JLabel(new ImageIcon(loadedImage));
                label.setBounds(50, 50, 500, 500);
                add(label);
                index++;
            } catch (IOException e) {
                index++;
            }
        }
        add(nextImageButton);
        nextImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(index < images.size()){
                    try {
                        remove(label);
                        BufferedImage loadedImage = ImageIO.read(new File(filePath + images.get(index).getName()));
                        label = new JLabel(new ImageIcon(loadedImage));
                        label.setBounds(50, 50, 500, 500);
                        add(label);
                        index++;
                        repaint();
                    } catch (IOException e) {
                        index++;
                    }
                }
                else{
                    setVisible(false);
                    repaint();
                }
            }
        });

        setVisible(true);
    }
}
