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
import com.otsi.retail.hsnDetails.repository.CreditDebitNotesRepository;
import com.otsi.retail.hsnDetails.vo.ReportsVO;
import com.otsi.retail.hsnDetails.vo.StoreVO;

/**
 * @author vasavi
 *
 */
@Component
public class ReportServiceImpl implements ReportService {

	@Autowired
	private CreditDebitNotesRepository creditDebitNotesRepo;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Config config;

	@Override
	public List<ReportsVO> debitNotesByStores() {
		String creditDebit = "D";
		List<ReportsVO> reports = new ArrayList<>();
		List<CreditDebitNotes> allNotes = creditDebitNotesRepo.findByCreditDebit(creditDebit);
		List<Long> storeIds = allNotes.stream().map(x -> x.getStoreId()).distinct().collect(Collectors.toList());
		List<StoreVO> svos = new ArrayList<>();
		try {
			svos = getStoresForGivenId(storeIds);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		svos.stream().forEach(x -> {
			List<CreditDebitNotes> store = creditDebitNotesRepo.findByStoreIdAndCreditDebit(x.getId(), creditDebit);
			long amount = store.stream().mapToLong(a -> a.getActualAmount()).sum();
			ReportsVO report = new ReportsVO();
			report.setDAmount(amount);
			report.setStoreId(x.getId());
			report.setName(x.getName());
			reports.add(report);
		});

		return reports;
	}

	@Override
	public List<ReportsVO> usedAndBalancedAmountByStores() {
		String creditDebit = "C";
		List<ReportsVO> reports = new ArrayList<>();
		List<CreditDebitNotes> allNotes = creditDebitNotesRepo.findByCreditDebit(creditDebit);

		List<Long> storeIds = allNotes.stream().map(x -> x.getStoreId()).distinct().collect(Collectors.toList());
		List<StoreVO> svos = new ArrayList<>();
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
			ReportsVO report = new ReportsVO();
			report.setActualAmount(actualAmount);
			report.setTransactionAmount(transactionAmount);
			report.setStoreId(x.getId());
			report.setName(x.getName());
			reports.add(report);
		});

		return reports;
	}

	public List<StoreVO> getStoresForGivenId(List<Long> storeIds) throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		URI uri = UriComponentsBuilder.fromUri(new URI(config.getStoreDetails())).build().encode().toUri();

		HttpEntity<List<Long>> request = new HttpEntity<List<Long>>(storeIds, headers);

		ResponseEntity<?> notesResponse = restTemplate.exchange(uri, HttpMethod.POST, request, GateWayResponse.class);

		System.out.println("Received Request to getStoreDetails:" + notesResponse);
		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(notesResponse.getBody(), GateWayResponse.class);

		List<StoreVO> bvo = mapper.convertValue(gatewayResponse.getResult(), new TypeReference<List<StoreVO>>() {
		});
		return bvo;

	}

}
