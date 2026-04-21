package com.petrolstation.gui;

import com.petrolstation.dao.User_DAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Login_Frame extends JFrame
    {
        private CardLayout cl = new CardLayout();
        private JPanel mainContainer = new JPanel(cl);

        public Login_Frame()
            {
                // Set up the Frame
                setTitle("Petrol Station Management - Login");
                setSize(1600, 400);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setLocationRelativeTo(null); // Centers window
                setLayout(new BorderLayout(10, 10));

                Login_Panel loginPage = new Login_Panel(mainContainer, cl);

                Add_User_Panel addUserPage = new Add_User_Panel(this, mainContainer, cl, loginPage);
                Manager_Dashboard dashboardPage = new Manager_Dashboard(mainContainer, cl);
                Staff_Dashboard staffDashboardPage = new Staff_Dashboard(mainContainer, cl);

                mainContainer.add(loginPage, "LOGIN");
                mainContainer.add(addUserPage, "ADD_USER");
                mainContainer.add(dashboardPage, "DASHBOARD");
                mainContainer.add(staffDashboardPage, "STAFF_DASHBOARD");

                add(mainContainer);

                cl.show(mainContainer, "LOGIN");
            }
    }
