/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.portfolio.loanaccount.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mifosplatform.infrastructure.core.service.ThreadLocalContextUtil;
import org.mifosplatform.infrastructure.core.service.Page;
import org.mifosplatform.infrastructure.jobs.annotation.CronTarget;
import org.mifosplatform.infrastructure.jobs.exception.JobExecutionException;
import org.mifosplatform.infrastructure.jobs.service.JobName;
import org.mifosplatform.portfolio.loanaccount.data.LoanScheduleAccrualData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanAccrualPlatformServiceImpl implements LoanAccrualPlatformService {

	private final static Logger logger = LoggerFactory.getLogger(LoanAccrualPlatformServiceImpl.class);
    private final LoanReadPlatformService loanReadPlatformService;
    private final LoanAccrualWritePlatformService loanAccrualWritePlatformService;

    @Autowired
    public LoanAccrualPlatformServiceImpl(final LoanReadPlatformService loanReadPlatformService,
            final LoanAccrualWritePlatformService loanAccrualWritePlatformService) {
        this.loanReadPlatformService = loanReadPlatformService;
        this.loanAccrualWritePlatformService = loanAccrualWritePlatformService;
    }

    @Override
    @CronTarget(jobName = JobName.ADD_ACCRUAL_ENTRIES)
    public void addAccrualAccounting() throws JobExecutionException {
    	ThreadLocalContextUtil.setIgnoreAccountClosureCheck(true);
        Collection<LoanScheduleAccrualData> loanScheduleAccrualDatas = this.loanReadPlatformService.retriveScheduleAccrualData();
        StringBuilder sb = new StringBuilder();
        Map<Long, Collection<LoanScheduleAccrualData>> loanDataMap = new HashMap<>();
        for (final LoanScheduleAccrualData accrualData : loanScheduleAccrualDatas) {
            if (loanDataMap.containsKey(accrualData.getLoanId())) {
                loanDataMap.get(accrualData.getLoanId()).add(accrualData);
            } else {
                Collection<LoanScheduleAccrualData> accrualDatas = new ArrayList<>();
                accrualDatas.add(accrualData);
                loanDataMap.put(accrualData.getLoanId(), accrualDatas);
            }
        }

        for (Map.Entry<Long, Collection<LoanScheduleAccrualData>> mapEntry : loanDataMap.entrySet()) {
            try {
                this.loanAccrualWritePlatformService.addAccrualAccounting(mapEntry.getKey(), mapEntry.getValue());
            } catch (Exception e) {
            	e.printStackTrace();
                Throwable realCause = e;
                if (e.getCause() != null) {
                    realCause = e.getCause();
                }
                sb.append("failed to add accural transaction for loan " + mapEntry.getKey() + " with message " + realCause.getMessage());
            }
        }

        if (sb.length() > 0) { throw new JobExecutionException(sb.toString()); }
    }

    @Override
    @CronTarget(jobName = JobName.ADD_PERIODIC_ACCRUAL_ENTRIES)
    public void addPeriodicAccruals() throws JobExecutionException {
    	ThreadLocalContextUtil.setIgnoreAccountClosureCheck(true);
      /*  String errors = addPeriodicAccruals(LocalDate.now());*/
        String errors = addPeriodicAccruals(LocalDate.now(), null, null);
        if (errors.length() > 0) { throw new JobExecutionException(errors); }
    }

    @Override
/*    public String addPeriodicAccruals(final LocalDate tilldate) {
    	ThreadLocalContextUtil.setIgnoreAccountClosureCheck(true);
        Collection<LoanScheduleAccrualData> loanScheduleAccrualDatas = this.loanReadPlatformService.retrivePeriodicAccrualData(tilldate);
        return addPeriodicAccruals(tilldate, loanScheduleAccrualDatas);*/
	public String addPeriodicAccruals(final LocalDate tilldate, Integer fromLoanId, Integer toLoanId) {
		int offsetCounter = 0;
 		int maxPageSize = 5000;
 	final StringBuilder exceptionReasons = new StringBuilder();
         Page<LoanScheduleAccrualData> loanScheduleAccrualDatas = this.loanReadPlatformService.retrivePeriodicAccrualData(tilldate, offsetCounter, maxPageSize, fromLoanId, toLoanId);
        
         int totalFilteredRecords = loanScheduleAccrualDatas.getTotalFilteredRecords();
         logger.info("Post retrivePeriodicAccrualData entry for " + totalFilteredRecords + " Entries : In Progress...");
        exceptionReasons.append(addPeriodicAccruals(tilldate, loanScheduleAccrualDatas.getPageItems()));
       /*offsetCounter = maxPageSize;
       int processedRecords = maxPageSize;
        while (totalFilteredRecords > processedRecords) {    
        	 logger.info("No of Records Processed[" + processedRecords + "]");
         	loanScheduleAccrualDatas = this.loanReadPlatformService.retrivePeriodicAccrualData(tilldate, offsetCounter, maxPageSize, fromLoanId, toLoanId);
         	exceptionReasons.append(addPeriodicAccruals(tilldate, loanScheduleAccrualDatas.getPageItems()));
         	offsetCounter += maxPageSize;
             processedRecords += maxPageSize;
         } */
        logger.info("ExceptionStrring[" + exceptionReasons.toString() + "]");
       return exceptionReasons.toString();
    
    }

    @Override
    public String addPeriodicAccruals(final LocalDate tilldate, Collection<LoanScheduleAccrualData> loanScheduleAccrualDatas) {
    	
        StringBuilder sb = new StringBuilder();
        Map<Long, Collection<LoanScheduleAccrualData>> loanDataMap = new HashMap<>();
        for (final LoanScheduleAccrualData accrualData : loanScheduleAccrualDatas) {
            if (loanDataMap.containsKey(accrualData.getLoanId())) {
            	
                loanDataMap.get(accrualData.getLoanId()).add(accrualData);
            } else {
                Collection<LoanScheduleAccrualData> accrualDatas = new ArrayList<>();
                accrualDatas.add(accrualData);
                loanDataMap.put(accrualData.getLoanId(), accrualDatas);
            }
        }
        int y=0;
        logger.info("No of Loans Getting processed in this cycle[" + loanDataMap.size() + "]");
        for (Map.Entry<Long, Collection<LoanScheduleAccrualData>> mapEntry : loanDataMap.entrySet()) {
        	logger.info("increment value["+y + "], LoanId[" + mapEntry.getKey() + "]");
        	y++;
            try {            	
                this.loanAccrualWritePlatformService.addPeriodicAccruals(tilldate, mapEntry.getKey(), mapEntry.getValue());
                // System.out.println("LOAN ID PROCCESSED AND ADDED"+mapEntry.getKey());
            } catch (Exception e) {
                Throwable realCause = e;
                if (e.getCause() != null) {
                    realCause = e.getCause();
                }
                sb.append("failed to add accural transaction for loan " + mapEntry.getKey() + " with message " + realCause.getMessage());
            }
        }

        return sb.toString();
    }

}