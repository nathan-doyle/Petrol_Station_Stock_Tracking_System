package com.petrolstation.dao;

import java.sql.*;

import static com.petrolstation.MyJDBC.pw;

public class Supplier_DAO
    {
        public static void Add_New_Supplier(String name, String email, String phone)//CREATE new product
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

                pstat = connection.prepareStatement("INSERT INTO suppliers (Supplier_Name, Supplier_Email, Supplier_Phone) VALUES (?, ?, ?)");
                pstat.setString(1, name);
                pstat.setString(2, email);
                pstat.setString(3, phone);

                i = pstat.executeUpdate();
                IO.println(i + " Supplier successfully added.");
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

        public static ResultSet Get_All_Suppliers() //RETRIEVE all products
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

                pstat = connection.prepareStatement("SELECT * FROM suppliers",ResultSet.TYPE_SCROLL_INSENSITIVE,
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

        public static void Remove_Supplier(int id)
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

                    pstat = connection.prepareStatement("DELETE FROM suppliers WHERE Supplier_ID = ?");
                    pstat.setInt(1, id);

                    int rowsDeleted = pstat.executeUpdate();

                    if (rowsDeleted > 0)
                    {
                        IO.println("Supplier " + id + " was successfully removed.");
                    }
                    else
                    {
                        IO.println("No supplier was deleted");
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
    }
