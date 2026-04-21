package com.petrolstation.gui;

import com.petrolstation.dao.User_DAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Add_User_Panel extends JPanel
    {
        private CardLayout cl;
        private JPanel mainCardPanel;
        private Login_Panel Login_Panel_Ref;
        private JTextField txtNewUser;
        private JPasswordField txtNewPass;
        private JCheckBox chkIsManager;
        private JButton btnCreate, btnBack;

        public Add_User_Panel(JFrame parentFrame, JPanel mainCardPanel, CardLayout cl, Login_Panel Login_Ref)
            {
                this.cl = cl;
                this.mainCardPanel = mainCardPanel;
                this.Login_Panel_Ref = Login_Ref;
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
                btnCreate.addActionListener(e -> Handle_Create_User());

                btnBack.addActionListener(e ->
                    {
                        // Swap back to the Login card
                        cl.show(mainCardPanel, "LOGIN");
                    });
            }

        private void Handle_Create_User()
            {
                String username = txtNewUser.getText();
                String password = new String(txtNewPass.getPassword());

                if (username.isEmpty() || password.isEmpty())
                    {
                        JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                User_DAO.Add_User(username, password, chkIsManager.isSelected());

                Login_Panel_Ref.populateUserDropdown();

                cl.show(mainCardPanel, "LOGIN");

                JOptionPane.showMessageDialog(this, "User " + username + " created successfully!");

                // Clear fields for next use
                txtNewUser.setText("");
                txtNewPass.setText("");
                chkIsManager.setSelected(false);
            }
    }