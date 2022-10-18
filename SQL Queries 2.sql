CREATE TABLE Users (
  	UserName varchar(20) UNIQUE,
  	passsword varchar(20),
	role varchar(20),
	PRIMARY KEY (UserName)
	
);
  
CREATE TABLE Employees( 
	UserName varchar(20),
	PRIMARY KEY(UserName), 
	FOREIGN KEY(UserName) REFERENCES Users(UserName)
);

CREATE TABLE Managers( 
	UserName varchar(20),
	PRIMARY KEY(UserName), 
	FOREIGN KEY(UserName) REFERENCES Users(UserName)
);

CREATE TABLE pendingtickets  (
	TicketId SERIAL PRIMARY KEY,
	EmployeeUserName varchar(20),
	Status varchar(20),
	Amount float,
  	Description varchar(100),
	FOREIGN KEY (EmployeeUserName) REFERENCES Employees(UserName)
);

CREATE TABLE processedtickets (
	TicketId NUMERIC,
	EmployeeUserName varchar(20),
	Status varchar(20),
	Amount float,
  	Description varchar(100)
);

--Adding Employees

INSERT INTO Users (UserName,passsword,role)
VALUES('bryan','password2','employee');

INSERT INTO Employees (UserName)
VALUES('bryan');


INSERT INTO Users (UserName,passsword,role)
VALUES('karina','password2','employee');

INSERT INTO Employees (UserName)
VALUES('karina');

--Addding Tickets to PendingTickets 

INSERT INTO pendingtickets (Status, Amount, Description,employeeusername)
VALUES('pending',21.9, 'travel','bryan');

INSERT INTO pendingtickets (Status, Amount, Description,employeeusername)
VALUES('pending','21.9', 'food','bryan');

INSERT INTO pendingtickets (Status, Amount, Description,employeeusername)
VALUES('pending',200, 'hotel','karina');


--Adding Tickets to ProcessedTickets
DELETE FROM pendingtickets WHERE ticketid = 1 ;

INSERT INTO processedtickets  (TicketId,Status, Amount, Description,employeeusername)
VALUES(1,'approved','21.9', 'food','bryan');


DELETE FROM pendingtickets WHERE ticketid = 3 ;

INSERT INTO processedtickets  (TicketId,Status, Amount, Description,employeeusername)
VALUES(3,'approved','200', 'food','karina');




--Adding manager

INSERT INTO Users (UserName,passsword,role)
VALUES('Kevin','password2','manager');

INSERT INTO Managers (UserName)
VALUES('Kevin');



SELECT * FROM processedtickets  WHERE employeeusername = 'karina';


SELECT * FROM Users; 
SELECT * FROM Managers;
SELECT * FROM Employees;

SELECT * FROM pendingtickets ;
SELECT * FROM processedtickets ;




DROP TABLE pendingtickets;
DROP TABLE processedtickets;
DROP TABLE Managers;
DROP TABLE employees;
DROP TABLE Users;