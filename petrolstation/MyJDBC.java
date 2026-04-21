package com.petrolstation;
// NAME: Nathan Doyle
// StudentID: C00310640
// Date_Start: 15/1/26
// Date_End:
// Purpose: Main class for project

import com.petrolstation.gui.Login_Frame;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import static com.petrolstation.dao.Supplier_DAO.*;

public class MyJDBC
    {
        public static PrintWriter pw;

        public static void main(String[] args) throws FileNotFoundException
            {
                pw = new PrintWriter(new FileOutputStream("log.txt", true));
                pw.println("Starting...");
                Runtime.getRuntime().addShutdownHook(new Thread(() ->
                    {
                        if (pw != null)
                        {
                            pw.println("Closing...");
                            pw.close();
                        }
                    }));

                //Insert_PO(1,"On the way",1);
                //Get_All_Products();
                //Update_Stock(1, 24);
                //Get_Low_Stock();
                //Add_User(Hello,1111, 1);
                //Validate_Login("Test1", "Test")
                //Get_Product_Suppliers();
                //Get_All_Orders();
                //Remove_Purchase_Order(1);
                //Update_Order_Status(5, "Arrived");
                //Insert_Discrepancy(5,1,24,20,"Damaged",1);
                //Get_All_Discreoancies();
                //Get_Orders_By_Status("Arrived");
                //Add_New_Supplier("test", "Test@gmail.com", "0892060179");



                //Opens the GUI login frame
                Login_Frame login = new Login_Frame();
                login.setVisible(true);

            }
    }
