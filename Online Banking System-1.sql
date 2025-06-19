---Create New Database
CREATE DATABASE Banking_System;

---Use the database
USE Banking_System;

---Create table customers
CREATE TABLE Customers(
Customer_ID INT PRIMARY KEY,
Name VARCHAR(100) NOT NULL,
Surname VARCHAR(100) NOT NULL,
Email VARCHAR(100) UNIQUE,
Phone VARCHAR(20) NOT NULL,
Address VARCHAR(150) NOT NULL
);
---Populate the table with appropriate entities and attributes
INSERT INTO customers(Customer_ID,Name,Surname,Email,Phone,Address)
VALUEs('C1','James','Anderson','James.anderson@gmail.com','0712345678','12 Elm street,yeovile'),
('C2','Samuel','Mkhize','SamuelMkhi@gmail.com','0723456789','23 Moi Avenue'),
('C3','Michael','Smith','MichaelM@gmail.com','073334456','56 Tom Mboya Street'),
('C4','Jason','Smith','JasonJason@gmail.com','0858734908','34 Troye street'),
('C5','David','Nkomo','DavidDiva@gmail.com','0987896543','534 braamfontein complex'),
('C6','Lawrence','Gopane','lawlaw@gmail.com','0878976543','13 grant avenue'),
('C7','Nicholas','Thabethe','NicholasThabethe@gmail.com','0678905628','237 medrine avenue'),
('C8','Chris','Mapetla','ChrisM@gmail.com','07890679876','154 street,soweto'),
('C9','Daniel','Jones','DanielDJones@gmail.com','0987896578','57 park road,midrand'),
('C10','George','Sampsom','GeorgeGman@gmail.com','0987890989','15 parklane'),
('C11','Justin','De klerk','JustinDe_klerk@gmail.com','0768965430','28 arthur rd'),
('C12','patrick','mofokeng','patpatrick2gmail.com','0987654329','200 paterson street'),
('C13','marcus','mamba','marcusmamaba@gmail.com','0676897665','74 corner eloff and vvon brandis street'),
('C14','Primrose','Baloyi','primBaloyi@gmail.com','0876745890','90 student street, lenasia'),
('C15','lesedi','Mohale','leeleelesedi@gmail.com','0987843211','654 thokoza street'),
('C16','Thandeka','Mangena','Thandeka@gmail.com','0957755833','27 Joe slovo street'),
('C17','Lindiwe','Moyo','LindiweMoyo@gmail.com','0872367232','885 davis avenue'),
('C18','Sipho','Yeni','SiphoY@gmail.com','0679234722','908 Midrand flat'),
('C19','Melusi','Duma','MelusiDuma@gmail.com','0789237486','81 shipton road'),
('C20','Thembi','Majola','ThembiMajola@gmail.com','0734673873','404 room, Thembisa');

---Create table Accounts
CREATE TABLE Accounts(
Account_ID INT PRIMARY KEY,
Customer_ID INT,
Account_Type VARCHAR(50) NOT NULL,
Card_Number INT NOT NULL,
Expiry_Date DATE NOT NULL,
Security_Code INT NOT NULL,
Balance DECIMAL(10,2) NOT NULL,
FOREIGN KEY (Customer_ID) REFERENCES customers(Customer_ID)
);
---Populate the table with appropriate attributes and entities
INSERT INTO accounts(Account_ID,Customer_ID, Account_Type, Card_Number,Expiry_Date,Security_Code,Balance)
VALUES ('A01','C1','Savings','9234785672354542','78/36','8733',15000.00),
('A02','C2','Cheque','9473646464763425','08/43','2622',3500.00),
('A03','C3','Savings','7234623682988372','73/71','5123',150.00),
('A04','C4','Savings','7853467576487657','98/90','5765',2500.50),
('A05','C5','Savings','8975463455434557','09/87','4656',00.00),
('A06','C6','Cheque','8745678476578568','98/08','8880',289.00),
('A07','C7','Savings','9877465353675559','97/56','5767',27000.00),
('A08','C8','Savings','4325656465555555','54/64','5464',750.00),
('A09','C9','Savings','545665465545767','44/55','1323',350.00),
('A010','C10','Savings','2157712363287878','61/25','1621',50.00),
('A011','C11','Savings','6125816783126268','21/65','1879',450.25),
('A012','C12','Savings','2378627868238625','12/67','0188',4500),
('A013','C13','Savings','6176682637826223','09/02','7829',550.45),
('A014','C14','Cheque','7389787348943896','33/44','7813',6522.00),
('A015','C15','Savings','761255756712357867','67/66','7612',2300.90),
('A016','C16','Savings','7523757237823767','27/82','1771',9000.00),
('A017','C17','Savings','6712817648789317','76/67','7812',4700.00),
('A018','C18','Cheque','1781236832663243','23/32','3443',00.00),
('A019','C19','Cheque','2847629729789723','27/22','2378',6123),
('A020','C20','Cheque','8957768346234234','89/47','2782',00.00);

