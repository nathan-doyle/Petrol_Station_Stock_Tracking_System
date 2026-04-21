package com.petrolstation.dao;

import java.io.PrintWriter;
import java.sql.*;

import static com.petrolstation.MyJDBC.pw;

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

        public static ResultSet Get_All_Discrepancies()
            {
                Connection connection = null;
                PreparedStatement pstat = null;
                ResultSet resultSet = null;
                try {
                    connection = DriverManager.getConnection(
                            "jdbc:mysql://127.0.0.1:3306/petrol_station_schema",
                            "root",
                            "110316"
                    );

                    pstat = connection.prepareStatement("SELECT discrepancy_reports.Report_ID, discrepancy_reports.PO_ID, products.Product_Name, discrepancy_reports.Expected_QTY, discrepancy_reports.Actual_QTY, discrepancy_reports.Reason_Code, users.Username FROM (discrepancy_reports INNER JOIN products ON discrepancy_reports.Product_ID = products.Product_ID) INNER JOIN users ON users.User_ID = discrepancy_reports.Reported_By", ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);

                    resultSet = pstat.executeQuery();
                    return resultSet;

                            /*
                            ResultSetMetaData metaData = resultSet.getMetaData();
                            int Number_Of_Columns = metaData.getColumnCount();
                            IO.println("Petrol station table of discrepancies:\n");

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
                    /*
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

                     */
            }

        public static void Process_Discrepancy_Delivery(int poID, int prodID, int expected, int actual, String reason, int staffID) throws SQLException
            {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/petrol_station_schema", "root", "110316"))
                    {
                        conn.setAutoCommit(false);

                        try
                            {
                                String reportSql = "INSERT INTO discrepancy_reports (PO_ID, Product_ID, Expected_QTY, Actual_QTY, Reason_Code, Reported_By) VALUES (?, ?, ?, ?, ?, ?)";
                                PreparedStatement psReport = conn.prepareStatement(reportSql);
                                psReport.setInt(1, poID);
                                psReport.setInt(2, prodID);
                                psReport.setInt(3, expected);
                                psReport.setInt(4, actual);
                                psReport.setString(5, reason);
                                psReport.setInt(6, staffID);
                                psReport.executeUpdate();

                                int diff = expected - actual;

                                String stockSql = "UPDATE products SET Current_Stock_Level = Current_Stock_Level - ? WHERE Product_ID = ?";
                                PreparedStatement psStock = conn.prepareStatement(stockSql);
                                psStock.setInt(1, diff);
                                psStock.setInt(2, prodID);
                                psStock.executeUpdate();

                                String orderItemSql = "UPDATE order_items SET Quantity_Received = ? WHERE PO_ID = ? AND Product_ID = ?";
                                PreparedStatement psOrder = conn.prepareStatement(orderItemSql);
                                psOrder.setInt(1, actual);
                                psOrder.setInt(2, poID);
                                psOrder.setInt(3, prodID);
                                psOrder.executeUpdate();

                                conn.commit(); // Save all changes at once
                            }
                        catch (SQLException e)
                            {
                                e.printStackTrace(pw);
                                conn.rollback(); // Undo everything if one part fails
                                throw e;
                            }
                    }
            }
    }
