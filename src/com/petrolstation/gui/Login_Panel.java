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

        // Added constructor that accepts the binder parts
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
            }

        private void populateUserDropdown()
            {
                // Fetch usernames from User_DAO
                ArrayList<String> names = User_DAO.Get_All_Users();
                for (String name : names)
                    {
                        userDropdown.addItem(name);
                    }
            }
    }
