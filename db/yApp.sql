CREATE DATABASE IF NOT EXISTS yApp;
USE yApp;

-- Users table
CREATE OR ALTER TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    region VARCHAR(255),
    phone_number VARCHAR(20),
    user_numbers INT
);

-- Posts table
CREATE OR ALTER TABLE IF NOT EXISTS Posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    post_text TEXT,
    post_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Media table (optional)
CREATE OR ALTER TABLE IF NOT EXISTS Media (
    media_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    media_url VARCHAR(255),
    media_type ENUM('image', 'video'),
    FOREIGN KEY (post_id) REFERENCES Posts(post_id)
);

-- Comments table
CREATE OR ALTER TABLE IF NOT EXISTS Comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    comment_text TEXT NOT NULL,
    comment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES Posts(post_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Likes table
CREATE OR ALTER TABLE IF NOT EXISTS Likes (
    like_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES Posts(post_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

SELECT * FROM Users;