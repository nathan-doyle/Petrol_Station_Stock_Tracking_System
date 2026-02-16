package com.petrolstation.gui;

import javax.swing.table.AbstractTableModel;
import java.sql.*;

public class Result_Set_Table_Model extends AbstractTableModel
    {
        private ResultSet resultSet;
        private ResultSetMetaData metaData;
        private int numberOfRows;

        // Constructor that takes a ResultSet from DAO
        public Result_Set_Table_Model(ResultSet resultSet) throws SQLException
            {
                this.resultSet = resultSet;
                this.metaData = resultSet.getMetaData();

                // Determine the number of rows in the ResultSet
                resultSet.last(); // Move to the last row
                numberOfRows = resultSet.getRow(); // Get the row number

                // Notify the JTable that data has changed
                fireTableDataChanged();
            }

        @Override
        public int getColumnCount()
            {
                try
                    {
                        return metaData.getColumnCount();
                    }
                catch (SQLException e)
                    {
                        e.printStackTrace();
                        return 0;
                    }
            }

        @Override
        public int getRowCount()
            {
                return numberOfRows;
            }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) //Gets value at a specific cell in table
            {
                try
                    {
                        resultSet.absolute(rowIndex + 1); // Jump to the specific row
                        return resultSet.getObject(columnIndex + 1); // Get the data
                    }
                catch (SQLException e)
                    {
                        e.printStackTrace();
                        return null;
                    }
            }

        // Show the SQL column names as headers
        @Override
        public String getColumnName(int column)
            {
                try
                    {
                        return metaData.getColumnName(column + 1);
                    }
                catch (SQLException e)
                    {
                        e.printStackTrace();
                        return "";
                    }
            }
    }