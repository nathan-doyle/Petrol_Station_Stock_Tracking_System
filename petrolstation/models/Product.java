package com.petrolstation.models;
// NAME: Nathan Doyle
// StudentID: C00310640
// Date_Start: 15/1/26
// Date_End:
// Purpose: Create a class for products

public class Product
    {
        private int productId;
        private String name;
        private String barcode;
        private int currentStock;
        private int reorderLevel;
        private int maxLevel;
        private String category;

        // Constructor for products
        public Product(int productId, String name, String barcode, int currentStock, int reorderLevel, int maxLevel, String category)
            {
                this.productId = productId;
                this.name = name;
                this.barcode = barcode;
                this.currentStock = currentStock;
                this.reorderLevel = reorderLevel;
                this.maxLevel = maxLevel;
                this.category = category;
            }

        // Getters
        public String getName()
            {
                return name;
            }
        public String getBarcode()
            {
                return barcode;
            }
        public int getCurrentStock()
            {
                return currentStock;
            }
    }
