package org.mifosplatform.portfolio.loanaccount.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

public class RescheduleRepaymentDateException extends AbstractPlatformDomainRuleException {

public RescheduleRepaymentDateException(final String postFix, final String defaultUserMessage, final Object... defaultUserMessageArgs) {
       super("error.msg.loan.disbursal." + postFix, defaultUserMessage, defaultUserMessageArgs);
   }

}