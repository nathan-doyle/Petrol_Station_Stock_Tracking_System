package com.petrolstation.dao;

import java.sql.*;

import static com.petrolstation.MyJDBC.pw;

public class Purchase_Order_DAO
    {
        public static void Insert_PO(int SupplierID, String stat, int manID) throws SQLException //CREATE purchase order
            {
                Connection connection = null;
                PreparedStatement pstat = null;
                int i = 0;
                        connection = DriverManager.getConnection(
                                "jdbc:mysql://127.0.0.1:3306/petrol_station_schema",
                                "root",
                                "110316"
                        );

                        java.sql.Timestamp currentTs = new java.sql.Timestamp(System.currentTimeMillis());

                        pstat = connection.prepareStatement("INSERT INTO purchase_orders (Supplier_ID, Status, Manager_ID, TimeStamp) VALUES (?, ?, ?, ?)");
                        pstat.setInt(1, SupplierID);
                        pstat.setString(2, stat);
                        pstat.setInt(3, manID);
                        pstat.setTimestamp(4, currentTs); // Sets the current date and time

                        i= pstat.executeUpdate();
                        IO.println(i + " record successfully added to the table.");

            }

        public static ResultSet Get_All_Orders() //RETRIEVE all orders
            {
                Connection connection = null;
                PreparedStatement pstat = null;
                ResultSet resultSet = null;
                try
                    {
                        connection = DriverManager.getConnection(
                                "jdbc:mysql://127.0.0.1:3306/petrol_station_schema",
                                "root",
                                "110316"
                        );

                        pstat = connection.prepareStatement("SELECT purchase_orders.PO_ID, suppliers.Supplier_Name, purchase_orders.Status, purchase_orders.TimeStamp, users.Username FROM (purchase_orders INNER JOIN suppliers ON purchase_orders.Supplier_ID = suppliers.Supplier_ID) INNER JOIN users ON purchase_orders.Manager_ID = users.User_ID", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        resultSet = pstat.executeQuery();
                        return resultSet;

                        /*
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int Number_Of_Columns = metaData.getColumnCount();
                        IO.println("Petrol station table of orders:\n");

                        while (resultSet.next())
                            {
                                for (int i = 1; i <= Number_Of_Columns; i++)
                                    IO.print(resultSet.getObject(i) + "\t\t");
                                IO.println();
                            }
                         */
                    }
                catch (SQLException sqlException)
                    {
                        sqlException.printStackTrace();
                        pw.flush();
                        return null;
                    }
                /*finally
                    {
                        try
                            {
                                resultSet.close();
                                pstat.close();
                                connection.close();
                            }
                        catch (Exception exception)
                            {
                                exception.printStackTrace();
                            }
                    }
                 */

            }

        public static void Get_Orders_By_Status(String status)
            {
                Connection connection = null;
                PreparedStatement pstat = null;
                ResultSet resultSet = null;
                try
                    {
                        connection = DriverManager.getConnection(
                                "jdbc:mysql://127.0.0.1:3306/petrol_station_schema",
                                "root",
                                "110316"
                        );

                        pstat = connection.prepareStatement("SELECT * FROM purchase_orders WHERE Status = ?");
                        pstat.setString(1, status);

                        resultSet = pstat.executeQuery();

                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int Number_Of_Columns = metaData.getColumnCount();
                        IO.println("Petrol station table of orders:\n");

                        if (!resultSet.next())
                            {
                                IO.println("No orders with status " + status);
                            }
                        else
                            {
                                 do
                                    {
                                        for (int i = 1; i <= Number_Of_Columns; i++)
                                            IO.print(resultSet.getObject(i) + "\t\t");
                                        IO.println();
                                    }
                                    while (resultSet.next());
                            }
                    }
                catch (SQLException sqlException)
                {
                    sqlException.printStackTrace(pw);
                    pw.flush();
                }
                finally
                {
                    try
                    {
                        resultSet.close();
                        pstat.close();
                        connection.close();
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace(pw);
                        pw.flush();
                    }
                }

            }

        public static void Update_Order_Status(int poID, String newStatus) //UPDATE a purchase order
            {
                Connection connection = null;
                PreparedStatement pstat = null;

                try
                {
                    connection = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/petrol_station_schema",
                            "root",
                            "110316"
                    );

                    String sql = "UPDATE purchase_orders SET Status = ? WHERE PO_ID = ?";
                    pstat = connection.prepareStatement(sql);

                    pstat.setString(1, newStatus);
                    pstat.setInt(2, poID);

                    int i = pstat.executeUpdate();

                    if (i > 0)
                    {
                        // A row existed and was updated
                        IO.println("Order " + poID + " status updated to: " + newStatus);;
                    }
                    else
                    {
                        // The ID provided didn't match any row in the table
                        IO.println("Error: Order ID " + poID + " does not exist. No changes made.");
                    }
                }
                catch (SQLException e)
                {
                    e.printStackTrace(pw);
                    pw.flush();
                }
                finally
                {
                    try
                    {
                        pstat.close();
                        connection.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace(pw);
                        pw.flush();
                    }
                }
            }

        public static void Process_Delivery(int poID) throws SQLException
        {
            String fetchItemsSql = "SELECT Product_ID, Quantity_Ordered FROM order_items WHERE PO_ID = ?";
            String updateItemSql = "UPDATE order_items SET Quantity_Received = ? WHERE PO_ID = ? AND Product_ID = ?";

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/petrol_station_schema", "root", "110316"))
                {
                    PreparedStatement psFetch = conn.prepareStatement(fetchItemsSql);
                    psFetch.setInt(1, poID);
                    ResultSet rs = psFetch.executeQuery();

                    while (rs.next())
                        {
                            int prodID = rs.getInt("Product_ID");
                            int qtyOrdered = rs.getInt("Quantity_Ordered");

                            // We assume full delivery here.
                            PreparedStatement psItem = conn.prepareStatement(updateItemSql);
                            psItem.setInt(1, qtyOrdered);
                            psItem.setInt(2, poID);
                            psItem.setInt(3, prodID);
                            psItem.executeUpdate();

                            com.petrolstation.dao.Product_DAO.Update_Stock(prodID, qtyOrdered);
                        }

                    Update_Order_Status(poID, "Arrived");
                }
        }

        public static void Remove_Purchase_Order(int poID) //DELETE a purchase order
            {
                Connection connection = null;
                PreparedStatement pstat = null;

                try
                    {
                        connection = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/petrol_station_schema",
                                "root",
                                "110316"
                        );

                        pstat = connection.prepareStatement("DELETE FROM purchase_orders WHERE PO_ID = ?");
                        pstat.setInt(1, poID);

                        int rowsDeleted = pstat.executeUpdate();

                        if (rowsDeleted > 0)
                            {
                                IO.println("Order " + poID + " was successfully removed.");
                            }
                        else
                            {
                                IO.println("No purchase order was deleted");
                            }
                    }

                catch (SQLException e)
                    {
                        e.printStackTrace(pw);
                        pw.flush();
                    }
                finally
                    {
                        try
                            {
                                pstat.close();
                                connection.close();
                            }
                        catch (Exception e)
                            {
                                e.printStackTrace(pw);
                                pw.flush();
                            }
                    }
            }

        public static int Insert_PO_With_ID(int SupplierID, String stat, int manID) throws SQLException
            {
                String sql = "INSERT INTO purchase_orders (Supplier_ID, Status, Manager_ID, TimeStamp) VALUES (?, ?, ?, ?)";
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/petrol_station_schema", "root", "110316");
                     PreparedStatement pstat = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
                    {

                        pstat.setInt(1, SupplierID);
                        pstat.setString(2, stat);
                        pstat.setInt(3, manID);
                        pstat.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));

                        pstat.executeUpdate();
                        ResultSet rs = pstat.getGeneratedKeys();
                        if (rs.next()) return rs.getInt(1); // Return the new PO_ID
                    }
                return -1;
            }

        public static void Insert_Order_Item(int poID, int prodID, int qty, double price) throws SQLException
            {
                String sql = "INSERT INTO order_items (PO_ID, Product_ID, Quantity_Ordered) VALUES (?, ?, ?)";
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/petrol_station_schema", "root", "110316");
                     PreparedStatement pstat = connection.prepareStatement(sql))
                    {
                        pstat.setInt(1, poID);
                        pstat.setInt(2, prodID);
                        pstat.setInt(3, qty);
                        pstat.executeUpdate();
                    }
            }

        public static ResultSet Get_Order_Items(int poID) throws SQLException
            {
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1:3306/petrol_station_schema", "root", "110316");

                String sql = "SELECT oi.Product_ID, p.Product_Name, oi.Quantity_Ordered " +
                        "FROM order_items oi " +
                        "INNER JOIN products p ON oi.Product_ID = p.Product_ID " +
                        "WHERE oi.PO_ID = ?";

                PreparedStatement pstat = connection.prepareStatement(sql,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                pstat.setInt(1, poID);
                return pstat.executeQuery();
            }
    }
