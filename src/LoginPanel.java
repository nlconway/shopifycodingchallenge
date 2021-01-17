import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    JFrame repoFrame;

    public LoginPanel(){

    }
    public void populate(RepositoryFrame repoFrame, ImageSelectionPanel imagePanel, Connector connector){
        this.repoFrame = repoFrame;
        setLayout(null);
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        JLabel userName = new JLabel();
        userName.setText("Username");
        userName.setBounds(10, 20, 80, 25);
        JTextField userNameText = new JTextField(25);
        userNameText.setBounds(100, 20, 165, 25);
        JLabel passWord = new JLabel();
        passWord.setText("Password");
        passWord.setBounds(10, 50, 80, 25);
        JTextField passWordText = new JTextField(25);
        passWordText.setBounds(100, 50, 165, 25);

        add(userName);
        add(userNameText);
        add(passWord);
        add(passWordText);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
//               connector.login(userNameText.getText(), passWordText.getText());
                Session.login(userNameText.getText(), passWordText.getText() , connector);
                if (Session.getStatus().equals(Session.Status.LOGIN)) {
                    remove();
                    imagePanel.setVisible(true);
                }
            }
        });

        setVisible(true);
    }

    private void remove(){
        repoFrame.getContentPane().remove(this);
    }
}
