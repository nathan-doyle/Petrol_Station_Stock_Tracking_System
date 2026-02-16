package com.petrolstation.models;
// NAME: Nathan Doyle
// StudentID: C00310640
// Date_Start: 15/1/26
// Date_End:
// Purpose: Create a class for users of the system
public class User
    {
        private int userId;
        private String username;
        private String password;
        private boolean isManager;

        // Constructor for creating a new user
        public User(int userId, String username, String password, boolean isManager)
            {
                this.userId = userId;
                this.username = username;
                this.password = password;
                this.isManager = isManager;
            }

        // Getters
        public int getUserId()
            {
                return userId;
            }
        public String getUsername()
            {
                return username;
            }
        public boolean isManager()
            {
                return isManager;
            }
    }
