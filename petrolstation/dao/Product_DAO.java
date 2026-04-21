package com.petrolstation.dao;

import java.sql.*;

import static com.petrolstation.MyJDBC.pw;

public class Product_DAO
    {
        public static void Add_New_Product(String name, String barcode, int stock, int reorder, int max)//CREATE new product
            {
                Connection connection = null;
                PreparedStatement pstat = null;
                int i = 0;
                try
                    {
                        connection = DriverManager.getConnection(
                                "jdbc:mysql://127.0.0.1:3306/petrol_station_schema",
                                "root",
                                "110316"
                        );

                        pstat = connection.prepareStatement("INSERT INTO products (Product_Name, Barcode, Current_Stock_Level, Reorder_Level, Max_Level) VALUES (?, ?, ?, ?, ?)");
                        pstat.setString(1, name);
                        pstat.setString(2, barcode);
                        pstat.setInt(3, stock);
                        pstat.setInt(4, reorder);
                        pstat.setInt(5, max);

                        i= pstat.executeUpdate();
                        IO.println(i + " Product successfully added.");
                    }
                catch (SQLException sqlException)
                    {
                        sqlException.printStackTrace(pw);
                    }
                finally
                    {
                        try
                            {
                                pstat.close();
                                connection.close();
                            }
                        catch (Exception exception)
                            {
                                exception.printStackTrace(pw);
                            }
                    }

            }

        public static ResultSet Get_All_Products() //RETRIEVE all products
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

                        pstat = connection.prepareStatement("SELECT * FROM products",ResultSet.TYPE_SCROLL_INSENSITIVE,
                                ResultSet.CONCUR_READ_ONLY);

                        resultSet = pstat.executeQuery();
                        return resultSet;

                        /*
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int Number_Of_Columns = metaData.getColumnCount();
                        IO.println("Petrol station table of products:\n");

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
                        sqlException.printStackTrace(pw);
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
                                exception.printStackTrace(pw);
                            }
                    }
                 */

            }

        public static void Update_Stock(int Product_ID, int New_Quantity) //UPDATE stock
            {
                Connection connection = null;
                PreparedStatement pstat = null;
                ResultSet rs = null;
                int Current_Stock = 0;
                try
                    {
                        connection = DriverManager.getConnection(
                                "jdbc:mysql://127.0.0.1:3306/petrol_station_schema",
                                "root",
                                "110316"
                        );

                        //Retrieve current stock level
                        pstat = connection.prepareStatement("SELECT Current_Stock_Level FROM products WHERE Product_ID = ?");
                        pstat.setInt(1, Product_ID);
                        rs = pstat.executeQuery();

                        if (rs.next())
                        {
                            Current_Stock = rs.getInt("Current_Stock_Level");
                        }

                        //UPDATE current stock level
                        int Updated_Total = Current_Stock + New_Quantity;

                        //Reusing pstat as I'm done with using it for retrieving
                        pstat = connection.prepareStatement("UPDATE products SET Current_Stock_Level = ? WHERE Product_ID = ?");
                        pstat.setInt(1, Updated_Total);
                        pstat.setInt(2, Product_ID);

                        int Affected_Rows = pstat.executeUpdate();
                        IO.println(Affected_Rows + " record successsfully updated. New total: " + Updated_Total);
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
                                rs.close();
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

        public static void Get_Low_Stock() //RETRIEVE all products with current stock level below reorder level
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

                    pstat = connection.prepareStatement("SELECT * FROM products WHERE Current_Stock_Level <= Reorder_Level");

                    resultSet = pstat.executeQuery();

                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int Number_Of_Columns = metaData.getColumnCount();
                    IO.println("Petrol station table of products low on stock:\n");

                    while (resultSet.next())
                    {
                        for (int i = 1; i <= Number_Of_Columns; i++)
                            IO.print(resultSet.getObject(i) + "\t\t");
                        IO.println();
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

        public static void Get_Product_Suppliers() //RETRIEVE all suppliers of a product
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

                String sql = "SELECT p.Product_Name, s.Supplier_Name, sp.cost_price " +
                        "FROM products p " +
                        "INNER JOIN supplier_products sp ON p.Product_ID = sp.Product_ID " +
                        "INNER JOIN suppliers s ON sp.Supplier_ID = s.Supplier_ID";

                pstat = connection.prepareStatement(sql);

                resultSet = pstat.executeQuery();

                IO.println("These suppliers supply this product:\n");

                while (resultSet.next())
                {
                    String Product_Name = resultSet.getString("Product_Name");
                    String Supplier_Name = resultSet.getString("Supplier_Name");
                    double price = resultSet.getDouble("cost_price");

                    IO.println("Product: " + Product_Name + " | Supplier: " + Supplier_Name + " | Price: €" + price);
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

        public static void Remove_Product(int Product_ID) //DELETE a product
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

                        pstat = connection.prepareStatement("DELETE FROM products WHERE Product_ID = ?");
                        pstat.setInt(1, Product_ID);

                        int rowsDeleted = pstat.executeUpdate();

                        if (rowsDeleted > 0)
                            {
                                IO.println("Product " + Product_ID + " was successfully removed.");
                            }
                        else
                            {
                                IO.println("No product was deleted");
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

        public static java.util.ArrayList<String> Get_All_Supplier_Names() // Method to get all supplier names for the JComboBox
            {
                java.util.ArrayList<String> supplierList = new java.util.ArrayList<>();
                String sql = "SELECT Supplier_Name FROM suppliers";

                try (Connection conn = DriverManager.getConnection
                        (
                            "jdbc:mysql://localhost:3306/petrol_station_schema",
                            "root",
                            "110316"
                        );
                     PreparedStatement pstat = conn.prepareStatement(sql);
                     ResultSet rs = pstat.executeQuery())
                    {

                        while (rs.next())
                            {
                                supplierList.add(rs.getString("Supplier_Name"));
                            }
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace(pw);
                    }
                return supplierList;
            }


        public static void Assign_Supplier_To_Product(int productId, String supplierName, double costPrice) // Method to link the product to a supplier
            {
                String findIdSql = "SELECT Supplier_ID FROM suppliers WHERE Supplier_Name = ?";
                String insertSql = "INSERT INTO supplier_products (Product_ID, Supplier_ID, cost_price) " +
                        "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE cost_price = ?";

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/petrol_station_schema", "root", "110316"))
                    {

                        int supplierId = -1;
                        try (PreparedStatement psId = conn.prepareStatement(findIdSql))
                            {
                                psId.setString(1, supplierName);
                                ResultSet rs = psId.executeQuery();
                                if (rs.next()) supplierId = rs.getInt("Supplier_ID");
                            }

                        if (supplierId != -1)
                            {
                                try (PreparedStatement psInsert = conn.prepareStatement(insertSql))
                                    {
                                        psInsert.setInt(1, productId);
                                        psInsert.setInt(2, supplierId);
                                        psInsert.setDouble(3, costPrice);
                                        psInsert.setDouble(4, costPrice); // For the UPDATE part
                                        psInsert.executeUpdate();
                                    }
                            }
                    }
                catch (SQLException e)
                    {
                        e.printStackTrace(pw);
                    }
            }

        public static ResultSet Get_Product_Supplier_Info(int productId)
            {
                String sql = "SELECT s.Supplier_Name, s.Supplier_ID, sp.cost_price " +
                        "FROM suppliers s " +
                        "INNER JOIN supplier_products sp ON s.Supplier_ID = sp.Supplier_ID " +
                        "WHERE sp.Product_ID = ?";
                try
                    {
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/petrol_station_schema", "root", "110316");
                        PreparedStatement pstat = conn.prepareStatement(sql);
                        pstat.setInt(1, productId);
                        return pstat.executeQuery();
                    }
                catch (SQLException e)
                    {
                        e.printStackTrace(pw);
                        return null;
                    }
            }

        public static ResultSet Get_Low_Stock_Info() throws SQLException
            {
                try
                    {
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/petrol_station_schema", "root", "110316");

                        String sql = "SELECT p.Product_ID, p.Product_Name, p.Current_Stock_Level, p.Reorder_Level, " +
                                "s.Supplier_ID, s.Supplier_Name, sp.cost_price " +
                                "FROM products p " +
                                "JOIN supplier_products sp ON p.Product_ID = sp.Product_ID " +
                                "JOIN suppliers s ON sp.Supplier_ID = s.Supplier_ID " +
                                "WHERE p.Current_Stock_Level <= p.Reorder_Level";

                        PreparedStatement pstat = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        return pstat.executeQuery();
                    }
                catch (SQLException e)
                    {
                        e.printStackTrace(pw);
                        return null;
                    }

            }
    }
