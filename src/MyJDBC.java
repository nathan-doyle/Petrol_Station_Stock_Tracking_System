// NAME: Nathan Doyle
// StudentID: C00310640
// Date_Start: 15/1/26
// Date_End:
// Purpose: Main class for project

import com.petrolstation.gui.Login_Frame;

import java.sql.*;

import static com.petrolstation.dao.Discrepancy_DAO.Get_All_Discreoancies;
import static com.petrolstation.dao.Discrepancy_DAO.Insert_Discrepancy;
import static com.petrolstation.dao.Product_DAO.*;
import static com.petrolstation.dao.Purchase_Order_DAO.*;
import static com.petrolstation.dao.User_DAO.Add_User;
import static com.petrolstation.dao.User_DAO.Validate_Login;

public class MyJDBC
    {
        public static void main(String[] args)
            {
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


                //Opens the GUI login frame
                Login_Frame login = new Login_Frame();
                login.setVisible(true);
            }
    }
