package com.petrolstation.gui;

import com.petrolstation.dao.Product_DAO;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Manager_Dashboard extends JPanel
    {
        private JTable dataTable;
        private JScrollPane scrollPane;

        public Manager_Dashboard(JPanel mainContainer, CardLayout cl)
            {
                setLayout(new BorderLayout());

                // Sidebar for Navigation
                JPanel sidebar = new JPanel(new GridLayout(5, 1, 5, 5));
                sidebar.setBackground(Color.DARK_GRAY);

                JButton btnInventory = new JButton("View Inventory");
                JButton btnOrders = new JButton("Manage Orders");
                JButton btnLogout = new JButton("Logout");

                sidebar.add(btnInventory);
                sidebar.add(btnOrders);
                sidebar.add(btnLogout);
                add(sidebar, BorderLayout.WEST);

                // Main Display Area (The Table)
                dataTable = new JTable();
                scrollPane = new JScrollPane(dataTable);
                add(scrollPane, BorderLayout.CENTER);

                // Button Logic
                btnInventory.addActionListener(e -> refreshInventory());

                btnLogout.addActionListener(e ->
                    {
                        cl.show(mainContainer, "LOGIN"); // Flip back to login page
                    });
            }

        private void refreshInventory()
        {
            try
                {
                    // Call DAO to get the data as a ResultSet
                    ResultSet rs = Product_DAO.Get_All_Products();

                    // Create the translator (Model)
                    Result_Set_Table_Model model = new Result_Set_Table_Model(rs);

                    // Hand the translator to JTable
                    dataTable.setModel(model);

                }
            catch (SQLException e)
                {
                    JOptionPane.showMessageDialog(this, "Error loading inventory: " + e.getMessage());
                }
        }
    }