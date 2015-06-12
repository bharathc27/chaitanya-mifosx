package org.mifosplatform.portfolio.calendar.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

public class MissingInstallmentDateException extends AbstractPlatformDomainRuleException{

	public MissingInstallmentDateException(String postFix,String defaultUserMessage, Object[] defaultUserMessageArgs) {
		super("error.msg.calendar." + defaultUserMessage + "", postFix, defaultUserMessageArgs);
	}

}
