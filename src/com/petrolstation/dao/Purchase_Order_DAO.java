package com.petrolstation.dao;

import java.sql.*;

public class Purchase_Order_DAO
    {
        public static void Insert_PO(int SupplierID, String stat, int manID) //CREATE purchase order
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

                        java.sql.Timestamp currentTs = new java.sql.Timestamp(System.currentTimeMillis());

                        pstat = connection.prepareStatement("INSERT INTO purchase_orders (Supplier_ID, Status, Manager_ID, TimeStamp) VALUES (?, ?, ?, ?)");
                        pstat.setInt(1, SupplierID);
                        pstat.setString(2, stat);
                        pstat.setInt(3, manID);
                        pstat.setTimestamp(4, currentTs); // Sets the current date and time

                        i= pstat.executeUpdate();
                        IO.println(i + " record successfully added to the table.");
                    }
                catch (SQLException sqlException)
                    {
                        sqlException.printStackTrace();
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
                                exception.printStackTrace();
                            }
                    }

            }

        public static void Get_All_Orders() //RETRIEVE all orders
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

                        pstat = connection.prepareStatement("SELECT * FROM purchase_orders");

                        resultSet = pstat.executeQuery();

                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int Number_Of_Columns = metaData.getColumnCount();
                        IO.println("Petrol station table of orders:\n");

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
                    e.printStackTrace();
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
                        e.printStackTrace();
                    }
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
                        e.printStackTrace();
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
                                e.printStackTrace();
                            }
                    }
            }
    }
