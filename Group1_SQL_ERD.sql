DROP DATABASE IF EXISTS HiddenGemsDB;

CREATE DATABASE HiddenGemsDB;
USE HiddenGemsDB;

CREATE TABLE Points_Tier (

	Tier_ID INT AUTO_INCREMENT,
	Tier_Name VARCHAR(50) NOT NULL,
	Min_Points INT NOT NULL,
	Max_Points INT NOT NULL,
        
	CONSTRAINT Points_Tier_pk PRIMARY KEY (Tier_ID)
    
);


CREATE TABLE User (
		
	User_ID INT AUTO_INCREMENT,
	First_Name VARCHAR(50) NOT NULL,
	Last_Name VARCHAR(50) NOT NULL,
	Nationality VARCHAR(50),
	Points INT DEFAULT 0,
	Tier_ID INT,
	Password VARCHAR(255) NOT NULL,
    Is_Admin BOOLEAN DEFAULT FALSE,
        
	CONSTRAINT User_pk PRIMARY KEY (User_ID),
	CONSTRAINT User_Tier_fk FOREIGN KEY (Tier_ID) REFERENCES Points_Tier(Tier_ID)
        ON DELETE SET NULL
        ON UPDATE CASCADE
    
);


CREATE TABLE User_Phone (

	Phone_ID INT AUTO_INCREMENT,
	User_ID INT NOT NULL,
	Phone_Number VARCHAR(20) NOT NULL,
        
	CONSTRAINT User_Phone_pk PRIMARY KEY (Phone_ID),
	CONSTRAINT User_Phone_User_fk FOREIGN KEY (User_ID) REFERENCES User(User_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
    
);


CREATE TABLE User_Email (

	Email_ID INT AUTO_INCREMENT,
	User_ID INT NOT NULL,
	Email VARCHAR(100) NOT NULL,
        
	CONSTRAINT User_Email_pk PRIMARY KEY (Email_ID),
	CONSTRAINT User_Email_User_fk FOREIGN KEY (User_ID) REFERENCES User(User_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
        
);


CREATE TABLE Country (

	country_id INT AUTO_INCREMENT PRIMARY KEY,
	country_name VARCHAR(50) NOT NULL
        
);


CREATE TABLE Region (

	region_id INT AUTO_INCREMENT PRIMARY KEY,
	region_name VARCHAR(50) NOT NULL,
	country_id INT NOT NULL,
        
	FOREIGN KEY (country_id) REFERENCES Country(country_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
        
);


CREATE TABLE City (

	city_id INT AUTO_INCREMENT PRIMARY KEY,
	city_name VARCHAR(50) NOT NULL,
	region_id INT NOT NULL,
        
	FOREIGN KEY (region_id) REFERENCES Region(region_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
        
);


CREATE TABLE Travel_Spot (

	location_id INT AUTO_INCREMENT PRIMARY KEY,
	user_id INT NOT NULL,
	spotname VARCHAR(100) NOT NULL,
	date_shared DATE NOT NULL,
	city_id INT NOT NULL,
    base_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    max_capacity INT NOT NULL DEFAULT 0,
    availability ENUM('Available', 'Unavailable') DEFAULT 'Available',
        
	FOREIGN KEY (city_id) REFERENCES City(city_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
	FOREIGN KEY (user_id) REFERENCES User(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
        
);


CREATE TABLE Category (

	category_id INT AUTO_INCREMENT PRIMARY KEY,
	category_name VARCHAR(100) NOT NULL
        
);


CREATE TABLE TS_Category (

	location_id INT NOT NULL,
	category_id INT NOT NULL,
        
	PRIMARY KEY (location_id, category_id),
	FOREIGN KEY (location_id) REFERENCES Travel_Spot(location_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
	FOREIGN KEY (category_id) REFERENCES Category(category_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
        
);


CREATE TABLE Reaction(

	ReactionType_ID 	INT AUTO_INCREMENT NOT NULL,
	Reaction_Name		VARCHAR(50) NOT NULL,
    
    CONSTRAINT Reaction_pk PRIMARY KEY (ReactionType_ID)
        
);


CREATE TABLE User_Feedback(

	Review_ID 			INT AUTO_INCREMENT NOT NULL,
    User_ID 			INT NOT NULL,
    Location_ID 		INT NOT NULL,
    Rating 		        DECIMAL(2, 1) CHECK(Rating BETWEEN 1 AND 5),
    is_recommendation   BOOLEAN DEFAULT FALSE,
    Reaction_Count 		INT DEFAULT 0,
    Comment_Count 		INT DEFAULT 0,
    Review_Date 		DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT User_Feedback_pk PRIMARY KEY (Review_ID),
    CONSTRAINT User_Feedback_UserID_fk FOREIGN KEY (User_ID) REFERENCES User (User_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT User_Feedback_LocationID_fk FOREIGN KEY (Location_ID) REFERENCES Travel_Spot (Location_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE

);


CREATE TABLE User_Reaction(

    Reaction_ID 		INT AUTO_INCREMENT NOT NULL,
    Review_ID 			INT NOT NULL,
    User_ID 			INT NOT NULL,
    ReactionType_ID		INT NOT NULL,
    Reaction_Date 		DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT User_Reaction_pk PRIMARY KEY (Reaction_ID),
    CONSTRAINT User_Reaction_ReviewID_fk FOREIGN KEY (Review_ID) REFERENCES User_Feedback (Review_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT User_Reaction_UserID_fk FOREIGN KEY (User_ID) REFERENCES User (User_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT User_Reaction_ReactionTypeID_fk FOREIGN KEY (ReactionType_ID) REFERENCES Reaction (ReactionType_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT Unique_User_Reaction UNIQUE (Reaction_ID, Review_ID, User_ID)
                
);


CREATE TABLE Booking(
    Booking_ID INT AUTO_INCREMENT PRIMARY KEY,
    Location_ID INT NOT NULL,
    Status VARCHAR(50) NOT NULL,
    Start_date DATE NOT NULL,
    End_date DATE NOT NULL,
    Booking_Dates VARCHAR(100) AS (CONCAT(Start_date, ' to ', End_date)) STORED,
    Tax DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    Gem_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    Price DECIMAL(10,2) AS (Tax + Gem_price) STORED,
    Organizer_ID INT NOT NULL,
    Current_Capacity INT NOT NULL DEFAULT 0,
    Max_Capacity INT NOT NULL,
    
    CONSTRAINT fk_booking_location
        FOREIGN KEY (Location_ID)
        REFERENCES travel_spot(Location_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    CONSTRAINT fk_booking_organizer
        FOREIGN KEY (Organizer_ID)
        REFERENCES User(User_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE User_Booking (
	User_Booking_ID INT AUTO_INCREMENT PRIMARY KEY,
	Booking_ID INT NOT NULL,
	User_ID INT NOT NULL,
	Role VARCHAR(50) NOT NULL,
	CONSTRAINT fk_userbooking_booking
		FOREIGN KEY (Booking_ID)
		REFERENCES booking(Booking_ID)
		ON DELETE CASCADE
		ON UPDATE CASCADE,

	CONSTRAINT fk_userbooking_user
		FOREIGN KEY (User_ID)
		REFERENCES User(User_ID)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

INSERT INTO Points_Tier (Tier_Name, Min_Points, Max_Points) VALUES
('Surface Miner', 0, 99),
('Shallow Digger', 100, 299),
('Deep Miner', 300, 599),
('Cavern Navigator', 600, 999),
('Chasm Delver', 1000, 1999),
('Vein Striker', 2000, 3999),
('Molten Explorer', 4000, 6999),
('Abyss Excavator', 7000, 9999),
('Bedrock Breaker', 10000, 14999),
('Core Conqueror', 15000, 24999);

INSERT INTO User (First_Name, Last_Name, Nationality, Points, Tier_ID, Password, Is_Admin) VALUES
('Admin', 'User', NULL, NULL, NULL, 'admin123', TRUE),
('Joreve', 'De Jesus', 'Filipino', 50, 1, 'joreve123', FALSE),
('Martin', 'Gamilla', 'Filipino', 199, 2, 'password1234PogzLi', FALSE),
('Jhiro', 'Lirio', 'Filipino', 459, 3, '12413224JL', FALSE),
('Aaron', 'Romero', 'Filipino', 999, 4, 'aaroncool', FALSE),
('Frederick Voltair', 'Garcia', 'Filipino', 1234, 5, 'thunder1515', FALSE),
('Stefan Manuel', 'Domingo', 'Filipino', 2001, 6, 'uh}>aCyWt?tY95Y', FALSE),
('Ejreen', 'Del Mundo', 'Filipino', 6998, 7, '15Th15Th@Kru5TyKr4B?', FALSE),
('Ethan', 'Franco', 'Filipino', 8000, 8, '#$21yeSyeS@', FALSE),
('Neil Jr.', 'Gutang', 'Filipino', 10000, 9, 'TheSh4DowNeverB3trays!!!', FALSE),
('Jakov Benedict', 'Santos', 'Filipino', 23000, 10, 'Cisco123', FALSE),
('Mariel', 'Yasamuro', 'Japanese', 765, 4, 'ilovefeet', FALSE),
('Dustine Gian', 'Rivera', 'Puerto Rican', 1999, 5, 'root', FALSE);

INSERT INTO User_Email (User_ID, Email) VALUES
(2, 'jorevepangandejesus@gmail.com'),
(3, 'martin_gamilla@dlsu.edu.ph'),
(4, 'jhiro_lirio@dlsu.edu.ph'),
(5, 'aaronzrom@gmail.com'),
(6, 'derickgarcia1212@gmail.com'),
(7, 'stefan_manuel_domingo@dlsu.edu.ph'),
(8, 'ejreen_delmundo@dlsu.edu.ph'),
(10, 'darkgamer17298@gmail.com'),
(11, 'santosjakov@gmail.com'),
(12, 'mariel_yasumuro@dlsu.edu.ph'),
(13, 'aaron@gmail.com');

INSERT INTO User_Phone (User_ID, Phone_Number) VALUES
(2, '09287232389'),
(3, '09171287361'),
(4, '09159773025'),
(5, '+86-138-1234-5678'),
(5, '639562600656'),
(6, '09162579113'),
(7, '09279109865'),
(8, '09175119762'),
(11, '09493339430'),
(13, '09391809204');

INSERT INTO Country (country_name) VALUES
('Philippines'),
('Japan'),
('USA'),
('France'),
('Italy'),
('India'),
('Australia'),
('Germany'),
('Spain'),
('Turkey'),
('Thailand');

INSERT INTO Region (region_name, country_id) VALUES
('Region I', 1),
('Region III', 1),
('NCR', 1),
('Kanto Region' , 2),
('California', 3),
('Tuscany', 5),
('Berlin', 8),
('Hokkaido', 2),
('Madrid', 9),
('Istanbul Region', 10);

INSERT INTO City (city_name, region_id) VALUES
('Hakone-Machi', 4),
('Urdaneta City', 1),
('Manila City', 3),
('Malabon City' , 3 ),
('Angeles City', 2),
('San Fernando City', 1),
('Las Pinas City', 3),
('Tokyo', 4),
('Sapporo', 8),
('Los Angeles', 5),
('Siena', 6);

-- INSERT INTO Category (category_name) VALUES

-- INSERT INTO Travel_Spot (user_id, spotname, availability, date_shared, city_id, base_price, max_capacity) VALUES

-- INSERT INTO TS_Category (location_id, category_id) VALUES

INSERT INTO Reaction (Reaction_Name) VALUES
('Like'),
('Dislike'),
('Happy'),
('Laugh'),
('Sad'),
('Angry'),
('Love'),
('Crying'),
('Kiss'),
('Fire');

INSERT INTO User_Feedback (User_ID, Location_ID, Rating, is_recommendation, Comment_Count, Review_Date) VALUES
(1, 1, 4.6, TRUE, 0, CURRENT_TIMESTAMP),
(2, 2, 4.2, TRUE, 2, CURRENT_TIMESTAMP),
(2, 5, 3.8, FALSE, 0, CURRENT_TIMESTAMP),
(3, 7, 3.1, FALSE, 4, '2025-07-20 19:04:00'),
(4, 9, 4.6, TRUE, 1, '2025-07-21 09:25:30'),
(5, 10, 3.2, FALSE, 1, '2025-05-29 08:18:00'),
(6, 2, 2.5, FALSE, 0, '2025-05-08 22:15:02'),
(7, 3, 3.9, TRUE, 1, '2024-11-20 09:18:00'),
(8, 5, 2.9, FALSE, 1, '2024-11-20 11:21:00'),
(9, 1, 4.7, TRUE, 3, '2024-10-20 20:19:00'),
(10, 2, 4.1, TRUE, 0, '2024-10-21 09:45:00'),
(11, 3, 4.6, TRUE, 0, '2023-05-27 09:27:57'),
(2, 8, 5.0, TRUE, 1, '2023-05-16 08:15:56'),
(7, 8, 4.4, TRUE, 1, '2022-09-24 09:18:42'),
(8, 3, 4.2, TRUE, 0, '2022-09-27 15:17:12'),
(10, 10, 4.3, TRUE, 0, '2021-01-03 12:15:02'),
(10, 4, 5.0, TRUE, 0, '2021-01-20 13:21:01');

INSERT INTO User_Reaction (Review_ID, User_ID, ReactionType_ID, Reaction_Date) VALUES
(1, 2, 1, CURRENT_TIMESTAMP),
(1, 3, 1, CURRENT_TIMESTAMP),
(1, 4, 1, CURRENT_TIMESTAMP),
(1, 5, 2, CURRENT_TIMESTAMP),
(1, 6, 2, CURRENT_TIMESTAMP),
(1, 7, 1, CURRENT_TIMESTAMP),
(1, 8, 1, CURRENT_TIMESTAMP),
(1, 9, 1, CURRENT_TIMESTAMP),
(1, 10, 2, CURRENT_TIMESTAMP),
(1, 11, 2, CURRENT_TIMESTAMP),
(2, 2, 1, '2025-05-08 22:15:02'),
(2, 3, 1, '2025-05-20 09:18:00'),
(2, 7, 3, '2024-10-20 20:19:00'),
(2, 4, 8, '2024-05-20 09:18:00'),
(2, 8, 9, '2024-05-08 22:15:02'),
(3, 2, 1, '2024-10-21 12:45:00'),
(3, 4, 1, '2024-10-21 09:45:00'),
(3, 3, 3, '2023-05-27 09:27:57'),
(4, 8, 3, '2023-05-16 05:15:56'),
(4, 8, 4, '2022-11-24 09:18:42'),
(5, 3, 6, '2022-11-27 15:17:12'),
(6, 10, 7, '2022-02-03 12:17:02'),
(7, 4, 9, '2022-02-21 13:21:01');


-- INSERT INTO Booking (Location_ID, Status, Start_date, End_date, Tax, Gem_price, Organizer_ID, Current_Capacity, Max_Capacity) VALUES

-- INSERT INTO User_Booking (Booking_ID, User_ID, Role) VALUES
