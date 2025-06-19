# DBT-Banking-System
A secure and persistent banking management system built with Java that allows customers, employees, and administrators to manage accounts, transactions, and user access with robust data storage and security features.

This  is a comprehensive banking system application built with Java that provides functionalities for customers, employees, and administrators. The system features persistent data storage, transaction tracking, account management, and security features.

Features
Customer Features
* Account creation and login
* Deposit, withdrawal, and transfer operations
* View account details and transaction history
* Secure password protection
* Secure login with account locking after 3 failed attempts
* Transaction history tracking

Employee Features
* Customer account management
* Add new customers
* Password reset functionality
* View all customer information
* Apply monthly interest to savings accounts

Admin Features
* Employee management
* Password reset for employees
* Account unlocking
* System-wide operations
* Comprehensive reporting

System Features
* Automatic branch assignment based on customer name
* Peresistent data storage using text files
* Password complexity enforcement
* Transaction logging
* Error handling and input validation

Security Features
* Password requirements: 8+ characters with at least 1 special character
* Account locking after 3 failes login attempts
* Admin-controlled account unlocking 

Technical Specifications
* Language: Java
* Data Storage: Flat file system (CSV format)
* Security: Account locking, password validation
* Error Handling: Comprehensive input validation

Installation
Prerequisites
* Java JDK 11 or later
* Maven (for building)

Setup
1. Clone the repository:
   git clone https://github.com/DTshiyeya/dbt-banking-system.git
   cd dbt-banking-system

2. Navigate to the project directory:
   cd banking-system

3. Compile the project:
   javac bankingsystem/BankingSystem.java

4. Run the application:
   java bankingsystem.BankingSystem

Default Admin Credentials
* Username: admin
* Password: admin123

System Usage
Main Menu Options
1. Customer Sign Up: Create a new account
2. Customer Login: Access your banking dashboard
3. Employee Login: Access employee functions
4. Admin Login: Access administrator functions
5. Exit: Close the application

Customer Dashboard
* View account details
* Deposit money
* Withdraw money
* Trnasfer funds
* View transaction history
* Logout 

File Structure

banking-system/
├── src/
│   └── bankingsystem/
│       └── BankingSystem.java
├── data/
│   ├── customers.txt
│   ├── employees.txt
│   ├── transactions.txt
│   └── locked_accounts.txt
├── README.md
└── LICENSE

The system uses four data files:
1. customers.txt - Stores customer account information
2. employees.txt - Stores employee records
3. transactions.txt - Logs all financial transactions
4. locked_accounts.txt  - Tracks locked usernames

Important Notes
1. Data Persistence
The system maintains data in text files that persist between sessions:
  * Customer records
  * Employee records
  * Transaction history
  * Locked account information

2. Security
* Passwords are stored in plain text (not recommended for production)
* Consider adding password hashing for real-world use

Future Enhancements
1. Implement password hashing for security
2. Add database support instead of file storage
3. Implement interest scheduling
4. Add email/SMS notifications
5. Develop a web-based interface

Contributing
Contributions are welcome! Please follow these steps:
* Fork the repository
* Create your feature branch (git checkout -b feature/your-feature)
* Commit your changes (git commit -m 'Add some feature')
* Push to the branch (git push origin feature/your-feature)
* Open a pull request

Contact
For questions or support, please contact:
Deborah Tshiyeya - tshiyeya@example.com

Project Link: https://github.com/DTshiyeya/dbt-banking-system

Note: This is a demonstration application. Not recommended for production use with real financial data. Always follow security best practices when handling sensitive information.
