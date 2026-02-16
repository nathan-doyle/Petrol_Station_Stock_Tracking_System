package com.petrolstation.dao;

import java.sql.*;

public class Product_DAO
    {
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
                        sqlException.printStackTrace();
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

        public static void Update_Stock(int productID, int New_Quantity) //CREATE purchase order
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
                        pstat.setInt(1, productID);
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
                        pstat.setInt(2, productID);

                        int Affected_Rows = pstat.executeUpdate();
                        IO.println(Affected_Rows + " record successsfully updated. New total: " + Updated_Total);
                    }
                catch (SQLException sqlException)
                    {
                        sqlException.printStackTrace();
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
                                exception.printStackTrace();
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
                    sqlException.printStackTrace();
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
                        exception.printStackTrace();
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
                sqlException.printStackTrace();
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
                    exception.printStackTrace();
                }
            }

        }
    }
