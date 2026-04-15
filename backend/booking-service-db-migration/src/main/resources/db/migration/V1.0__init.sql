-- booking_service
CREATE TABLE `reservations` (
    `id`         INT AUTO_INCREMENT,
    `user_id`    INT NOT NULL,
    `company_id` INT NOT NULL,
    `room_id`    INT NOT NULL,
    `date`       DATE NOT NULL,
    `start_time` TIME NOT NULL,
    `end_time`   TIME NOT NULL,
    `status`     VARCHAR(20) NOT NULL,
    `created_at` DATETIME NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `idx_room_date` (`room_id`, `date`)
);