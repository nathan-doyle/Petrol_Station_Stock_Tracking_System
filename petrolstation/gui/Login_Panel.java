package com.petrolstation.gui;

import com.petrolstation.dao.User_DAO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Login_Panel extends JPanel
    {
        private JComboBox<String> userDropdown;
        private JPasswordField txtPassword;
        private JButton btnAddUser, btnLogin;
        private static String User_Logged_In = null;
        private static int User_Role = 0;

        // Constructor
        public Login_Panel(JPanel mainContainer, CardLayout cl)
            {
                this.setLayout(new BorderLayout(10, 10)); // Set the panel's layout

                // Create the Input Panel
                JPanel centerPanel = new JPanel(new GridLayout(2, 2, 5, 5));
                centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                centerPanel.add(new JLabel("Select User:"));
                userDropdown = new JComboBox<>();
                populateUserDropdown(); // Method to fill names from DB
                centerPanel.add(userDropdown);

                centerPanel.add(new JLabel("Password:"));
                txtPassword = new JPasswordField();
                centerPanel.add(txtPassword);

                add(centerPanel, BorderLayout.CENTER);

                // Create the Button Panel
                JPanel southPanel = new JPanel(new BorderLayout());
                btnAddUser = new JButton("Add User");
                btnLogin = new JButton("Login");

                southPanel.add(btnAddUser, BorderLayout.WEST); // Bottom Left
                southPanel.add(btnLogin, BorderLayout.EAST);   // Bottom Right
                add(southPanel, BorderLayout.SOUTH);

                btnAddUser.addActionListener(e -> cl.show(mainContainer, "ADD_USER"));

                btnLogin.addActionListener(e ->
                    {
                        String user = (String) userDropdown.getSelectedItem();
                        String pass = new String(txtPassword.getPassword());

                        int role = User_DAO.Validate_Login(user, pass);

                        if (role == 1)
                            { // Manager
                                JOptionPane.showMessageDialog(this, "Welcome Manager!");
                                User_Logged_In = user;
                                User_Role = role;
                                cl.show(mainContainer, "DASHBOARD");
                            }
                        else if (role == 2)
                            { // Staff
                                User_Logged_In = user;
                                User_Role = role;
                                JOptionPane.showMessageDialog(this, "Welcome Staff!");
                                cl.show(mainContainer, "STAFF_DASHBOARD");
                            } else
                            {
                                JOptionPane.showMessageDialog(this, "Incorrect Credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
                            }
                    });
            }

        public void populateUserDropdown()
            {
                userDropdown.removeAllItems();
                // Fetch usernames from User_DAO
                ArrayList<String> names = User_DAO.Get_All_Users();
                for (String name : names)
                    {
                        userDropdown.addItem(name);
                    }
            }


        public static String Get_User()
            {
                return User_Logged_In;
            }

        public static int Get_Role()
            {
                return User_Role;
            }
    }