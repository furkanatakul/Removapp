package Main;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import DataBase.*;
import Helpers.*;

public class LoginInterface extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField usernameFieldLogin;
    private JPasswordField passwordFieldLogin;
    private JTextField nameSurnameFieldRegister;
    private JTextField usernameFieldRegister;
    private JPasswordField passwordFieldRegister;
    private JRadioButton rdbtnMale;
    private JRadioButton rdbtnFemale;
    private DBConnect connect = new DBConnect();
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginInterface frame = new LoginInterface();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LoginInterface() {
    	Insets insets = UIManager.getInsets("TabbedPane.contentBorderInsets");
    	//insets.top = -1;
    	insets.left=-1;
    	insets.right=-1;
    	insets.bottom=-1;
    	UIManager.put("TabbedPane.contentBorderInsets", insets);
        setTitle("Removapp");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1156, 689);
        contentPane = new JPanel();
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel welcomeLabel = new JLabel("REMOVAPP");
        welcomeLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setForeground(new Color(213, 224, 223));
        welcomeLabel.setBounds(191, 10, 757, 78);
        welcomeLabel.setFont(new Font("Stencil", Font.BOLD, 60));
        contentPane.add(welcomeLabel);
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(201, 87, 717, 275);
        tabbedPane.setBackground(Color.BLACK);
        contentPane.add(tabbedPane);

        JPanel loginPanel = new JPanel();
        loginPanel.setBorder(null);
        loginPanel.setLocation(213, 24);
        loginPanel.setBackground(new Color(0, 0, 0));
        tabbedPane.addTab("LogIn", null, loginPanel, null);
        tabbedPane.setForegroundAt(0, new Color(0, 0, 0));
        tabbedPane.setBackgroundAt(0, new Color(255, 255, 255));
        loginPanel.setLayout(null);

        JLabel lblUsernameLogin = new JLabel("Username:");
        lblUsernameLogin.setForeground(new Color(192, 192, 192));
        lblUsernameLogin.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 23));
        lblUsernameLogin.setBounds(167, 43, 149, 40);
        loginPanel.add(lblUsernameLogin);

        JLabel lblPasswordLogin = new JLabel("Password:");
        lblPasswordLogin.setForeground(new Color(192, 192, 192));
        lblPasswordLogin.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 23));
        lblPasswordLogin.setBounds(167, 105, 149, 40);
        loginPanel.add(lblPasswordLogin);

        usernameFieldLogin = new JTextField();
        usernameFieldLogin.setFont(new Font("Yu Gothic UI", Font.PLAIN, 23));
        usernameFieldLogin.setBounds(326, 43, 192, 40);
        loginPanel.add(usernameFieldLogin);
        usernameFieldLogin.setColumns(10);

        passwordFieldLogin = new JPasswordField();
        passwordFieldLogin.setFont(new Font("Yu Gothic UI", Font.PLAIN, 23));
        passwordFieldLogin.setBounds(326, 105, 192, 40);
        loginPanel.add(passwordFieldLogin);

        JButton btnLogin = new JButton("LogIn");
        btnLogin.setForeground(new Color(192, 192, 192));
        btnLogin.setBackground(new Color(75, 19, 255));
        btnLogin.addActionListener(e -> login());
        btnLogin.setFont(new Font("Yu Gothic UI", Font.BOLD, 23));
        btnLogin.setBounds(298, 185, 122, 40);
        loginPanel.add(btnLogin);

        JPanel registerPanel = new JPanel();
        registerPanel.setBackground(Color.BLACK);
        tabbedPane.addTab("Register", null, registerPanel, null);
        tabbedPane.setBackgroundAt(1, new Color(255, 255, 255));
        registerPanel.setLayout(null);

        JLabel lblNameSurnameRegister = new JLabel("Name and Surname:");
        lblNameSurnameRegister.setForeground(new Color(192, 192, 192));
        lblNameSurnameRegister.setBackground(new Color(0, 0, 0));
        lblNameSurnameRegister.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 23));
        lblNameSurnameRegister.setBounds(10, 21, 219, 40);
        registerPanel.add(lblNameSurnameRegister);

        nameSurnameFieldRegister = new JTextField();
        nameSurnameFieldRegister.setFont(new Font("Yu Gothic UI", Font.PLAIN, 23));
        nameSurnameFieldRegister.setColumns(10);
        nameSurnameFieldRegister.setBounds(236, 22, 192, 40);
        registerPanel.add(nameSurnameFieldRegister);

        JLabel lblUsernameRegister = new JLabel("Username:");
        lblUsernameRegister.setForeground(new Color(192, 192, 192));
        lblUsernameRegister.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 23));
        lblUsernameRegister.setBounds(10, 71, 175, 40);
        registerPanel.add(lblUsernameRegister);

        usernameFieldRegister = new JTextField();
        usernameFieldRegister.setFont(new Font("Yu Gothic UI", Font.PLAIN, 23));
        usernameFieldRegister.setColumns(10);
        usernameFieldRegister.setBounds(236, 72, 192, 40);
        registerPanel.add(usernameFieldRegister);

        JLabel lblPasswordRegister = new JLabel("Password:");
        lblPasswordRegister.setForeground(new Color(192, 192, 192));
        lblPasswordRegister.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 23));
        lblPasswordRegister.setBounds(10, 121, 149, 40);
        registerPanel.add(lblPasswordRegister);

        passwordFieldRegister = new JPasswordField();
        passwordFieldRegister.setFont(new Font("Yu Gothic UI", Font.PLAIN, 23));
        passwordFieldRegister.setBounds(236, 122, 192, 40);
        registerPanel.add(passwordFieldRegister);

        JButton btnRegister = new JButton("Register");
        btnRegister.setForeground(new Color(192, 192, 192));
        btnRegister.setBackground(new Color(75, 19, 255));
        btnRegister.addActionListener(e -> register());
        btnRegister.setFont(new Font("Yu Gothic UI", Font.BOLD, 23));
        btnRegister.setBounds(298, 185, 122, 40);
        registerPanel.add(btnRegister);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setForeground(new Color(192, 192, 192));
        lblGender.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 23));
        lblGender.setBackground(new Color(0, 0, 0));
        lblGender.setBounds(501, 21, 126, 40);
        registerPanel.add(lblGender);

        rdbtnMale = new JRadioButton("Male");
        rdbtnMale.setForeground(new Color(255, 255, 255));
        rdbtnMale.setBackground(new Color(0, 0, 0));
        rdbtnMale.setFont(new Font("Yu Gothic UI", Font.PLAIN, 23));
        rdbtnMale.setActionCommand("Male");
        rdbtnMale.setBounds(501, 72, 103, 21);
        rdbtnMale.setSelected(true);
        registerPanel.add(rdbtnMale);

        rdbtnFemale = new JRadioButton("Female");
        rdbtnFemale.setForeground(new Color(255, 255, 255));
        rdbtnFemale.setBackground(new Color(0, 0, 0));
        rdbtnFemale.setFont(new Font("Yu Gothic UI", Font.PLAIN, 23));
        rdbtnFemale.setActionCommand("Female");
        rdbtnFemale.setBounds(501, 106, 103, 21);
        registerPanel.add(rdbtnFemale);
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(rdbtnMale);
        bg.add(rdbtnFemale);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon(getClass().getResource("/arkaplan.jpg")));
        lblNewLabel.setBounds(0, 372, 1142, 280);
        contentPane.add(lblNewLabel);
    }

    private void login() {
        String username = usernameFieldLogin.getText();
        String password = new String(passwordFieldLogin.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Fill In All The Fields !!!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection con = connect.connectdb();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM kullanicilar WHERE username = '" + username + "' AND password = '" + password + "'")) {

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setPassword(rs.getString("password"));
                u.setUsername(rs.getString("username"));
                u.setName_surname(rs.getString("name_surname"));
                u.setGender(rs.getString("gender"));
                connect.disconnect();
                ListInterface obj = new ListInterface(u);
                obj.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username or Password is wrong !!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void register() {
        String nameSurname = nameSurnameFieldRegister.getText();
        String username = usernameFieldRegister.getText();
        String password = new String(passwordFieldRegister.getPassword());
        String gender = rdbtnMale.isSelected() ? "Erkek" : "Kadin";

        if (nameSurname.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Fill In All The Fields !!!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection con = connect.connectdb();
             Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM kullanicilar WHERE username = '" + username + "'");
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username already exists. Please choose another username.", "Error", JOptionPane.ERROR_MESSAGE);
                return; 
            }
            int result = st.executeUpdate("INSERT INTO kullanicilar (name_surname, username, password, gender) " +
                    "VALUES ('" + nameSurname + "', '" + username + "', '" + password + "', '" + gender + "')");

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful. You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}