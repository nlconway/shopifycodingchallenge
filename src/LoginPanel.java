import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Login Panel, User enters username and password
class LoginPanel extends JPanel {
    private RepositoryFrame repoFrame;
    private Connector connector;

    LoginPanel(RepositoryFrame repoFrame, Connector connector) {
        this.repoFrame = repoFrame;
        this.connector = connector;
    }

    //Add components of login panel
    void populate() {
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

        //On selecting login, Session is called to change state
        //If login successful Session state is changed and the frame repaints
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Session.login(userNameText.getText(), passWordText.getText(), connector);
                if (Session.getStatus().equals(Session.Status.LOGIN)) {
                    repoFrame.repaint();
                }
            }
        });

        setVisible(true);
    }

}