---Create table transaction records
CREATE TABLE Transaction_Records(
Transaction_ID INT PRIMARY KEY,
Account_ID INT,
Transaction_Type VARCHAR(20) NOT NULL,
Amount DECIMAL(10,2) NOT NULL,
Transaction_Date DATE NOT NULL,
FOREIGN KEY (Account_ID) REFERENCES Accounts(Account_ID)
);
---Populate table with appropriate entities and attributes
INSERT INTO transaction_records(Transaction_ID, Account_ID,Transaction_Type,Amount,Transaction_Date)
VALUES ('T001','A01','Transfer',500.00,'2025-06-01'),
('T002','A02','Deposit',500.00,'2025-06-02'),
('T003','A03','Withdraw',150.00,'2025-06-03'),
('T004','A04','Withdraw',500.00,'2025-06-04'),
('T005','A05','Deposit',1000.00,'2025-06-05'),
('T006','A06','Deposit',3500.00,'2025-06-06'),
('T007','A07','Withdraw',2000.00,'2025-06-07'),
('T008','A08','Transfer',700.00,'2025-06-08'),
('T009','A09','Deposit',350.00,'2025-06-09'),
('T010','A10','Deposit',1500.00,'2025-06-10'),
('T011','A11','Deposit',4500.00,'2025-06-11'),
('T012','A12','Withdraw',500.00,'2025-06-11'),
('T013','A13','Transfer',300.00,'2025-06-01'),
('T014','A14','Transfer',4500.00,'2025-06-02'),
('T015','A15','Withdraw',2000.00,'2025-06-03'),
('T016','A16','Transfer',3000.00,'2025-06-04'),
('T017','A17','Transfer',700.00,'2025-06-05'),
('T018','A18','Deposit',100.00,'2025-06-06'),
('T019','A19','Withdraw',600.00,'2025-06-07'),
('T020','A20','Deposit',200.00,'2025-06-08');

---Create table branch_information
CREATE TABLE Branch_Information(
Branch_ID INT PRIMARY KEY,
Branch_Name VARCHAR(100) NOT NULL,
Branch_Location VARCHAR(100) NOT NULL,
Contact_Number VARCHAR(20) NOT NULL
);
---Populate table with appropriate entities and attributes
INSERT INTO branch_information(Branch_ID,Branch_Name, Branch_Location,Contact_Number)
VALUES ('B101','DBT Bank','Johannesburg Station east','0115509237'),
('B102','DBT Bank','Johannesburg Braamfontein','0115508793'),
('B103','DBT Bank','Johannesburg Pritchard','0115508743'),
('B104','DBT Bank','Johannesburg Bree','0115501237'),
('B105','DBT Bank','Johannesburg Jewel City','0115503467'),
('B106','DBT Bank','Johannesburg carlton centre','0115507622'),
('B107','DBT Bank','Johannesburg kerk','0115503723'),
('B108','DBT Bank','Johannesburg Station west','0115502623'),
('B109','DBT Bank','Johannesburg plain street','0115509238'),
('B110','DBT Bank','Johannesburg Hillbrow','0115502738');

