package com.petrolstation.gui;

import com.petrolstation.dao.User_DAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Add_User_Panel extends JPanel
    {
        private JTextField txtNewUser;
        private JPasswordField txtNewPass;
        private JCheckBox chkIsManager;
        private JButton btnCreate, btnBack;

        public Add_User_Panel(JFrame parentFrame, JPanel mainCardPanel, CardLayout cl)
            {
                // Set Layout
                setLayout(new BorderLayout(10, 10));
                setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                // Form Panel
                JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
                formPanel.add(new JLabel("New Username:"));
                txtNewUser = new JTextField();
                formPanel.add(txtNewUser);

                formPanel.add(new JLabel("New Password:"));
                txtNewPass = new JPasswordField();
                formPanel.add(txtNewPass);

                formPanel.add(new JLabel("Manager Permissions:"));
                chkIsManager = new JCheckBox("Yes (Admin Access)");
                formPanel.add(chkIsManager);

                add(formPanel, BorderLayout.CENTER);

                // Button Panel
                JPanel southPanel = new JPanel(new BorderLayout());
                btnBack = new JButton("Back to Login");
                btnCreate = new JButton("Create User");

                southPanel.add(btnBack, BorderLayout.WEST);
                southPanel.add(btnCreate, BorderLayout.EAST);
                add(southPanel, BorderLayout.SOUTH);

                // Action Listeners
                btnCreate.addActionListener(e -> handleCreateUser());

                btnBack.addActionListener(e ->
                    {
                        // Swap back to the Login card
                        cl.show(mainCardPanel, "LOGIN");
                    });
            }

        private void handleCreateUser()
            {
                String username = txtNewUser.getText();
                String password = new String(txtNewPass.getPassword());

                if (username.isEmpty() || password.isEmpty())
                    {
                        JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                User_DAO.Add_User(username, password, chkIsManager.isSelected());

                JOptionPane.showMessageDialog(this, "User " + username + " created successfully!");

                // Clear fields for next use
                txtNewUser.setText("");
                txtNewPass.setText("");
                chkIsManager.setSelected(false);
            }
    }