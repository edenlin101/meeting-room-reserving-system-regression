-- resource_service
CREATE TABLE `companies` (
    `id`   INT AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
);

CREATE TABLE `rooms` (
    `id`         INT AUTO_INCREMENT,
    `name`       VARCHAR(100) NOT NULL,
    `company_id` INT NOT NULL,
    PRIMARY KEY (`id`)
);
