ALTER TABLE `m_calendar_instance`
	ADD COLUMN `rescheduled_date` DATE NULL AFTER `entity_type_enum`;