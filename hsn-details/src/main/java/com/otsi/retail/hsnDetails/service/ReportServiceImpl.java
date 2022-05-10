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
import com.otsi.retail.hsnDetails.gatewayresponse.GateWayResponse;
import com.otsi.retail.hsnDetails.model.CreditDebitNotes;
import com.otsi.retail.hsnDetails.repo.CreditDebitNotesRepo;
import com.otsi.retail.hsnDetails.vo.ReportsVo;
import com.otsi.retail.hsnDetails.vo.StoreVo;

/**
 * @author vasavi
 *
 */
@Component
public class ReportServiceImpl implements ReportService {

	@Autowired
	private CreditDebitNotesRepo creditDebitNotesRepo;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Config config;

	@Override
	public List<ReportsVo> debitNotesByStores() {
		String creditDebit = "D";
		List<ReportsVo> reports = new ArrayList<>();
		List<CreditDebitNotes> allNotes = creditDebitNotesRepo.findByCreditDebit(creditDebit);
		List<Long> storeIds = allNotes.stream().map(x -> x.getStoreId()).distinct().collect(Collectors.toList());
		List<StoreVo> svos = new ArrayList<>();
		try {
			svos = getStoresForGivenId(storeIds);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		svos.stream().forEach(x -> {
			List<CreditDebitNotes> store = creditDebitNotesRepo.findByStoreIdAndCreditDebit(x.getId(), creditDebit);
			long amount = store.stream().mapToLong(a -> a.getActualAmount()).sum();
			ReportsVo report = new ReportsVo();
			report.setDAmount(amount);
			report.setStoreId(x.getId());
			report.setName(x.getName());
			reports.add(report);
		});

		return reports;
	}

	@Override
	public List<ReportsVo> usedAndBalancedAmountByStores() {
		String creditDebit = "C";
		List<ReportsVo> reports = new ArrayList<>();
		List<CreditDebitNotes> allNotes = creditDebitNotesRepo.findByCreditDebit(creditDebit);

		List<Long> storeIds = allNotes.stream().map(x -> x.getStoreId()).distinct().collect(Collectors.toList());
		List<StoreVo> svos = new ArrayList<>();
		try {
			svos = getStoresForGivenId(storeIds);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		svos.stream().forEach(x -> {
			String status = "used";
			List<CreditDebitNotes> store = creditDebitNotesRepo.findByStoreIdAndStatus(x.getId(), status);
			long transactionAmount = store.stream().mapToLong(r -> r.getTransactionAmount()).sum();

			List<CreditDebitNotes> store1 = creditDebitNotesRepo.findByStoreIdAndFlag(x.getId(), Boolean.TRUE);
			long actualAmount = store1.stream().mapToLong(r -> r.getActualAmount()).sum();
			ReportsVo report = new ReportsVo();
			report.setActualAmount(actualAmount);
			report.setTransactionAmount(transactionAmount);
			report.setStoreId(x.getId());
			report.setName(x.getName());
			reports.add(report);
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
