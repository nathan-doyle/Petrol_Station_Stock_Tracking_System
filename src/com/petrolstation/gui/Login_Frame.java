package com.petrolstation.gui;

import com.petrolstation.dao.User_DAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Login_Frame extends JFrame
    {
        private JComboBox<String> userDropdown;
        private JPasswordField txtPassword;
        private JButton btnLogin, btnAddUser;
        private CardLayout cl = new CardLayout();
        private JPanel mainContainer = new JPanel(cl);

        public Login_Frame()
            {
                // Set up the Frame
                setTitle("Petrol Station Management - Login");
                setSize(400, 250);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setLocationRelativeTo(null); // Centers window
                setLayout(new BorderLayout(10, 10));

                Login_Panel loginPage = new Login_Panel(mainContainer, cl);

                Add_User_Panel addUserPage = new Add_User_Panel(this, mainContainer, cl);
                Manager_Dashboard dashboardPage = new Manager_Dashboard(mainContainer, cl);

                mainContainer.add(loginPage, "LOGIN");
                mainContainer.add(addUserPage, "ADD_USER");
                mainContainer.add(dashboardPage, "DASHBOARD");

                add(mainContainer);

                cl.show(mainContainer, "LOGIN");


                // Add Event Handling
                btnLogin.addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                            {
                                handleLogin();
                            }
                    });
            }

        private void handleLogin()
            {
                String user = (String) userDropdown.getSelectedItem();
                String pass = new String(txtPassword.getPassword());

                // Use  Validate_Login logic
                int role = User_DAO.Validate_Login(user, pass);

                if (role == 1)
                    {
                        JOptionPane.showMessageDialog(this, "Welcome Manager!");
                        // new ManagerDashboard().setVisible(true);
                        this.dispose(); // Close login window
                    }
                else if(role == 2)
                    {
                        // Logic for regular staff
                        JOptionPane.showMessageDialog(this, "Welcome Staff!");
                        this.dispose();
                    }
                else
                    {
                        JOptionPane.showMessageDialog(this, "Password Incorrect!, Please try again");
                    }
            }
    }
