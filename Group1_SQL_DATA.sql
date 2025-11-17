USE HiddenGemsDB;

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
('Core Conqueror', 15000, 25000);

INSERT INTO User (First_Name, Last_Name, Nationality, Points, Tier_ID, Password, Is_Admin) VALUES
('Admin', 'User', NULL, NULL, NULL, '@dmin123', TRUE),
('Joreve', 'De Jesus', 'Filipino', 50, 1, 'joreve123', FALSE),
('Martin', 'Gamilla', 'American', 199, 2, 'm4rt1n', FALSE),
('Jhiro', 'Lirio', 'Chinese', 459, 3, 'liri0jhir0', FALSE),
('Aaron', 'Romero', 'Indian', 999, 4, 'aaronpass2025', FALSE),
('Elisabeth', 'Meyer', 'German', 1234, 5, 'El!', FALSE),
('Andre', 'Agreste', 'French', 2001, 6, 'ndrgrst', FALSE),
('Viktor', 'Igor', 'Russian', 6998, 7, 'myp4ssword', FALSE),
('Sofia', 'Rossi', 'Brazilian', 8000, 8, 's3cr3t', FALSE),
('Keiko', 'Sato', 'Japanese', 10000, 9, 'cakeko123', FALSE),
('Juan', 'Dela Cruz', 'Filipino', 22000, 10, 'juan&only', FALSE);

INSERT INTO User_Email (User_ID, Email) VALUES
(2, 'joreve@email.com'),
(3, 'martin@email.com'),
(4, 'lirio@email.com'),
(5, 'romero@email.com'),
(6, 'elisabeth@email.com'),
(7, 'andre@email.com'),
(8, 'igor@email.com'),
(9, 'rossi@email.com'),
(10, 'keiko@email.com'),
(11, 'juan@email.com');

INSERT INTO User_Phone (User_ID, Phone_Number) VALUES
(2, '+63-917-123-4567'),
(3, '+1-555-0198'),
(4, '+86-138-1234-5678'),
(5, '+91-98765-43210'),
(6, '+49-151-2345-6789'),
(7, '+33-6-12-34-56-78'),
(8, '+7-916-123-4567'),
(9, '+55-11-98765-4321'),
(10, '+81-90-1234-5678'),
(11, '+63-919-876-5432');