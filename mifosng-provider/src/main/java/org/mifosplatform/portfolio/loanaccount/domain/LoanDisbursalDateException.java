package org.mifosplatform.portfolio.loanaccount.domain;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

public class LoanDisbursalDateException extends  AbstractPlatformDomainRuleException {

	public LoanDisbursalDateException(String globalisationMessageCode, String defaultUserMessage, Object... defaultUserMessageArgs) {
		super("error.msg.loan.disbursement.cannot.be.made.before.last.transaction.date", defaultUserMessage, defaultUserMessageArgs);
	}

}
