ALTER TABLE `m_loan`
 ADD COLUMN `is_job_successed` TINYINT  NULL DEFAULT '1' AFTER `accrued_from`;