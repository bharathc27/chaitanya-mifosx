CREATE TABLE `m_loan_tranche_disbursement_charge` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`loan_charge_id` BIGINT(20) NOT NULL,
	`disbursement_detail_id` BIGINT(20) NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

ALTER TABLE `m_loan_tranche_disbursement_charge`
	ADD CONSTRAINT `FK_m_loan_tranche_disbursement_charge_m_loan_charge` FOREIGN KEY (`loan_charge_id`) REFERENCES `m_loan_charge` (`id`),
	ADD CONSTRAINT `FK_m_loan_tranche_disbursement_charge_m_loan_disbursement_detail` FOREIGN KEY (`disbursement_detail_id`) REFERENCES `m_loan_disbursement_detail` (`id`);
	
	