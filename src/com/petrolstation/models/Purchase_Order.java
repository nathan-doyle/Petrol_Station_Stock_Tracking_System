package com.petrolstation.models;
// NAME: Nathan Doyle
// StudentID: C00310640
// Date_Start: 15/1/26
// Date_End:
// Purpose: Create a class for purchase orders

public class Purchase_Order
    {
        private int poId;
        private int supplierId;
        private String status; // e.g., "Draft", "Awaiting Delivery"
        private java.sql.Timestamp timeStamp;
        private int managerId; // FK to com.petrolstation.models.User table

        // Constructor for purchase orders
        public Purchase_Order(int poId, int supplierId, String status, java.sql.Timestamp timeStamp, int managerId)
            {
                this.poId = poId;
                this.supplierId = supplierId;
                this.status = status;
                this.timeStamp = timeStamp;
                this.managerId = managerId;
            }
    }