---Create table employees
CREATE TABLE Employees(
Employee_ID INT PRIMARY KEY,
Name VARCHAR(100) NOT NULL,
Position VARCHAR(50) NOT NULL,
Status VARCHAR(50) NOT NULL,
Description VARCHAR(100) NOT NULL,
Schedule_Date DATE NOT NULL,
Branch_ID INT,
FOREIGN KEY (branch_id) REFERENCES branches(branch_id)
);
---Populate table with appropriate entities and attributes
INSERT INTO employees(Employee_ID,Branch_ID,Name,Position,Status, Description,Schedule_Date) 
VALUES ('E1001','B101','Sibusiso','Bank teller','Active','handles customer transactions','8:00'),
('E1002','B102','Michael','Auditor','on leave','Conduct internal audits','10:30'),
('E1003','B103','Lindiwe','Personal banker','Active','personalized banking services','7:00'),
('E1004','B104','Mark','investment banker','Terminated','advice clients on financial transactions','7:00'),
('E1005','B105','Anna','Credit analyst','inactive','analyze loan applicants','12:00'),
('E1006','B106','Siyabonga','bank teller','Inactive','handles customer transactions','10:00'),
('E1007','B107','Themba','Auditor','active','conduct internal aduits','6:30'),
('E1008','B108','Thembi','personal banker','inactive','deals with personal loans','7:30'),
('E1009','B109','Melusi','investment banker','on leave','advice customers on transfer of money','7:00'),
('E1010','B110','Sthabile','credit analyst','active','analyze loan applications','8:00'),
('E1011','B105','Thuli','bank teller','on leave','handles customer transactions','8:00'),
('E1012','B104','mandla','auditor','inactive','conduct internal audits','7:30'),
('E1013','B109','nick','personal banker','active','deals personal loan','7:00'),
('E1014','B102','tebogo','investment banker','active','advice clients on safe transactions','8:00'),
('E1015','B106','Jackson','credit analyst','inactive','anaylze loan request','7:30'),
('E1016','B101','sifiso','bank teller','inactive','handles customer transactions','06:30'),
('E1017','B110','oratile','auditor','on leave','deals with internal audits','14:00'),
('E1018','B103','tshamio','personal banker','active','deals with personal loan','9:00'),
('E1019','B108','Sam','investment banker','inactive','advice customers on safe transactions','11:00'),
('E1020','B107','Paul','credit analyst','terminated','analyze laon request','10:00');



-------------SQL QUERIES--------------------

--Get all customers' full names and emails
SELECT Name, Surname, Email FROM Customers;

--Retrieve all accounts with a balance greater than R1000
SELECT * FROM Accounts WHERE Balance > 1000;

--Get transactions of type 'Withdraw'
SELECT * FROM Transaction_Records WHERE Transaction_Type = 'Withdraw';

--List all branches in Johannesburg Braamfontein
SELECT * FROM Branch_Information WHERE Branch_Location = 'Johannesburg Braamfontein';

--Count how many customers there are
SELECT COUNT(*) AS Total_Customers FROM Customers;

--Calculate the total money in all accounts
SELECT SUM(Balance) AS Total_Funds FROM Accounts;

--Get the average transaction amount
SELECT AVG(Amount) AS Average_Transaction FROM Transaction_Records;

--Find the minimum account balance
SELECT MIN(Balance) AS Min_Balance FROM Accounts;

--Count how many employees are 'on leave'
SELECT COUNT(*) AS OnLeave_Employees FROM Employees WHERE Status = 'on leave';

--List customer names with their account types and balances
SELECT c.Name, c.Surname, a.Account_Type, a.Balance
FROM Customers c
JOIN Accounts a ON c.Customer_ID = a.Customer_ID;

--Show transaction details along with account type
SELECT t.Transaction_ID, t.Transaction_Type, t.Amount, a.Account_Type
FROM Transaction_Records t
JOIN Accounts a ON t.Account_ID = a.Account_ID;

--Get employee names with their branch locations
SELECT e.Name, e.Position, b.Branch_Location
FROM Employees e
JOIN Branch_Information b ON e.Branch_ID = b.Branch_ID;

--List administration staff and their email addresses
SELECT Name, Surname, Email FROM Adminstration;

--Get each branch and the number of employees assigned there
SELECT b.Branch_Name, COUNT(e.Employee_ID) AS Num_Employees
FROM Branch_Information b
JOIN Employees e ON b.Branch_ID = e.Branch_ID
GROUP BY b.Branch_Name;

--Update the balance of a specific account (e.g., A01) by adding R1000
UPDATE Accounts SET Balance = Balance + 1000 WHERE Account_ID = 'A01';

--Change the status of employee E1004 to 'inactive'
UPDATE Employees SET Status = 'inactive' WHERE Employee_ID = 'E1004';

--Delete customers with no email address (if any)
DELETE FROM Customers WHERE Email IS NULL;

--Fix typo in email of admin Mandla Moyo
UPDATE Adminstration
SET Email = 'MandlaMoyo@gmail.com'
WHERE Name = 'Mandla' AND Surname = 'Moyo';

--Add R500 interest to all savings accounts
UPDATE Accounts
SET Balance = Balance + 500
WHERE Account_Type = 'Savings';

