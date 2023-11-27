DROP DATABASE yApp;

CREATE DATABASE IF NOT EXISTS yApp;
USE yApp;

-- Users table
CREATE TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    region VARCHAR(255),
    phone_number VARCHAR(20),
    token VARCHAR(255)
);

-- Posts table
CREATE TABLE IF NOT EXISTS Posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    post_text TEXT,
    post_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Media table (optional)
CREATE TABLE IF NOT EXISTS Media (
    media_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    media_url VARCHAR(255),
    media_type ENUM('image', 'video'),
    FOREIGN KEY (post_id) REFERENCES Posts(post_id)
);

-- Comments table
CREATE TABLE IF NOT EXISTS Comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    comment_text TEXT NOT NULL,
    comment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES Posts(post_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Likes table
CREATE TABLE Reactions (
    reaction_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    user_id INT,
    type VARCHAR(255),
    FOREIGN KEY (post_id) REFERENCES Posts(post_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

ALTER TABLE Reactions ADD UNIQUE INDEX idx_user_post (user_id, post_id);

-- Friends table
CREATE TABLE IF NOT EXISTS Friendships (
    user1 INT,
    user2 INT,
    PRIMARY KEY (user1, user2),
    FOREIGN KEY (user1) REFERENCES Users(user_id),
    FOREIGN KEY (user2) REFERENCES Users(user_id)
);
