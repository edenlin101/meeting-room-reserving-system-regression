-- user_service
CREATE TABLE `users` (
    `id`         INT AUTO_INCREMENT,
    `username`   VARCHAR(50) NOT NULL UNIQUE,
    `password`   VARCHAR(100) NOT NULL,
    `company_id` INT,
    `status`     VARCHAR(20) NOT NULL,
    `role`       VARCHAR(20) NOT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO `users` (`username`, `password`, `status`, `role`) VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3', 'ACTIVE', 'ADMIN'); -- md5 for 'admin'
