package com.otsi.retail.hsnDetails.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.hsnDetails.config.Config;
import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.gatewayresponse.GateWayResponse;
import com.otsi.retail.hsnDetails.model.AccountingBook;
import com.otsi.retail.hsnDetails.model.CreditDebitNotes;
import com.otsi.retail.hsnDetails.model.LedgerLogBook;
import com.otsi.retail.hsnDetails.repo.AccountingBookRepo;
import com.otsi.retail.hsnDetails.repo.CreditDebitNotesRepo;
import com.otsi.retail.hsnDetails.repo.LedgerLogBookRepo;
import com.otsi.retail.hsnDetails.vo.ReportsVo;
import com.otsi.retail.hsnDetails.vo.StoreVo;

/**
 * @author vasavi
 *
 */
@Component
public class ReportServiceImpl implements ReportService {

	@Autowired
	private LedgerLogBookRepo ledgerLogBookRepo;

	@Autowired
	private AccountingBookRepo accountingBookRepo;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Config config;

	@Override
	public List<ReportsVo> debitNotesByStores() {
		AccountType accountType = AccountType.DEBIT;
		List<ReportsVo> reports = new ArrayList<>();
		List<LedgerLogBook> ledgerLogs = ledgerLogBookRepo.findByAccountType(accountType);
		List<Long> storeIds = ledgerLogs.stream().map(x -> x.getStoreId()).distinct().collect(Collectors.toList());
		List<StoreVo> svos = new ArrayList<>();
		try {
			svos = getStoresForGivenId(storeIds);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		svos.stream().forEach(x -> {
			List<LedgerLogBook> store = ledgerLogBookRepo.findByStoreIdAndAccountType(x.getId(), accountType);
			long amount = store.stream().mapToLong(a -> a.getAmount()).sum();
			ReportsVo report = new ReportsVo();
			report.setAmount(amount);
			report.setStoreId(x.getId());
			report.setName(x.getName());
			reports.add(report);
		});

		return reports;
	}

	@Override
	public List<ReportsVo> usedAndBalancedAmountByStores() {
		AccountType accountType = AccountType.CREDIT;
		List<ReportsVo> reports = new ArrayList<>();
		List<AccountingBook> accountingBooks = accountingBookRepo.findByAccountType(accountType);

		List<Long> storeIds = accountingBooks.stream().map(x -> x.getStoreId()).distinct().collect(Collectors.toList());
		List<StoreVo> svos = new ArrayList<>();
		try {
			svos = getStoresForGivenId(storeIds);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		svos.stream().forEach(x -> {
			accountingBooks.stream().forEach(accountingBook -> {
				List<AccountingBook> store = accountingBookRepo.findByStoreIdAndUsedAmount(x.getId(),
						accountingBook.getUsedAmount());
				long usedAmount = store.stream().mapToLong(r -> r.getUsedAmount()).sum();
				long amount = store.stream().mapToLong(r -> r.getAmount()).sum();
				ReportsVo report = new ReportsVo();
				Long balanceAmount = Math.abs(usedAmount - amount);
				report.setAmount(balanceAmount);
				report.setUsedAmount(usedAmount);
				report.setStoreId(x.getId());
				report.setName(x.getName());
				reports.add(report);
			});
		});
		return reports;
	}

	public List<StoreVo> getStoresForGivenId(List<Long> storeIds) throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		URI uri = UriComponentsBuilder.fromUri(new URI(config.getStoreDetails())).build().encode().toUri();

		HttpEntity<List<Long>> request = new HttpEntity<List<Long>>(storeIds, headers);

		ResponseEntity<?> notesResponse = restTemplate.exchange(uri, HttpMethod.POST, request, GateWayResponse.class);

		System.out.println("Received Request to getStoreDetails:" + notesResponse);
		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(notesResponse.getBody(), GateWayResponse.class);

		List<StoreVo> bvo = mapper.convertValue(gatewayResponse.getResult(), new TypeReference<List<StoreVo>>() {
		});
		return bvo;

	}

}
