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

public class Manager_Dashboard extends JPanel
    {
        private JTable dataTable;
        private JScrollPane scrollPane;

        public Manager_Dashboard(JPanel mainContainer, CardLayout cl)
            {
                setLayout(new BorderLayout());

                // Sidebar for Navigation
                JPanel sidebar = new JPanel(new GridLayout(8, 1, 5, 5));
                sidebar.setBackground(Color.DARK_GRAY);

                JButton btn_Inventory = new JButton("View Inventory");
                JButton btn_Add_Product = new JButton("Add New Product");
                JButton btn_Remove_Product = new JButton("Remove Product");
                JButton btn_Remove_Supplier = new JButton("Remove Supplier");
                JButton btn_Assign_Supplier = new JButton("Assign Supplier");
                JButton btn_Suppliers = new JButton("Show Suppliers");
                JButton btn_Add_Supplier = new JButton("Add New Supplier");
                JButton btn_Orders = new JButton("Manage Orders");
                JButton btn_Add_Order = new JButton("Order Product");
                JButton btn_Update_Stock = new JButton("Order all Low Stock");
                JButton btn_Arrived = new JButton("Mark order as Arrived");
                JButton btn_View_Discrepancies = new JButton("View Discrepancies");
                JButton btn_Add_Discrepancies = new JButton("Add Discrepancies");
                JButton btn_Logout = new JButton("Logout");

                sidebar.add(btn_Inventory);
                sidebar.add(btn_Add_Product);
                sidebar.add(btn_Remove_Product);
                sidebar.add(btn_Suppliers);
                sidebar.add(btn_Add_Supplier);
                sidebar.add(btn_Assign_Supplier);
                sidebar.add(btn_Remove_Supplier);
                sidebar.add(btn_Orders);
                sidebar.add(btn_Update_Stock);
                sidebar.add(btn_Add_Order);
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

                btn_Update_Stock.addActionListener(e -> Handle_Order_All_Low_Stock());

                btn_Add_Product.addActionListener(e-> Handle_Add_Product());

                btn_Suppliers.addActionListener(e -> Refresh_Suppliers());

                btn_Add_Supplier.addActionListener(e -> Handle_Add_Supplier());

                btn_Assign_Supplier.addActionListener(e -> Handle_Assign_Supplier());

                btn_Remove_Supplier.addActionListener(e -> Handle_Remove_Supplier());

                btn_Remove_Product.addActionListener(e -> Handle_Remove_Product());

                btn_Orders.addActionListener(e -> Refresh_Orders());

                btn_Add_Order.addActionListener(e -> Handle_Order_Product());

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

        private void Handle_Add_Product()
            {
                // Create a panel with a grid layout for the inputs
                JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

                JTextField nameField = new JTextField();
                JTextField barcodeField = new JTextField();
                JTextField stockField = new JTextField();
                JTextField reorderField = new JTextField();
                JTextField maxField = new JTextField();

                panel.add(new JLabel("Product Name:"));
                panel.add(nameField);
                panel.add(new JLabel("Barcode:"));
                panel.add(barcodeField);
                panel.add(new JLabel("Current Stock:"));
                panel.add(stockField);
                panel.add(new JLabel("Reorder Level:"));
                panel.add(reorderField);
                panel.add(new JLabel("Max Level:"));
                panel.add(maxField);

                // Show a panel to confirm
                int result = JOptionPane.showConfirmDialog(this, panel, "Enter New Product Details",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // If they clicked OK, send it to the database
                if (result == JOptionPane.OK_OPTION)
                    {
                        try
                            {
                                String name = nameField.getText();
                                String barcode = barcodeField.getText();
                                int stock = Integer.parseInt(stockField.getText());
                                int reorder = Integer.parseInt(reorderField.getText());
                                int max = Integer.parseInt(maxField.getText());

                                if (name.isEmpty()) throw new Exception("Name cannot be empty");

                                // Call DAO method
                                Product_DAO.Add_New_Product(name, barcode, stock, reorder, max);

                                // Success feedback and refresh
                                Refresh_Inventory();
                                JOptionPane.showMessageDialog(this, name + " added to inventory!");

                            }
                        catch (Exception ex)
                            {
                                ex.printStackTrace(pw);
                                pw.flush();
                                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                            }
                    }
            }

        private void Handle_Remove_Product()
        {
            try
                {
                    int selectedRow = dataTable.getSelectedRow();

                    if (selectedRow == -1)
                        {
                            JOptionPane.showMessageDialog(this, "Please select a product to remove.");
                            return;
                        }

                    // Get ID and Name for the confirmation message
                    int productId = Integer.parseInt(dataTable.getValueAt(selectedRow, 0).toString());
                    String productName = dataTable.getValueAt(selectedRow, 1).toString();

                    // Confirm Deletion
                    int response = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to delete " + productName + " (ID: " + productId + ")?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (response == JOptionPane.YES_OPTION)
                        {
                            // Call the DAO
                            Product_DAO.Remove_Product(productId);

                            // Refresh the UI
                            Refresh_Inventory();
                            JOptionPane.showMessageDialog(this, productName + " has been removed.");
                        }
                }
            catch (SQLException e)
                {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace(pw);
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

        private void Handle_Add_Supplier()
            {
                // Create a panel with a grid layout for the inputs
                JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

                JTextField nameField = new JTextField();
                JTextField emailField = new JTextField();
                JTextField phoneField = new JTextField();

                panel.add(new JLabel("Supplier Name:"));
                panel.add(nameField);
                panel.add(new JLabel("Email Address:"));
                panel.add(emailField);
                panel.add(new JLabel("Phone Number:"));
                panel.add(phoneField);

                // Show a panel to confirm
                int result = JOptionPane.showConfirmDialog(this, panel, "Enter New Supplier Details",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // If they clicked OK, send it to the database
                if (result == JOptionPane.OK_OPTION)
                {
                    try
                    {
                        String name = nameField.getText();
                        String email = emailField.getText();
                        String phone = phoneField.getText();
                        if (name.isEmpty()) throw new Exception("Name cannot be empty");

                        // Call DAO method
                        Supplier_DAO.Add_New_Supplier(name, email, phone);

                        // Success feedback and refresh
                        Refresh_Suppliers();
                        JOptionPane.showMessageDialog(this, name + " added to suppliers!");

                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace(pw);
                        pw.flush();
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        private void Handle_Remove_Supplier()
            {

                int selectedRow = dataTable.getSelectedRow();

                if (selectedRow == -1)
                {
                    JOptionPane.showMessageDialog(this, "Please select a supplier to remove.");
                    return;
                }

                // Get ID and Name for the confirmation message
                int supplierId = Integer.parseInt(dataTable.getValueAt(selectedRow, 0).toString());
                String supplierName = dataTable.getValueAt(selectedRow, 1).toString();

                // Confirm Deletion
                int response = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete " + supplierName + " (ID: " + supplierId + ")?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (response == JOptionPane.YES_OPTION)
                {
                    // Call the DAO
                    Supplier_DAO.Remove_Supplier(supplierId);

                    // Refresh the UI
                    Refresh_Suppliers();
                    JOptionPane.showMessageDialog(this, supplierName + " has been removed.");
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

        private void Handle_Assign_Supplier()
            {
                int selectedRow = dataTable.getSelectedRow();

                if (selectedRow == -1)
                    {
                        JOptionPane.showMessageDialog(this, "Please select a product first.");
                        return;
                    }

                int productId = Integer.parseInt(dataTable.getValueAt(selectedRow, 0).toString());
                String productName = dataTable.getValueAt(selectedRow, 1).toString();

                JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

                JComboBox<String> supplierDropdown = new JComboBox<>();
                java.util.ArrayList<String> suppliers = com.petrolstation.dao.Product_DAO.Get_All_Supplier_Names();
                for (String s : suppliers) supplierDropdown.addItem(s);

                JTextField priceField = new JTextField();

                panel.add(new JLabel("Select Supplier:"));
                panel.add(supplierDropdown);
                panel.add(new JLabel("Cost Price (€):"));
                panel.add(priceField);

                int result = JOptionPane.showConfirmDialog(this, panel, "Assign Supplier to " + productName,
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION)
                    {
                        try
                            {
                                String selectedSupplier = (String) supplierDropdown.getSelectedItem();
                                double price = Double.parseDouble(priceField.getText());

                                com.petrolstation.dao.Product_DAO.Assign_Supplier_To_Product(productId, selectedSupplier, price);

                                JOptionPane.showMessageDialog(this, "Supplier assigned successfully!");
                            }
                        catch (NumberFormatException ex)
                            {
                                ex.printStackTrace(pw);
                                JOptionPane.showMessageDialog(this, "Please enter a valid price.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            }
                    }
            }

        private void Handle_Order_Product()
            {
                int selectedRow = dataTable.getSelectedRow();
                if (selectedRow == -1)
                    {
                        JOptionPane.showMessageDialog(this, "Please select a product to order.");
                        return;
                    }

                int productId = Integer.parseInt(dataTable.getValueAt(selectedRow, 0).toString());
                String productName = dataTable.getValueAt(selectedRow, 1).toString();
                int currentStock = Integer.parseInt(dataTable.getValueAt(selectedRow, 3).toString());
                int maxLevel = Integer.parseInt(dataTable.getValueAt(selectedRow, 5).toString());

                try (ResultSet rs = com.petrolstation.dao.Product_DAO.Get_Product_Supplier_Info(productId))
                    {
                        if (!rs.next())
                            {
                                JOptionPane.showMessageDialog(this, "No supplier assigned to this product yet.");
                                return;
                            }

                        String supplierName = rs.getString("Supplier_Name");
                        int supplierId = rs.getInt("Supplier_ID");
                        double unitPrice = rs.getDouble("cost_price");

                        String input = JOptionPane.showInputDialog(this,
                                "Supplier: " + supplierName + "\n" +
                                        "Current Stock: " + currentStock + " | Max Level: " + maxLevel + "\n" +
                                        "Enter quantity to order for " + productName + ":");

                        if (input != null && !input.isEmpty())
                            {
                                int orderQty = Integer.parseInt(input);

                                if (currentStock + orderQty > maxLevel)
                                    {
                                        JOptionPane.showMessageDialog(this, "Order denied! This would put stock at " +
                                                        (currentStock + orderQty) + ", which exceeds the Max Level of " + maxLevel + ".",
                                                "Overstock Warning", JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }

                                double totalPrice = orderQty * unitPrice;
                                int confirm = JOptionPane.showConfirmDialog(this,
                                        "Order Summary:\nProduct: " + productName + "\nSupplier: " + supplierName +
                                                "\nQuantity: " + orderQty + "\nTotal Cost: €" + String.format("%.2f", totalPrice) +
                                                "\n\nConfirm purchase?", "Final Confirmation", JOptionPane.YES_NO_OPTION);

                                if (confirm == JOptionPane.YES_OPTION)
                                {
                                    String user = com.petrolstation.gui.Login_Panel.Get_User(); //
                                    int managerId = com.petrolstation.dao.User_DAO.Return_UserID(user); //

                                    int newPoId = com.petrolstation.dao.Purchase_Order_DAO.Insert_PO_With_ID(supplierId, "Pending", managerId);
                                    com.petrolstation.dao.Purchase_Order_DAO.Insert_Order_Item(newPoId, productId, orderQty, unitPrice);

                                    JOptionPane.showMessageDialog(this, "Order #" + newPoId + " successfully placed!");
                                    Refresh_Orders();
                                }
                            }
                    }
                catch (Exception ex)
                    {
                        ex.printStackTrace(pw);
                        JOptionPane.showMessageDialog(this, "Error processing order: " + ex.getMessage());
                    }
            }

        private void Handle_Order_All_Low_Stock()
            {
                try (ResultSet rs = com.petrolstation.dao.Product_DAO.Get_Low_Stock_Info())
                    {
                        if (!rs.next())
                            {
                                JOptionPane.showMessageDialog(this, "All stock levels are currently above reorder level");
                                return;
                            }

                        StringBuilder breakdown = new StringBuilder("Low Stock Order Breakdown:\n");
                        breakdown.append(String.format("%-20s %-5s %-10s %-15s\n", "Product", "Qty", "Price", "Supplier"));
                        breakdown.append("------------------------------------------------------------\n");

                        double grandTotal = 0;
                        // Use a Map to group items by Supplier for the final database insertion
                        java.util.Map<Integer, java.util.List<Object[]>> supplierGroups = new java.util.HashMap<>();

                        rs.beforeFirst(); // Reset result set after the initial .next() check
                        while (rs.next())
                            {
                                int prodId = rs.getInt("Product_ID");
                                String name = rs.getString("Product_Name");
                                int current = rs.getInt("Current_Stock_Level");
                                int reorder = rs.getInt("Reorder_Level");
                                int supplierId = rs.getInt("Supplier_ID");
                                String supplierName = rs.getString("Supplier_Name");
                                double unitPrice = rs.getDouble("cost_price");

                                int qtyToOrder = (reorder + 1) - current;
                                double lineTotal = qtyToOrder * unitPrice;
                                grandTotal += lineTotal;

                                // Add to the popup text
                                breakdown.append(String.format("%-20s %-5d €%-9.2f %-15s\n", name, qtyToOrder, unitPrice, supplierName));

                                // Group for DB insertion
                                supplierGroups.computeIfAbsent(supplierId, k -> new java.util.ArrayList<>())
                                        .add(new Object[]{prodId, qtyToOrder, unitPrice});
                            }

                        breakdown.append("------------------------------------------------------------\n");
                        breakdown.append(String.format("Grand Total Cost: €%.2f", grandTotal));

                        // Display the breakdown
                        int confirm = JOptionPane.showConfirmDialog(this, new JTextArea(breakdown.toString()),
                                "Confirm Batch Order", JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION)
                            {
                                String managerUser = com.petrolstation.gui.Login_Panel.Get_User(); //
                                int managerId = com.petrolstation.dao.User_DAO.Return_UserID(managerUser);

                                // Create one Purchase Order per Supplier
                                for (Integer sId : supplierGroups.keySet())
                                    {
                                        int poId = com.petrolstation.dao.Purchase_Order_DAO.Insert_PO_With_ID(sId, "Pending", managerId);

                                        for (Object[] item : supplierGroups.get(sId))
                                            {
                                                com.petrolstation.dao.Purchase_Order_DAO.Insert_Order_Item(
                                                        poId, (int)item[0], (int)item[1], (double)item[2]);
                                            }
                                    }
                                JOptionPane.showMessageDialog(this, "All low stock orders have been placed successfully!");
                                Refresh_Orders();
                            }
                    }
                catch (Exception ex)
                    {
                        ex.printStackTrace(pw);
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    }
            }
    }