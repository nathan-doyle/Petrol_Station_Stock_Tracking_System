package com.petrolstation.dao;

import java.sql.*;
import java.util.ArrayList;

import static com.petrolstation.MyJDBC.pw;

public class User_DAO
    {
        public static void Add_User(String username, String password, boolean is_Manager)
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

                pstat = connection.prepareStatement("INSERT INTO users (Username, Password, Is_Manager) VALUES (?,?,?)");
                pstat.setString(1,username);
                pstat.setString(2,password);
                pstat.setBoolean(3, is_Manager);

                i= pstat.executeUpdate();
                IO.println(i + " user successfully added.");
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

        public static int Validate_Login(String username, String password) //RETRIEVE login details and validate that they match, plus check managerial status
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

                pstat = connection.prepareStatement("SELECT * FROM Users WHERE Username = ? AND Password = ?");
                pstat.setString(1, username);
                pstat.setString(2, password);

                resultSet = pstat.executeQuery();

                ResultSetMetaData metaData = resultSet.getMetaData();
                int Number_Of_Columns = metaData.getColumnCount();

                int isManager = 0;

                if (resultSet.next())
                    {
                        IO.println("Login Successful!");
                        boolean Man_Satus = resultSet.getBoolean(4);
                        if (Man_Satus)
                            {
                                IO.println("As a manager you have access to all functions");
                                isManager = 1;
                            }
                        else
                            {
                                IO.println("As a staff member you have limited access to functions");
                                isManager = 2;
                            }
                        return isManager;
                    }
                else
                    {
                        IO.println("Login Failed! \n Username or password incorrect \n Please try again");
                        isManager = 3;
                        return isManager;
                    }

            }
            catch (SQLException sqlException)
            {
                sqlException.printStackTrace(pw);
                pw.flush();
                return 0;
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

        public static int Return_UserID(String username)
        {
            Connection connection = null;
            PreparedStatement pstat = null;
            ResultSet resultSet = null;
            int UserID = -1;

            try
                {
                    connection = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/petrol_station_schema",
                                "root",
                                "110316"
                    );

                    pstat = connection.prepareStatement("SELECT User_ID FROM Users WHERE Username = ?");
                    pstat.setString(1, username);

                    resultSet = pstat.executeQuery();

                    if (resultSet.next())
                    {
                        UserID = resultSet.getInt("User_ID");
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
                            resultSet.close();
                            pstat.close();
                            connection.close();
                        }
                    catch (Exception e)
                        {
                            e.printStackTrace(pw);
                            pw.flush();
                        }
                }
            return UserID;
        }

        public static ArrayList<String> Get_All_Users()
            {
                // Initialize the list to store names
                ArrayList<String> names = new ArrayList<>();

                Connection connection = null;
                PreparedStatement pstat = null;
                ResultSet resultSet = null;

                try
                    {
                        connection = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/petrol_station_schema",
                                "root",
                                "110316"
                        );

                        String sql = "SELECT Username FROM users";
                        pstat = connection.prepareStatement(sql);

                        resultSet = pstat.executeQuery();

                        while (resultSet.next())
                            {
                                // Add each username from the DB to ArrayList
                                names.add(resultSet.getString("Username"));
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
                                resultSet.close();
                                pstat.close();
                                connection.close();
                            }
                        catch (Exception e)
                            {
                                e.printStackTrace(pw);
                                pw.flush();
                            }
                    }
                return names; // Return the list of usernames to the GUI
            }
    }

