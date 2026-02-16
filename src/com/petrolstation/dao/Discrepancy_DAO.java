package com.petrolstation.dao;

import java.sql.*;

public class Discrepancy_DAO
    {
        public static void Insert_Discrepancy(int poID, int prodID, int expected, int actual, String reason, int staffID) //CREATE stock discrepancy report
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

                        String sql = "INSERT INTO discrepancy_reports (PO_ID, Product_ID, Expected_QTY, Actual_QTY, Reason_Code, Reported_By) " +
                                "VALUES (?, ?, ?, ?, ?, ?)";

                        pstat = connection.prepareStatement(sql);
                        pstat.setInt(1, poID);
                        pstat.setInt(2, prodID);
                        pstat.setInt(3, expected);
                        pstat.setInt(4, actual);
                        pstat.setString(5, reason);
                        pstat.setInt(6, staffID);

                        int rowsInserted = pstat.executeUpdate();

                        if (rowsInserted > 0)
                            {
                                IO.println("Discrepancy logged successfully for Order #" + poID);
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

        public static void Get_All_Discreoancies()
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

                        pstat = connection.prepareStatement("SELECT * FROM discrepancy_reports");

                        resultSet = pstat.executeQuery();

                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int Number_Of_Columns = metaData.getColumnCount();
                        IO.println("Petrol station table of discrepancies:\n");

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
    }
