
CREATE TABLE IF NOT EXISTS `chai_villages` (
`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
`external_id` VARCHAR(50) NULL DEFAULT '0',
`office_id` BIGINT(20) NOT NULL,
`village_name` VARCHAR(50) NOT NULL,
`village_code` VARCHAR(50) NULL DEFAULT NULL,
`counter` BIGINT(20) NOT NULL DEFAULT '0',
`taluk` VARCHAR(50) NULL DEFAULT '0',
`district` VARCHAR(50) NULL DEFAULT '0',
`pincode` BIGINT(20) NULL DEFAULT '0',
`status` VARCHAR(50) NULL DEFAULT NULL,
`state` VARCHAR(50) NULL DEFAULT NULL,
`activatedon_userid` BIGINT(20) NULL DEFAULT NULL,
`activatedon_date` DATE NULL DEFAULT NULL,
`submitedon_userid` BIGINT(20) NULL DEFAULT NULL,
`submitedon_date` DATE NULL DEFAULT NULL,
PRIMARY KEY (`id`),
UNIQUE INDEX `village_name` (`village_name`, `office_id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;


CREATE TABLE IF NOT EXISTS `chai_village_center` (
`center_id` BIGINT(20) NOT NULL,
`village_id` BIGINT(20) NOT NULL,
PRIMARY KEY (`center_id`, `village_id`),
INDEX `center_id` (`village_id`),
CONSTRAINT `chai_village_center_ibfk1` FOREIGN KEY (`center_id`) REFERENCES `m_group` (`id`),
CONSTRAINT `chai_village_center_ibfk2` FOREIGN KEY (`village_id`) REFERENCES `chai_villages` (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio_village', 'CREATE_VILLAGE', 'VILLAGE', 'CREATE', 0);

INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio_village', 'UPDATE_VILLAGE', 'VILLAGE', 'UPDATE', 0);

INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio_village', 'DELETE_VILLAGE', 'VILLAGE', 'DELETE', 0);

INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio_village', 'ACTIVATE_VILLAGE', 'VILLAGE', 'ACTIVATE', 1);