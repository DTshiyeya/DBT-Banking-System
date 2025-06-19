package bankingsystem;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class BankingSystem {
    // System constants
    static final int MAX_CUSTOMERS = 1000;
    static final int MAX_EMPLOYEES = 100;
    static final int MAX_TRANSACTIONS = 10000;
    static final double SAVINGS_INTEREST_RATE = 0.02; // 2% annual
    
    // File paths
    static final String CUSTOMER_FILE = "customers.txt";
    static final String EMPLOYEE_FILE = "employees.txt";
    static final String TRANSACTION_FILE = "transactions.txt";
    static final String LOCKED_ACCOUNTS_FILE = "locked_accounts.txt";
    
    // Customer data arrays
    static String[] customerName = new String[MAX_CUSTOMERS];
    static String[] customerSurname = new String[MAX_CUSTOMERS];
    static String[] usernames = new String[MAX_CUSTOMERS];
    static String[] emails = new String[MAX_CUSTOMERS];
    static String[] passwords = new String[MAX_CUSTOMERS];
    static String[] accountTypes = new String[MAX_CUSTOMERS];
    static String[] accountNumbers = new String[MAX_CUSTOMERS];
    static String[] cardNumbers = new String[MAX_CUSTOMERS];
    static String[] expiryDates = new String[MAX_CUSTOMERS];
    static String[] securityCodes = new String[MAX_CUSTOMERS];
    static double[] balances = new double[MAX_CUSTOMERS];
    static String[] branches = new String[MAX_CUSTOMERS];
    static String[] phoneNumbers = new String[MAX_CUSTOMERS];
    static int customerCount = 0;
    
    // Employee data arrays
    static String[] employeeUsernames = new String[MAX_EMPLOYEES];
    static String[] employeePasswords = new String[MAX_EMPLOYEES];
    static String[] employeeBranches = new String[MAX_EMPLOYEES];
    static String[] employeeRoles = new String[MAX_EMPLOYEES];
    static String[] employeeStatuses = new String[MAX_EMPLOYEES];
    static String[] employeeDescriptions = new String[MAX_EMPLOYEES];
    static int employeeCount = 0;
    
    // Transaction data
    static String[][][] transactions = new String[MAX_CUSTOMERS][MAX_TRANSACTIONS][4];
    static int[] transactionCounts = new int[MAX_CUSTOMERS];
    
    // Security tracking
    static Map<String, Integer> failedAttempts = new HashMap<>();
    static Set<String> lockedAccounts = new HashSet<>();
    
    // Admin credentials
    static final String ADMIN_USERNAME = "admin";
    static final String ADMIN_PASSWORD = "admin123";
    
    static Scanner scanner = new Scanner(System.in);

    /*-------------------------------------------------
                     FILE HANDLING METHODS
    -------------------------------------------------*/
    
    // Load all customer data
    static void loadCustomers() {
        Set<String> loadedUsernames = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            customerCount = 0;
            while ((line = reader.readLine()) != null && customerCount < MAX_CUSTOMERS) {
                String[] parts = line.split(",");
                if (parts.length >= 12) {
                    String username = parts[2].trim();
                    if (!loadedUsernames.contains(username)) {
                        customerName[customerCount] = parts[0].trim();
                        customerSurname[customerCount] = parts[1].trim();
                        usernames[customerCount] = username;
                        emails[customerCount] = parts[3].trim();
                        passwords[customerCount] = parts[4].trim();
                        accountTypes[customerCount] = parts[5].trim();
                        accountNumbers[customerCount] = parts[6].trim();
                        cardNumbers[customerCount] = parts[7].trim();
                        expiryDates[customerCount] = parts[8].trim();
                        securityCodes[customerCount] = parts[9].trim();
                        balances[customerCount] = Double.parseDouble(parts[10].trim());
                        branches[customerCount] = parts[11].trim();
                        if (parts.length > 12) {
                            phoneNumbers[customerCount] = parts[12].trim();
                        }
                        loadedUsernames.add(username);
                        customerCount++;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("No existing customer data found or data corrupted.");
        }
    }
    
    // Save new customers (appends only)
    static void saveCustomers() {
        Set<String> existingUsernames = loadExistingUsernames(CUSTOMER_FILE, 2);
        
        try (FileWriter writer = new FileWriter(CUSTOMER_FILE, true)) {
            for (int i = 0; i < customerCount; i++) {
                if (!existingUsernames.contains(usernames[i])) {
                    writer.write(String.join(",",
                        customerName[i], customerSurname[i], usernames[i],
                        emails[i], passwords[i], accountTypes[i], accountNumbers[i],
                        cardNumbers[i], expiryDates[i], securityCodes[i],
                        String.valueOf(balances[i]), branches[i], phoneNumbers[i]
                    ) + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving customer data: " + e.getMessage());
        }
    }
    
    // Load all employee data
    static void loadEmployees() {
        Set<String> loadedUsernames = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_FILE))) {
            String line;
            employeeCount = 0;
            while ((line = reader.readLine()) != null && employeeCount < MAX_EMPLOYEES) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String username = parts[0].trim();
                    if (!loadedUsernames.contains(username)) {
                        employeeUsernames[employeeCount] = username;
                        employeePasswords[employeeCount] = parts[1].trim();
                        employeeBranches[employeeCount] = parts[2].trim();
                        employeeRoles[employeeCount] = parts[3].trim();
                        if (parts.length > 4) employeeStatuses[employeeCount] = parts[4].trim();
                        if (parts.length > 5) employeeDescriptions[employeeCount] = parts[5].trim();
                        loadedUsernames.add(username);
                        employeeCount++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No existing employee data found. Creating default admin...");
            createDefaultAdmin();
        }
    }
    
    // Save new employees (appends only)
    static void saveEmployees() {
        Set<String> existingUsernames = loadExistingUsernames(EMPLOYEE_FILE, 0);
        
        try (FileWriter writer = new FileWriter(EMPLOYEE_FILE, true)) {
            for (int i = 0; i < employeeCount; i++) {
                if (!existingUsernames.contains(employeeUsernames[i])) {
                    writer.write(String.join(",",
                        employeeUsernames[i], employeePasswords[i],
                        employeeBranches[i], employeeRoles[i],
                        employeeStatuses[i] != null ? employeeStatuses[i] : "",
                        employeeDescriptions[i] != null ? employeeDescriptions[i] : ""
                    ) + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving employee data: " + e.getMessage());
        }
    }
    
    // Load transaction history
    static void loadTransactions() {
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    String accNumber = parts[0].trim();
                    // Find customer index by account number
                    for (int i = 0; i < customerCount; i++) {
                        if (accountNumbers[i].equals(accNumber) && transactionCounts[i] < MAX_TRANSACTIONS) {
                            transactions[i][transactionCounts[i]] = new String[]{
                                parts[1].trim(), // date
                                parts[2].trim(), // type
                                parts[3].trim(), // amount
                                parts.length > 4 ? parts[4].trim() : "" // related account
                            };
                            transactionCounts[i]++;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No existing transaction data found.");
        }
    }
    
    // Record a new transaction (appends to file)
    static void recordTransaction(String accNumber, String type, double amount, String relatedAccount) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String record = String.join("|",
            accNumber, timestamp, type, 
            String.format("%.2f", amount),
            relatedAccount != null ? relatedAccount : ""
        );
        
        // Save to file
        try (FileWriter writer = new FileWriter(TRANSACTION_FILE, true)) {
            writer.write(record + "\n");
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
        
        // Update in-memory array
        for (int i = 0; i < customerCount; i++) {
            if (accountNumbers[i].equals(accNumber)) {
                if (transactionCounts[i] < MAX_TRANSACTIONS) {
                    transactions[i][transactionCounts[i]] = new String[]{
                        timestamp, type, 
                        String.format("%.2f", amount),
                        relatedAccount != null ? relatedAccount : ""
                    };
                    transactionCounts[i]++;
                }
                break;
            }
        }
    }
    
    // Load locked accounts
    static void loadLockedAccounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOCKED_ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lockedAccounts.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("No locked accounts found.");
        }
    }
    
    // Save locked accounts
    static void saveLockedAccounts() {
        try (FileWriter writer = new FileWriter(LOCKED_ACCOUNTS_FILE)) {
            for (String username : lockedAccounts) {
                writer.write(username + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving locked accounts: " + e.getMessage());
        }
    }
    
    // Helper method to load existing usernames from file
    private static Set<String> loadExistingUsernames(String filename, int usernameFieldIndex) {
        Set<String> usernames = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > usernameFieldIndex) {
                    usernames.add(parts[usernameFieldIndex].trim());
                }
            }
        } catch (IOException e) {
            // File may not exist yet
        }
        return usernames;
    }
    
    // Create default admin account
    private static void createDefaultAdmin() {
        employeeUsernames[0] = "admin";
        employeePasswords[0] = "admin123";
        employeeBranches[0] = "Headquarters";
        employeeRoles[0] = "Administrator";
        employeeStatuses[0] = "Active";
        employeeDescriptions[0] = "Default system administrator";
        employeeCount = 1;
        saveEmployees();
    }

    /*-------------------------------------------------
                     CUSTOMER METHODS
    -------------------------------------------------*/
    
    // Customer sign up
    static void customerSignUp() {
        loadCustomers();
        
        if (customerCount >= MAX_CUSTOMERS) {
            System.out.println("System cannot accept more users.");
            return;
        }
        
        System.out.println("\n===== CUSTOMER SIGN UP =====");
        
        // Get user details with validation
        String name = getInput("Enter your first name: ", "Name cannot be empty");
        String surname = getInput("Enter your surname: ", "Surname cannot be empty");
        
        String username;
        while (true) {
            username = getInput("Choose a username: ", "Username cannot be empty");
            if (isUsernameTaken(username)) {
                System.out.println("Username already exists. Please choose another.");
            } else {
                break;
            }
        }
        
        String email;
        while (true) {
            email = getInput("Enter your email: ", "Email cannot be empty");
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                System.out.println("Invalid email format. Please try again.");
            } else {
                break;
            }
        }
        
        String phone;
        while (true) {
            phone = getInput("Enter your phone number (10 digits): ", "Phone cannot be empty");
            if (!phone.matches("\\d{10}")) {
                System.out.println("Invalid phone number. Must be 10 digits.");
            } else {
                break;
            }
        }
        
        String accountType;
        while (true) {
            accountType = getInput("Choose account type (Savings/Cheque): ", "Type cannot be empty");
            if (accountType.equalsIgnoreCase("Savings") || accountType.equalsIgnoreCase("Cheque")) {
                break;
            }
            System.out.println("Invalid account type. Please choose Savings or Cheque.");
        }
        
        String password;
        while (true) {
            password = getInput("Create a password (min 8 chars, 1 special character): ", 
                              "Password cannot be empty");
            if (!isValidPassword(password)) {
                System.out.println("Password must be at least 8 characters with 1 special character.");
            } else {
                break;
            }
        }
        
        String branch = getInput("Enter your branch: ", "Branch cannot be empty");
        
        // Generate account details
        String accNumber = "ACC" + (10000 + customerCount);
        String cardNumber = "4111" + String.format("%012d", (long)(Math.random() * 1000000000000L));
        String expiry = String.format("%02d/%02d", (int)(Math.random() * 12) + 1, (int)(Math.random() * 10) + 25);
        String cvv = String.format("%03d", (int)(Math.random() * 1000));
        
        // Store customer data
        customerName[customerCount] = name;
        customerSurname[customerCount] = surname;
        usernames[customerCount] = username;
        emails[customerCount] = email;
        phoneNumbers[customerCount] = phone;
        passwords[customerCount] = password;
        accountTypes[customerCount] = accountType;
        accountNumbers[customerCount] = accNumber;
        cardNumbers[customerCount] = cardNumber;
        expiryDates[customerCount] = expiry;
        securityCodes[customerCount] = cvv;
        balances[customerCount] = 0.0;
        branches[customerCount] = branch;
        customerCount++;
        
        // Save to file
        saveCustomers();
        
        customerMenu(customerCount - 1);
    }
    
    // Customer login
    static void customerLogin() {
        loadCustomers();
        loadLockedAccounts();
        
        System.out.println("\n===== CUSTOMER LOGIN =====");
        String username = getInput("Enter username: ", "Username required");
        
        if (lockedAccounts.contains(username)) {
            System.out.println("Account locked. Please contact support.");
            return;
        }
        
        String password = getInput("Enter password: ", "Password required");
        
        int customerIndex = -1;
        for (int i = 0; i < customerCount; i++) {
            if (usernames[i].equals(username) && passwords[i].equals(password)) {
                customerIndex = i;
                break;
            }
        }
        
        if (customerIndex == -1) {
            System.out.println("Invalid credentials.");
            
            // Track failed attempts
            int attempts = failedAttempts.getOrDefault(username, 0) + 1;
            failedAttempts.put(username, attempts);
            
            if (attempts >= 3) {
                lockedAccounts.add(username);
                saveLockedAccounts();
                System.out.println("Account locked due to multiple failed attempts.");
            } else {
                System.out.println((3 - attempts) + " attempts remaining.");
            }
            return;
        }
        
        // Successful login
        failedAttempts.remove(username);
        System.out.println("Login successful!");
        customerMenu(customerIndex);
    }
    
    // Customer menu
    static void customerMenu(int customerIndex) {
        while (true) {
            System.out.println("\n===== CUSTOMER DASHBOARD =====");
            System.out.println("Welcome, " + customerName[customerIndex] + " " + customerSurname[customerIndex] + "!");
            System.out.println("Account: " + accountNumbers[customerIndex] + 
                             " (" + accountTypes[customerIndex] + ")");
            System.out.println("Balance: R" + String.format("%.2f", balances[customerIndex]));
            System.out.println("Branch: " + branches[customerIndex]);
            System.out.println("\n1. View Account Details");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. View Transaction History");
            System.out.println("6. Logout");
            
            int choice = getIntInput("Choose an option: ", 1, 6);
            
            switch (choice) {
                case 1:
                    showAccountDetails(customerIndex);
                    break;
                case 2:
                    depositMoney(customerIndex);
                    break;
                case 3:
                    withdrawMoney(customerIndex);
                    break;
                case 4:
                    transferMoney(customerIndex);
                    break;
                case 5:
                    viewTransactionHistory(customerIndex);
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
            }
        }
    }
    
    // Show account details
    static void showAccountDetails(int customerIndex) {
        System.out.println("\n===== ACCOUNT DETAILS =====");
        System.out.println("Name: " + customerName[customerIndex] + " " + customerSurname[customerIndex]);
        System.out.println("Username: " + usernames[customerIndex]);
        System.out.println("Email: " + emails[customerIndex]);
        System.out.println("Phone: " + phoneNumbers[customerIndex]);
        System.out.println("Account Type: " + accountTypes[customerIndex]);
        System.out.println("Account Number: " + accountNumbers[customerIndex]);
        System.out.println("Card Number: " + cardNumbers[customerIndex]);
        System.out.println("Expiry Date: " + expiryDates[customerIndex]);
        System.out.println("Security Code: " + securityCodes[customerIndex]);
        System.out.println("Balance: R" + String.format("%.2f", balances[customerIndex]));
        System.out.println("Branch: " + branches[customerIndex]);
    }
    
    // Deposit money
    static void depositMoney(int customerIndex) {
        double amount = getDoubleInput("Enter amount to deposit: R", 0.01, 1000000);
        
        balances[customerIndex] += amount;
        recordTransaction(accountNumbers[customerIndex], "DEPOSIT", amount, null);
        saveCustomers();
        
        System.out.println("Successfully deposited R" + String.format("%.2f", amount));
        System.out.println("New balance: R" + String.format("%.2f", balances[customerIndex]));
    }
    
    // Withdraw money
    static void withdrawMoney(int customerIndex) {
        double amount = getDoubleInput("Enter amount to withdraw: R", 0.01, balances[customerIndex]);
        
        balances[customerIndex] -= amount;
        recordTransaction(accountNumbers[customerIndex], "WITHDRAWAL", amount, null);
        saveCustomers();
        
        System.out.println("Successfully withdrew R" + String.format("%.2f", amount));
        System.out.println("New balance: R" + String.format("%.2f", balances[customerIndex]));
    }
    
    // Transfer money
    static void transferMoney(int senderIndex) {
        String recipientAcc = getInput("Enter recipient account number: ", "Account number required");
        
        // Find recipient
        int recipientIndex = -1;
        for (int i = 0; i < customerCount; i++) {
            if (accountNumbers[i].equals(recipientAcc)) {
                recipientIndex = i;
                break;
            }
        }
        
        if (recipientIndex == -1) {
            System.out.println("Account not found.");
            return;
        }
        
        if (recipientIndex == senderIndex) {
            System.out.println("Cannot transfer to yourself.");
            return;
        }
        
        double amount = getDoubleInput("Enter amount to transfer: R", 0.01, balances[senderIndex]);
        
        // Perform transfer
        balances[senderIndex] -= amount;
        balances[recipientIndex] += amount;
        
        // Record transactions
        recordTransaction(accountNumbers[senderIndex], "TRANSFER_OUT", amount, recipientAcc);
        recordTransaction(accountNumbers[recipientIndex], "TRANSFER_IN", amount, accountNumbers[senderIndex]);
        
        saveCustomers();
        
        System.out.println("Successfully transferred R" + String.format("%.2f", amount) + 
                          " to account " + recipientAcc);
        System.out.println("New balance: R" + String.format("%.2f", balances[senderIndex]));
    }
    
    // View transaction history
    static void viewTransactionHistory(int customerIndex) {
        System.out.println("\n===== TRANSACTION HISTORY =====");
        System.out.printf("%-20s %-15s %-10s %-15s\n", "Date/Time", "Type", "Amount", "Related Account");
        System.out.println("--------------------------------------------------");
        
        for (int i = 0; i < transactionCounts[customerIndex]; i++) {
            String[] t = transactions[customerIndex][i];
            System.out.printf("%-20s %-15s R%-10s %-15s\n", 
                t[0], t[1], t[2], t[3].isEmpty() ? "N/A" : t[3]);
        }
        
        if (transactionCounts[customerIndex] == 0) {
            System.out.println("No transactions found.");
        }
    }

    /*-------------------------------------------------
                     EMPLOYEE METHODS
    -------------------------------------------------*/
    
    // Employee login
    static void employeeLogin() {
        loadEmployees();
        loadLockedAccounts();
        
        System.out.println("\n===== EMPLOYEE LOGIN =====");
        String username = getInput("Enter username: ", "Username required");
        
        if (lockedAccounts.contains(username)) {
            System.out.println("Account locked. Please contact administrator.");
            return;
        }
        
        String password = getInput("Enter password: ", "Password required");
        String branch = getInput("Enter your branch: ", "Branch required");
        
        int employeeIndex = -1;
        for (int i = 0; i < employeeCount; i++) {
            if (employeeUsernames[i].equals(username) && 
                employeePasswords[i].equals(password) &&
                employeeBranches[i].equals(branch)) {
                employeeIndex = i;
                break;
            }
        }
        
        if (employeeIndex == -1) {
            System.out.println("Invalid credentials or branch mismatch.");
            
            // Track failed attempts
            int attempts = failedAttempts.getOrDefault(username, 0) + 1;
            failedAttempts.put(username, attempts);
            
            if (attempts >= 3) {
                lockedAccounts.add(username);
                saveLockedAccounts();
                System.out.println("Account locked due to multiple failed attempts.");
            } else {
                System.out.println((3 - attempts) + " attempts remaining.");
            }
            return;
        }
        
        // Successful login
        failedAttempts.remove(username);
        System.out.println("Login successful!");
        employeeMenu(employeeIndex);
    }
    
    // Employee menu
    static void employeeMenu(int employeeIndex) {
        while (true) {
            System.out.println("\n===== EMPLOYEE DASHBOARD =====");
            System.out.println("Welcome, " + employeeUsernames[employeeIndex] + 
                             " (" + employeeRoles[employeeIndex] + ")");
            System.out.println("Branch: " + employeeBranches[employeeIndex]);
            System.out.println("\n1. Add New Customer");
            System.out.println("2. View Customer Details");
            System.out.println("3. View All Customers");
            System.out.println("4. Reset Customer Password");
            System.out.println("5. Apply Monthly Interest");
            System.out.println("6. Logout");
            
            int choice = getIntInput("Choose an option: ", 1, 6);
            
            switch (choice) {
                case 1:
                    addCustomerByEmployee(employeeBranches[employeeIndex]);
                    break;
                case 2:
                    searchCustomer();
                    break;
                case 3:
                    viewAllCustomers();
                    break;
                case 4:
                    resetCustomerPassword();
                    break;
                case 5:
                    applyInterest();
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
            }
        }
    }
    
    // Add customer by employee
    static void addCustomerByEmployee(String branch) {
        loadCustomers();
        
        if (customerCount >= MAX_CUSTOMERS) {
            System.out.println("System cannot accept more customers.");
            return;
        }
        
        System.out.println("\n===== ADD NEW CUSTOMER =====");
        
        // Get customer details
        String name = getInput("Enter customer's first name: ", "Name required");
        String surname = getInput("Enter customer's surname: ", "Surname required");
        
        String username;
        while (true) {
            username = getInput("Choose a username: ", "Username required");
            if (isUsernameTaken(username)) {
                System.out.println("Username already exists.");
            } else {
                break;
            }
        }
        
        String email;
        while (true) {
            email = getInput("Enter email: ", "Email required");
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                System.out.println("Invalid email format.");
            } else {
                break;
            }
        }
        
        String phone;
        while (true) {
            phone = getInput("Enter phone number (10 digits): ", "Phone required");
            if (!phone.matches("\\d{10}")) {
                System.out.println("Must be 10 digits.");
            } else {
                break;
            }
        }
        
        String accountType;
        while (true) {
            accountType = getInput("Account type (Savings/Cheque): ", "Type required");
            if (accountType.equalsIgnoreCase("Savings") || accountType.equalsIgnoreCase("Cheque")) {
                break;
            }
            System.out.println("Invalid type. Choose Savings or Cheque.");
        }
        
        String password;
        while (true) {
            password = getInput("Set password (min 8 chars, 1 special): ", "Password required");
            if (!isValidPassword(password)) {
                System.out.println("Password requirements not met.");
            } else {
                break;
            }
        }
        
        // Generate account details
        String accNumber = "ACC" + (10000 + customerCount);
        String cardNumber = "4111" + String.format("%012d", (long)(Math.random() * 1000000000000L));
        String expiry = String.format("%02d/%02d", (int)(Math.random() * 12) + 1, (int)(Math.random() * 10) + 25);
        String cvv = String.format("%03d", (int)(Math.random() * 1000));
        
        // Store customer data
        customerName[customerCount] = name;
        customerSurname[customerCount] = surname;
        usernames[customerCount] = username;
        emails[customerCount] = email;
        phoneNumbers[customerCount] = phone;
        passwords[customerCount] = password;
        accountTypes[customerCount] = accountType;
        accountNumbers[customerCount] = accNumber;
        cardNumbers[customerCount] = cardNumber;
        expiryDates[customerCount] = expiry;
        securityCodes[customerCount] = cvv;
        balances[customerCount] = 0.0;
        branches[customerCount] = branch;
        customerCount++;
        
        // Save to file
        saveCustomers();
    }
    
    // View all customers
    static void viewAllCustomers() {
        loadCustomers();
        
        System.out.println("\n===== ALL CUSTOMERS =====");
        System.out.printf("%-5s %-20s %-15s %-10s %-15s %-10s\n", 
                         "No.", "Name", "Account", "Type", "Balance", "Branch");
        System.out.println("------------------------------------------------------------");
        
        for (int i = 0; i < customerCount; i++) {
            System.out.printf("%-5d %-20s %-15s %-10s R%-15.2f %-10s\n",
                i + 1,
                customerName[i] + " " + customerSurname[i],
                accountNumbers[i],
                accountTypes[i],
                balances[i],
                branches[i]);
        }
        
        if (customerCount == 0) {
            System.out.println("No customers found.");
        }
    }
    
    // Search for customer
    static void searchCustomer() {
        loadCustomers();
        
        String searchTerm = getInput("Enter account number or username: ", "Search term required");
        
        for (int i = 0; i < customerCount; i++) {
            if (accountNumbers[i].equals(searchTerm) || usernames[i].equals(searchTerm)) {
                showAccountDetails(i);
                return;
            }
        }
        
        System.out.println("Customer not found.");
    }
    
    // Reset customer password
    static void resetCustomerPassword() {
        loadCustomers();
        
        String username = getInput("Enter customer username: ", "Username required");
        
        int customerIndex = -1;
        for (int i = 0; i < customerCount; i++) {
            if (usernames[i].equals(username)) {
                customerIndex = i;
                break;
            }
        }
        
        if (customerIndex == -1) {
            System.out.println("Customer not found.");
            return;
        }
        
        String newPassword;
        while (true) {
            newPassword = getInput("Enter new password (min 8 chars, 1 special): ", "Password required");
            if (!isValidPassword(newPassword)) {
                System.out.println("Password requirements not met.");
            } else {
                break;
            }
        }
        
        passwords[customerIndex] = newPassword;
        saveCustomers();
        System.out.println("Password reset successfully for " + username);
    }
    
    // Apply monthly interest
    static void applyInterest() {
        loadCustomers();
        
        System.out.println("\nApplying monthly interest to savings accounts...");
        int count = 0;
        
        for (int i = 0; i < customerCount; i++) {
            if (accountTypes[i].equalsIgnoreCase("Savings") && balances[i] > 0) {
                double interest = balances[i] * (SAVINGS_INTEREST_RATE / 12);
                balances[i] += interest;
                recordTransaction(accountNumbers[i], "INTEREST", interest, null);
                count++;
            }
        }
        
        saveCustomers();
        System.out.println("Applied interest to " + count + " savings accounts.");
    }

    /*-------------------------------------------------
                     ADMIN METHODS
    -------------------------------------------------*/
    
    // Admin login
    static void adminLogin() {
        System.out.println("\n===== ADMIN LOGIN =====");
        String username = getInput("Username: ", "Username required");
        String password = getInput("Password: ", "Password required");
        
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            adminMenu();
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }
    
    // Admin menu
    static void adminMenu() {
        while (true) {
            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Reset Employee Password");
            System.out.println("5. Unlock Accounts");
            System.out.println("6. View All Customers");
            System.out.println("7. Apply Monthly Interest");
            System.out.println("8. Logout");
            
            int choice = getIntInput("Choose an option: ", 1, 8);
            
            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewAllEmployees();
                    break;
                case 3:
                    searchEmployee();
                    break;
                case 4:
                    resetEmployeePassword();
                    break;
                case 5:
                    unlockAccounts();
                    break;
                case 6:
                    viewAllCustomers();
                    break;
                case 7:
                    applyInterest();
                    break;
                case 8:
                    System.out.println("Logging out...");
                    return;
            }
        }
    }
    
    // Add employee
    static void addEmployee() {
        loadEmployees();
        
        if (employeeCount >= MAX_EMPLOYEES) {
            System.out.println("Maximum employees reached.");
            return;
        }
        
        System.out.println("\n===== ADD NEW EMPLOYEE =====");
        
        String username;
        while (true) {
            username = getInput("Enter username: ", "Username required");
            if (isEmployeeUsernameTaken(username)) {
                System.out.println("Username already exists.");
            } else {
                break;
            }
        }
        
        String password;
        while (true) {
            password = getInput("Enter password (min 8 chars, 1 special): ", "Password required");
            if (!isValidPassword(password)) {
                System.out.println("Password requirements not met.");
            } else {
                break;
            }
        }
        
        String branch = getInput("Enter branch: ", "Branch required");
        String role = getInput("Enter role: ", "Role required");
        String status = getInput("Enter status (Active/Inactive): ", "Status required");
        String description = getInput("Enter description: ", "Description required");
        
        // Store employee data
        employeeUsernames[employeeCount] = username;
        employeePasswords[employeeCount] = password;
        employeeBranches[employeeCount] = branch;
        employeeRoles[employeeCount] = role;
        employeeStatuses[employeeCount] = status;
        employeeDescriptions[employeeCount] = description;
        employeeCount++;
        
        // Save to file
        saveEmployees();
        
        System.out.println("Employee added successfully!");
    }
    
    // View all employees
    static void viewAllEmployees() {
        loadEmployees();
        
        System.out.println("\n===== ALL EMPLOYEES =====");
        System.out.printf("%-5s %-15s %-15s %-15s %-10s\n", 
                         "No.", "Username", "Branch", "Role", "Status");
        System.out.println("------------------------------------------------");
        
        for (int i = 0; i < employeeCount; i++) {
            System.out.printf("%-5d %-15s %-15s %-15s %-10s\n",
                i + 1,
                employeeUsernames[i],
                employeeBranches[i],
                employeeRoles[i],
                employeeStatuses[i]);
        }
        
        if (employeeCount == 0) {
            System.out.println("No employees found.");
        }
    }
    
    // Search employee
    static void searchEmployee() {
        loadEmployees();
        
        String searchTerm = getInput("Enter username: ", "Search term required");
        
        for (int i = 0; i < employeeCount; i++) {
            if (employeeUsernames[i].equals(searchTerm)) {
                System.out.println("\n===== EMPLOYEE DETAILS =====");
                System.out.println("Username: " + employeeUsernames[i]);
                System.out.println("Branch: " + employeeBranches[i]);
                System.out.println("Role: " + employeeRoles[i]);
                System.out.println("Status: " + employeeStatuses[i]);
                System.out.println("Description: " + employeeDescriptions[i]);
                return;
            }
        }
        
        System.out.println("Employee not found.");
    }
    
    // Reset employee password
    static void resetEmployeePassword() {
        loadEmployees();
        
        String username = getInput("Enter employee username: ", "Username required");
        
        int employeeIndex = -1;
        for (int i = 0; i < employeeCount; i++) {
            if (employeeUsernames[i].equals(username)) {
                employeeIndex = i;
                break;
            }
        }
        
        if (employeeIndex == -1) {
            System.out.println("Employee not found.");
            return;
        }
        
        String newPassword;
        while (true) {
            newPassword = getInput("Enter new password (min 8 chars, 1 special): ", "Password required");
            if (!isValidPassword(newPassword)) {
                System.out.println("Password requirements not met.");
            } else {
                break;
            }
        }
        
        employeePasswords[employeeIndex] = newPassword;
        saveEmployees();
        System.out.println("Password reset successfully for " + username);
    }
    
    // Unlock accounts
    static void unlockAccounts() {
        loadLockedAccounts();
        
        if (lockedAccounts.isEmpty()) {
            System.out.println("No accounts are currently locked.");
            return;
        }
        
        System.out.println("\n===== LOCKED ACCOUNTS =====");
        int i = 1;
        for (String username : lockedAccounts) {
            System.out.println(i++ + ". " + username);
        }
        
        String input = getInput("\nEnter username to unlock or 'all': ", "Input required");
        
        if (input.equalsIgnoreCase("all")) {
            lockedAccounts.clear();
            System.out.println("All accounts unlocked.");
        } else if (lockedAccounts.contains(input)) {
            lockedAccounts.remove(input);
            System.out.println("Account " + input + " unlocked.");
        } else {
            System.out.println("Account not found in locked list.");
        }
        
        saveLockedAccounts();
    }

    /*-------------------------------------------------
                     UTILITY METHODS
    -------------------------------------------------*/
    
    // Check if username is taken
    static boolean isUsernameTaken(String username) {
        for (int i = 0; i < customerCount; i++) {
            if (usernames[i].equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
    
    // Check if employee username is taken
    static boolean isEmployeeUsernameTaken(String username) {
        for (int i = 0; i < employeeCount; i++) {
            if (employeeUsernames[i].equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
    
    // Validate password
    static boolean isValidPassword(String password) {
        return password.length() >= 8 && 
               password.matches(".*[!@#$%^&*].*");
    }
    
    // Get validated string input
    static String getInput(String prompt, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(errorMsg);
        }
    }
    
    // Get validated integer input
    static int getIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.println("Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    // Get validated double input
    static double getDoubleInput(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double input = Double.parseDouble(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.printf("Please enter an amount between R%.2f and R%.2f\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /*-------------------------------------------------
                     MAIN MENU SYSTEM
    -------------------------------------------------*/
    
    // Main menu
    static void mainMenu() {
        while (true) {
            System.out.println("\n===== DBT BANKING SYSTEM =====");
            System.out.println("1. Customer Sign Up");
            System.out.println("2. Customer Login");
            System.out.println("3. Employee Login");
            System.out.println("4. Admin Login");
            System.out.println("5. Exit");
            
            int choice = getIntInput("Choose an option: ", 1, 5);
            
            switch (choice) {
                case 1:
                    customerSignUp();
                    break;
                case 2:
                    customerLogin();
                    break;
                case 3:
                    employeeLogin();
                    break;
                case 4:
                    adminLogin();
                    break;
                case 5:
                    System.out.println("Thank you for using our banking system. Goodbye!");
                    System.exit(0);
            }
        }
    }
    
    // Initialize system
    public static void main(String[] args) {
        // Load all data at startup
        loadCustomers();
        loadEmployees();
        loadTransactions();
        loadLockedAccounts();
        
        // Start the system
        mainMenu();
    }
}