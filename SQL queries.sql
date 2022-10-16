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

CREATE TABLE Tickets (
	TicketId SERIAL PRIMARY KEY,
	EmployeeUserName varchar(20),
	Status varchar(20),
	Amount float,
  	Description varchar(100),
	FOREIGN KEY (EmployeeUserName) REFERENCES Employees(UserName)
);


INSERT INTO Users (UserName,passsword,role)
VALUES('bryan','password2','employee');

INSERT INTO Employees (UserName)
VALUES('bryan');



INSERT INTO Tickets (Status, Amount, Description,employeeusername)
VALUES('pending',21.9, 'description','bryan');

INSERT INTO Tickets (Status, Amount, Description,employeeusername)
VALUES('pending','21.9', 'description','bryan');

--Adding manager


INSERT INTO Users (UserName,passsword,role)
VALUES('Kevin','password2','manager');

INSERT INTO Managers (UserName)
VALUES('Kevin');



SELECT * FROM Employees WHERE username = 'kim';


SELECT * FROM Users; 
SELECT * FROM Managers;
SELECT * FROM Employees;

SELECT * FROM Tickets;




DROP TABLE Tickets;
DROP TABLE Managers;
DROP TABLE employees;
DROP TABLE Users;