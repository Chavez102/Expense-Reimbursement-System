CREATE TABLE Users (
  UserName varchar(20),
  passsword varchar(20)
);
 

INSERT INTO Users (UserName,passsword)
VALUES('username1','password1');

SELECT * FROM Users;

-- DROP TABLE Users

CREATE TABLE Tickets (
	TicketId SERIAL PRIMARY KEY,
	Status varchar(20),
	Amount float,
  	Description varchar(100)
);

INSERT INTO Tickets (Status, Amount, Description)
VALUES('status1',21.9, 'description');

INSERT INTO Tickets (Status, Amount, Description)
VALUES('status2',22, 'description2');

SELECT * FROM Tickets;

DROP TABLE Tickets