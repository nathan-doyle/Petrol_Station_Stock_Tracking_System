package com.petrolstation.gui;

import com.petrolstation.dao.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.petrolstation.MyJDBC.pw;
import static com.petrolstation.dao.Discrepancy_DAO.Get_All_Discrepancies;

public class Staff_Dashboard extends JPanel
{
    private JTable dataTable;
    private JScrollPane scrollPane;

    public Staff_Dashboard(JPanel mainContainer, CardLayout cl)
    {
        setLayout(new BorderLayout());

        // Sidebar for Navigation
        JPanel sidebar = new JPanel(new GridLayout(8, 1, 5, 5));
        sidebar.setBackground(Color.DARK_GRAY);

        JButton btn_Inventory = new JButton("View Inventory");
        JButton btn_Suppliers = new JButton("Show Suppliers");
        JButton btn_Orders = new JButton("Manage Orders");
        JButton btn_Arrived = new JButton("Mark order as Arrived");
        JButton btn_View_Discrepancies = new JButton("View Discrepancies");
        JButton btn_Add_Discrepancies = new JButton("Add Discrepancies");
        JButton btn_Logout = new JButton("Logout");

        sidebar.add(btn_Inventory);
        sidebar.add(btn_Suppliers);
        sidebar.add(btn_Orders);
        sidebar.add(btn_Arrived);
        sidebar.add(btn_View_Discrepancies);
        sidebar.add(btn_Add_Discrepancies);
        sidebar.add(btn_Logout);
        add(sidebar, BorderLayout.WEST);

        // Main Display Area (The Table)
        dataTable = new JTable();
        scrollPane = new JScrollPane(dataTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button Logic
        btn_Inventory.addActionListener(e -> Refresh_Inventory());

        btn_Suppliers.addActionListener(e -> Refresh_Suppliers());

        btn_Orders.addActionListener(e -> Refresh_Orders());

        btn_Arrived.addActionListener(e -> Handle_Mark_Arrived());

        btn_Logout.addActionListener(e -> cl.show(mainContainer, "LOGIN"));// Flip back to login page

        btn_View_Discrepancies.addActionListener(e -> Refresh_Discrepancies());

        btn_Add_Discrepancies.addActionListener(e -> Handle_Log_Discrepancy());

    }

    private void Refresh_Inventory()
    {
        try
        {
            // Call DAO to get the data as a ResultSet
            ResultSet rs = Product_DAO.Get_All_Products();

            // Create the translator (Model)
            Result_Set_Table_Model model = new Result_Set_Table_Model(rs);

            // Hand the translator to JTable
            dataTable.setModel(model);

            // Set a custom renderer for the whole table
            dataTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
            {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
                {

                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    String First_Column_Name = table.getColumnName(0);

                    // Get Stock and Reorder values from the table model IF it is the stock table
                    if("Product_ID".equals(First_Column_Name))
                    {
                        try
                        {
                            int stock = Integer.parseInt(table.getValueAt(row, 3).toString());
                            int reorder = Integer.parseInt(table.getValueAt(row, 4).toString());

                            // If stock is low, paint it red
                            if (stock <= reorder)
                            {
                                c.setForeground(Color.RED);
                                c.setFont(c.getFont().deriveFont(Font.BOLD));
                            }
                            else
                            {
                                c.setForeground(Color.BLACK);
                                c.setFont(c.getFont().deriveFont(Font.PLAIN));
                            }
                        }
                        catch (Exception e)
                        {
                            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE); // Handle cases where values might be null during refresh
                            e.printStackTrace(pw);
                            pw.flush();
                        }
                    }
                    else
                    {
                        c.setForeground(Color.BLACK);
                        c.setFont(c.getFont().deriveFont(Font.PLAIN));
                    }

                    // Keep the selection color working so the user knows what row is clicked
                    if (isSelected)
                    {
                        c.setBackground(table.getSelectionBackground());
                    }
                    else
                    {
                        c.setBackground(table.getBackground());
                    }

                    return c;
                }
            });

        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this, "Error loading inventory: " + e.getMessage());
            e.printStackTrace(pw);
            pw.flush();
        }
    }

    private void Refresh_Orders()
    {
        try
        {
            // Call DAO to get the data as a ResultSet
            ResultSet rs = Purchase_Order_DAO.Get_All_Orders();

            // Create the translator (Model)
            Result_Set_Table_Model model = new Result_Set_Table_Model(rs);

            // Hand the translator to JTable
            dataTable.setModel(model);
        }
        catch (SQLException e)
        {
            e.printStackTrace(pw);
            pw.flush();
            JOptionPane.showMessageDialog(this, "Error loading orders: " + e.getMessage());
        }
    }

    private void Refresh_Discrepancies()
    {
        try
        {
            // Call DAO to get the data as a ResultSet
            ResultSet rs = Get_All_Discrepancies();

            // Create the translator (Model)
            Result_Set_Table_Model model = new Result_Set_Table_Model(rs);

            // Hand the translator to JTable
            dataTable.setModel(model);
        }
        catch (SQLException e)
        {
            e.printStackTrace(pw);
            pw.flush();
            JOptionPane.showMessageDialog(this, "Error loading discrepancies: " + e.getMessage());
        }
    }

    private void Handle_Mark_Arrived()
    {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow == -1)
        {
            JOptionPane.showMessageDialog(this, "Please select an order from the table.");
            return;
        }

        int poId = Integer.parseInt(dataTable.getValueAt(selectedRow, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this,
                "Mark Order #" + poId + " as arrived?\nThis will automatically update your inventory stock levels.",
                "Confirm Delivery", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION)
        {
            try
            {
                com.petrolstation.dao.Purchase_Order_DAO.Process_Delivery(poId);

                Refresh_Orders();
                JOptionPane.showMessageDialog(this, "Inventory updated and order marked as arrived!");

            }
            catch (SQLException ex)
            {
                ex.printStackTrace(pw);
                JOptionPane.showMessageDialog(this, "Error processing delivery: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void Handle_Log_Discrepancy()
    {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow == -1)
        {
            JOptionPane.showMessageDialog(this, "Select an Order from the table first.");
            return;
        }

        int poId = Integer.parseInt(dataTable.getValueAt(selectedRow, 0).toString());
        String user = com.petrolstation.gui.Login_Panel.Get_User();
        int staffId = com.petrolstation.dao.User_DAO.Return_UserID(user);

        try (ResultSet rs = com.petrolstation.dao.Purchase_Order_DAO.Get_Order_Items(poId))
        {
            while (rs.next())
            {
                int prodId = rs.getInt("Product_ID");
                int expected = rs.getInt("Quantity_Ordered");
                String prodName = rs.getString("Product_Name");

                String input = JOptionPane.showInputDialog(this,
                        "Order #" + poId + " | Item: " + prodName + "\n" +
                                "Expected: " + expected + "\n" +
                                "Enter Actual Quantity Received:");

                if (input != null)
                {
                    int actual = Integer.parseInt(input);

                    if (actual != expected)
                    {
                        String reason = JOptionPane.showInputDialog(this, "Reason for discrepancy (e.g., Damaged, Missing):");
                        com.petrolstation.dao.Discrepancy_DAO.Process_Discrepancy_Delivery(poId, prodId, expected, actual, reason, staffId);
                    }
                }
            }

            com.petrolstation.dao.Purchase_Order_DAO.Update_Order_Status(poId, "Arrived");
            Refresh_Inventory();
            Refresh_Orders();

        }
        catch (Exception ex)
        {
            ex.printStackTrace(pw);
            JOptionPane.showMessageDialog(this, "Error processing discrepancy: " + ex.getMessage());
        }
    }

    private void Refresh_Suppliers()
    {
        try
        {
            // Call DAO to get the data as a ResultSet
            ResultSet rs = Supplier_DAO.Get_All_Suppliers();

            // Create the translator (Model)
            Result_Set_Table_Model model = new Result_Set_Table_Model(rs);

            // Hand the translator to JTable
            dataTable.setModel(model);

            // Set a custom renderer for the whole table
            dataTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
            {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
                {

                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    String First_Column_Name = table.getColumnName(0);

                    // Get values from the table model IF it is the suppliers table
                    if ("Supplier_ID".equals(First_Column_Name))
                    {
                        try
                        {
                            c.setForeground(Color.BLACK);
                            c.setFont(c.getFont().deriveFont(Font.PLAIN));
                        }
                        catch (Exception e)
                        {
                            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE); // Handle cases where values might be null during refresh
                            e.printStackTrace(pw);
                            pw.flush();
                        }
                    }
                    else
                    {
                        c.setForeground(Color.BLACK);
                        c.setFont(c.getFont().deriveFont(Font.PLAIN));
                    }

                    // Keep the selection color working so the user knows what row is clicked
                    if (isSelected)
                    {
                        c.setBackground(table.getSelectionBackground());
                    }
                    else
                    {
                        c.setBackground(table.getBackground());
                    }

                    return c;
                }
            });
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this, "Error loading inventory: " + e.getMessage());
            e.printStackTrace(pw);
            pw.flush();
        }
    }
}