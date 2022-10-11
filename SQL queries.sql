CREATE TABLE Users (
  	UserName varchar(20),
  	passsword varchar(20),
	role varchar(20),
	PRIMARY KEY (UserName)
	
);
 
 
CREATE TABLE Employees( 
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
VALUES('bryan','password2','employee2');

INSERT INTO Employees (UserName)
VALUES('username1');


INSERT INTO Tickets (Status, Amount, Description,employeeusername)
VALUES('status1',21.9, 'description','username1');



SELECT * FROM Users;
SELECT * FROM Employees;
SELECT * FROM Tickets;

DROP TABLE Tickets;
DROP TABLE employees;
DROP TABLE Users;