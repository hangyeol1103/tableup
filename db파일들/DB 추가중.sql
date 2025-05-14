CREATE TABLE Restaurant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    address VARCHAR(255),
    image_url VARCHAR(255),
    open_time TIME,
    close_time TIME,
    status ENUM('OPEN', 'CLOSED') DEFAULT 'OPEN',
    min_people INT DEFAULT 2,
    remaining_seats INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Reservation (
    id INT AUTO_INCREMENT KEY,
    member_id INT NOT NULL,
    restaurant_id INT NOT NULL,
    reserve_date DATE NOT NULL,
    reserve_time TIME NOT NULL,
    people_count INT NOT NULL,
    status ENUM('RESERVED', 'CANCELLED') DEFAULT 'RESERVED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(id)
    ON DELETE CASCADE
);
