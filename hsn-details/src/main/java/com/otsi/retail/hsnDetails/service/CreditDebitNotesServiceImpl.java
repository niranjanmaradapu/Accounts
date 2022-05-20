package com.otsi.retail.hsnDetails.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.otsi.retail.hsnDetails.config.Config;
import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.enums.PaymentStatus;
import com.otsi.retail.hsnDetails.enums.PaymentType;
import com.otsi.retail.hsnDetails.exceptions.RecordNotFoundException;
import com.otsi.retail.hsnDetails.gatewayresponse.GateWayResponse;
import com.otsi.retail.hsnDetails.mapper.AccountingBookMapper;
import com.otsi.retail.hsnDetails.mapper.CreditDebitNotesMapper;
import com.otsi.retail.hsnDetails.mapper.LedgerLogBookMapper;
import com.otsi.retail.hsnDetails.model.AccountingBook;
import com.otsi.retail.hsnDetails.model.CreditDebitNotes;
import com.otsi.retail.hsnDetails.model.LedgerLogBook;
import com.otsi.retail.hsnDetails.repo.AccountingBookRepo;
import com.otsi.retail.hsnDetails.repo.CreditDebitNotesRepo;
import com.otsi.retail.hsnDetails.repo.LedgerLogBookRepo;
import com.otsi.retail.hsnDetails.util.DateConverters;
import com.otsi.retail.hsnDetails.vo.AccountingBookVo;
import com.otsi.retail.hsnDetails.vo.CreditDebitNotesVo;
import com.otsi.retail.hsnDetails.vo.GetUserRequestVo;
import com.otsi.retail.hsnDetails.vo.LedgerLogBookVo;
import com.otsi.retail.hsnDetails.vo.PaymentDetailsVo;
import com.otsi.retail.hsnDetails.vo.SearchFilterVo;
import com.otsi.retail.hsnDetails.vo.UpdateCreditRequest;
import com.otsi.retail.hsnDetails.vo.UserDetailsVo;

/**
 * @author vasavi
 *
 */
@Component
public class CreditDebitNotesServiceImpl implements CreditDebitNotesService {

	private Logger log = LoggerFactory.getLogger(CreditDebitNotesServiceImpl.class);

	@Autowired
	private CreditDebitNotesRepo creditDebitNotesRepo;

	@Autowired
	private CreditDebitNotesMapper creditDebitNotesMapper;

	@Autowired
	private AccountingBookMapper accountingBookMapper;

	@Autowired
	private LedgerLogBookMapper ledgerLogBookMapper;

	@Autowired
	private AccountingBookRepo accountingBookRepo;

	@Autowired
	private LedgerLogBookRepo ledgerLogBookRepo;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Config config;

	@Override
	public String saveCreditDebitNotes(CreditDebitNotesVo creditDebitNotesVo) {
		boolean flag = true;
		CreditDebitNotes cred = creditDebitNotesRepo.findByMobileNumberAndCustomerIdAndFlag(
				creditDebitNotesVo.getMobileNumber(), creditDebitNotesVo.getCustomerId(), flag);
		if (cred != null) {
			cred.setFlag(false);
			creditDebitNotesRepo.save(cred);

			CreditDebitNotes cd = creditDebitNotesMapper.VoToEntity(creditDebitNotesVo);
			cd.setFlag(true);
			cd.setActualAmount(creditDebitNotesVo.getActualAmount() + cred.getActualAmount());
			cd.setStatus("credited");
			creditDebitNotesRepo.save(cd);
		} else {

			CreditDebitNotes cd = creditDebitNotesMapper.VoToEntity(creditDebitNotesVo);
			if (creditDebitNotesVo.getCreditDebit().equals("C")) {
				cd.setStatus("credited");
			} else if (creditDebitNotesVo.getCreditDebit().equals("D")) {
				cd.setStatus("debited");
			}
			CreditDebitNotes notesSave = creditDebitNotesRepo.save(cd);
		}
		log.warn("we are checking if notes is saved...");
		log.info("note details  saved successfully");
		return "note details  saved successfully";

	}

	@Override
	public List<CreditDebitNotesVo> getCreditNotes(String mobileNumber, Long customerId) {
		log.debug("debugging getMobileNumber:" + mobileNumber);
		List<CreditDebitNotesVo> voList = new ArrayList<>();
		boolean flag = true;
		List<CreditDebitNotes> mob = creditDebitNotesRepo.findAllByMobileNumberAndCustomerIdAndFlag(mobileNumber,
				customerId, flag);
		List<CreditDebitNotes> mob1 = creditDebitNotesRepo.findByCustomerIdAndFlag(customerId, flag);
		if (mob1 != null && !mob1.isEmpty()) {
			mob1.stream().forEach(m -> {
				if (m.getCustomerId() != null) {
					CreditDebitNotesVo vo = creditDebitNotesMapper.EntityToVo(m);
					voList.add(vo);
				}
			});
			return voList;

		} else
			log.error("no record found with customerId:" + customerId);
		throw new RecordNotFoundException("no record found with  customerId:" + customerId);

	}

	@Override
	public List<CreditDebitNotes> getAllCreditDebitNotes() {
		log.debug("debugging getAllDebitNotes:");
		List<CreditDebitNotes> vo = creditDebitNotesRepo.findAll();
		log.warn("we are testing is fetching all credit-debit notes");
		log.info("after fetching all credit-debitnotes:" + vo.toString());
		return vo;
	}

	@Override
	public List<CreditDebitNotesVo> saveListCreditDebitNotes(List<CreditDebitNotesVo> creditDebitNotesVos) {
		log.debug("debugging saveListDebitNotes:" + creditDebitNotesVos);
		List<CreditDebitNotes> voList = creditDebitNotesMapper.VoToEntity(creditDebitNotesVos);
		creditDebitNotesVos = creditDebitNotesMapper.EntityToVo(creditDebitNotesRepo.saveAll(voList));
		log.warn("we are testing is saving all credit-debit notes");
		log.info("after saving all credit-debit notes:" + creditDebitNotesVos.toString());
		return creditDebitNotesVos;
	}

	@Override
	public String updateCreditDebitNotes(UpdateCreditRequest updateCreditRequest) {
		log.debug("debugging updateCreditDebitNotes:" + updateCreditRequest);
		boolean flag = true;
		if (updateCreditRequest.getCreditDebit().equals("C")) {
			CreditDebitNotes cred = creditDebitNotesRepo.findByMobileNumberAndStoreIdAndCreditDebitAndFlag(
					updateCreditRequest.getMobileNumber(), updateCreditRequest.getStoreId(),
					updateCreditRequest.getCreditDebit(), flag);

			if ((cred.getMobileNumber().equals(updateCreditRequest.getMobileNumber()))
					&& (cred.getStoreId() == updateCreditRequest.getStoreId())
					&& (cred.getCreditDebit().equals(updateCreditRequest.getCreditDebit()))) {

				if (cred.getActualAmount() >= updateCreditRequest.getAmount()) {
					cred.setFlag(false);
					creditDebitNotesRepo.save(cred);
					CreditDebitNotesVo credVo = new CreditDebitNotesVo();
					credVo.setApprovedBy(cred.getApprovedBy());
					credVo.setCreditDebit(cred.getCreditDebit());
					credVo.setStoreId(cred.getStoreId());
					credVo.setCustomerId(cred.getCustomerId());
					credVo.setFlag(true);
					credVo.setMobileNumber(cred.getMobileNumber());
					credVo.setStoreId(cred.getStoreId());
					credVo.setTransactionAmount(updateCreditRequest.getAmount());
					credVo.setActualAmount(Math.abs(cred.getActualAmount() - updateCreditRequest.getAmount()));
					credVo.setStatus("used");
					credVo.setComments(updateCreditRequest.getAmount() + " used ");
					CreditDebitNotes credMapper = creditDebitNotesMapper.VoToEntity(credVo);
					creditDebitNotesRepo.save(credMapper);
				}

			}

		} else if (updateCreditRequest.getCreditDebit().equals("D")) {
			CreditDebitNotes deb = new CreditDebitNotes();
			deb.setActualAmount(updateCreditRequest.getAmount());
			deb.setStoreId(updateCreditRequest.getStoreId());
			deb.setCreditDebit("D");
			deb.setComments("debit");
			deb.setCreatedDate(LocalDate.now());
			deb.setLastModifiedDate(LocalDate.now());
			deb.setMobileNumber(updateCreditRequest.getMobileNumber());
			creditDebitNotesRepo.save(deb);
		}

		log.warn("we are checking if notes is updated...");
		log.info("notes details updated successfully with the given id");
		return "Notes updated successfully";
	}

	@Override
	public String updateNotes(CreditDebitNotesVo creditDebitNotesVo) {
		log.debug("debugging updateCreditDebitNotes:" + creditDebitNotesVo);
		Optional<CreditDebitNotes> notesOpt = creditDebitNotesRepo
				.findByCreditDebitId(creditDebitNotesVo.getCreditDebitId());

		// if id is not present,it will throw custom exception "RecordNotFoundException"
		if (!notesOpt.isPresent()) {
			log.error("Record Not Found with id:" + notesOpt.get());
			throw new RecordNotFoundException("Record Not Found with id:" + notesOpt.get());
		}

		CreditDebitNotes notes = creditDebitNotesMapper.VoToEntity(creditDebitNotesVo);
		CreditDebitNotes notesSave = creditDebitNotesRepo.save(notes);
		CreditDebitNotesVo notesUpdate = creditDebitNotesMapper.EntityToVo(notesSave);
		log.warn("we are checking if notes is updated...");
		log.info("notes details updated successfully with the given id:" + notesUpdate);
		return "Notes updated successfully:" + notesUpdate.getMobileNumber();
	}

	@Override
	public String deleteCreditDebitNotes(Long creditDebitId) {
		log.debug("debugging deleteCreditDebitNotes:" + creditDebitId);
		Optional<CreditDebitNotes> notes = creditDebitNotesRepo.findById(creditDebitId);
		if (!(notes.isPresent())) {
			log.error("Record Not Found with id:" + notes.get());
			throw new RecordNotFoundException("Record Not Found with id:" + notes.get());
		}
		creditDebitNotesRepo.delete(notes.get());
		log.warn("we are checking if notes data is fetching by id...");
		log.info("after deleting notes:" + creditDebitId);
		return "deleted notes successfully:" + creditDebitId;
	}

	@Override
	public List<CreditDebitNotesVo> getAllCreditNotes(CreditDebitNotesVo creditNotesVo) {
		List<CreditDebitNotes> creditNotes = new ArrayList<>();
		String creditDebit = "C";
		boolean flag = true;
		List<CreditDebitNotes> storeOpt = creditDebitNotesRepo
				.findAllByStoreIdAndCreditDebitAndFlag(creditNotesVo.getStoreId(), creditDebit, flag);

		/*
		 * using dates and storeId
		 */
		if (creditNotesVo.getFromDate() != null && creditNotesVo.getToDate() != null
				&& creditNotesVo.getStoreId() != null && creditNotesVo.getMobileNumber() == "") {

			if (storeOpt != null) {

				creditNotes = creditDebitNotesRepo
						.findByCreatedDateBetweenAndStoreIdAndCreditDebitAndFlagOrderByLastModifiedDateAsc(
								creditNotesVo.getFromDate(), creditNotesVo.getToDate(), creditNotesVo.getStoreId(),
								creditDebit, flag);

				if (creditNotes.isEmpty()) {
					log.error("No record found with given information");
					throw new RecordNotFoundException("No record found with given information");
				}
			} else {
				throw new RecordNotFoundException("No record found with storeId");
			}
		}
		/*
		 * 
		 * using dates and mobile number and storeId
		 */
		else if (creditNotesVo.getFromDate() != null && creditNotesVo.getToDate() != null
				&& creditNotesVo.getMobileNumber() != null && creditNotesVo.getStoreId() != null) {

			if (storeOpt != null) {
				List<CreditDebitNotes> mobileOpt = creditDebitNotesRepo
						.findAllByMobileNumber(creditNotesVo.getMobileNumber());
				if (mobileOpt != null) {
					creditNotes = creditDebitNotesRepo
							.findByCreatedDateBetweenAndMobileNumberAndStoreIdAndCreditDebitAndFlagOrderByLastModifiedDateAsc(
									creditNotesVo.getFromDate(), creditNotesVo.getToDate(),
									creditNotesVo.getMobileNumber(), creditNotesVo.getStoreId(), creditDebit, flag);
				} else {
					log.error("No record found with given productItemId");
					throw new RecordNotFoundException("No record found with given productItemId");
				}
			}
		}

		/*
		 * using storeId
		 */
		else if (creditNotesVo.getFromDate() == null && creditNotesVo.getToDate() == null
				&& creditNotesVo.getMobileNumber() == "" && creditNotesVo.getStoreId() != null) {
			if (storeOpt.isEmpty()) {
				log.error("retail record is not found with storeId:" + creditNotesVo.getStoreId());
				throw new RecordNotFoundException(
						"retail record is not found with storeId:" + creditNotesVo.getStoreId());
			}
			if (creditNotesVo.getStoreId() != null) {
				List<CreditDebitNotesVo> creditList = creditDebitNotesMapper.EntityToVo(storeOpt);
				return creditList;
			} else {
				throw new RecordNotFoundException("No record found with storeId");
			}
		}
		/*
		 * using mobile number and storeId
		 */
		else if (creditNotesVo.getFromDate() == null && creditNotesVo.getToDate() == null
				&& creditNotesVo.getMobileNumber() != null && creditNotesVo.getStoreId() != null) {

			if (storeOpt != null) {
				CreditDebitNotes mobileOpt = creditDebitNotesRepo
						.findByMobileNumberAndFlag(creditNotesVo.getMobileNumber(), flag);
				if (mobileOpt == null) {
					throw new RecordNotFoundException("barcode record is not found");

				} else {

					creditNotes.add(mobileOpt);
					List<CreditDebitNotesVo> creditList = creditDebitNotesMapper.EntityToVo(creditNotes);
					return creditList;

				}
			} else {
				throw new RecordNotFoundException("No record found with storeId");
			}
		}

		else {
			List<CreditDebitNotes> creditNotes1 = creditDebitNotesRepo.findAll();
			List<CreditDebitNotesVo> creditList1 = creditDebitNotesMapper.EntityToVo(creditNotes1);
			return creditList1;
		}

		List<CreditDebitNotesVo> creditList = creditDebitNotesMapper.EntityToVo(creditNotes);
		log.warn("we are checking if credit notes is fetching...");
		log.info("after fetching all credit notes  details:" + creditList.toString());
		return creditList;
	}

	@Override
	public List<CreditDebitNotesVo> getAllDebitNotes(CreditDebitNotesVo debitNotesVo) {
		List<CreditDebitNotes> debitNotes = new ArrayList<>();
		String creditDebit = "D";
		boolean flag = true;
		List<CreditDebitNotes> storeOpt = creditDebitNotesRepo
				.findAllByStoreIdAndCreditDebitAndFlag(debitNotesVo.getStoreId(), creditDebit, flag);

		/*
		 * using dates and storeId
		 */
		if (debitNotesVo.getFromDate() != null && debitNotesVo.getToDate() != null && debitNotesVo.getStoreId() != null
				&& debitNotesVo.getMobileNumber() == "") {

			if (storeOpt != null) {

				debitNotes = creditDebitNotesRepo
						.findByCreatedDateBetweenAndStoreIdAndCreditDebitAndFlagOrderByLastModifiedDateAsc(
								debitNotesVo.getFromDate(), debitNotesVo.getToDate(), debitNotesVo.getStoreId(),
								creditDebit, flag);

				if (debitNotes.isEmpty()) {
					log.error("No record found with given information");
					throw new RecordNotFoundException("No record found with given information");
				}
			} else {
				throw new RecordNotFoundException("No record found with storeId");
			}
		}
		/*
		 * 
		 * using dates and mobile number and storeId
		 */
		else if (debitNotesVo.getFromDate() != null && debitNotesVo.getToDate() != null
				&& debitNotesVo.getMobileNumber() != null && debitNotesVo.getStoreId() != null) {

			if (storeOpt != null) {
				List<CreditDebitNotes> mobileOpt = creditDebitNotesRepo
						.findAllByMobileNumber(debitNotesVo.getMobileNumber());
				if (mobileOpt != null) {
					debitNotes = creditDebitNotesRepo
							.findByCreatedDateBetweenAndMobileNumberAndStoreIdAndCreditDebitAndFlagOrderByLastModifiedDateAsc(
									debitNotesVo.getFromDate(), debitNotesVo.getToDate(),
									debitNotesVo.getMobileNumber(), debitNotesVo.getStoreId(), creditDebit, flag);
				} else {
					log.error("No record found with given productItemId");
					throw new RecordNotFoundException("No record found with given productItemId");
				}
			}
		}

		/*
		 * using storeId
		 */
		else if (debitNotesVo.getFromDate() == null && debitNotesVo.getToDate() == null
				&& debitNotesVo.getMobileNumber() == "" && debitNotesVo.getStoreId() != null) {
			if (storeOpt.isEmpty()) {
				log.error("retail record is not found with storeId:" + debitNotesVo.getStoreId());
				throw new RecordNotFoundException(
						"retail record is not found with storeId:" + debitNotesVo.getStoreId());
			}
			if (debitNotesVo.getStoreId() != null) {
				List<CreditDebitNotesVo> debitList = creditDebitNotesMapper.EntityToVo(storeOpt);
				return debitList;
			} else {
				throw new RecordNotFoundException("No record found with storeId");
			}
		}
		/*
		 * using mobile number and storeId
		 */
		else if (debitNotesVo.getFromDate() == null && debitNotesVo.getToDate() == null
				&& debitNotesVo.getMobileNumber() != null && debitNotesVo.getStoreId() != null) {

			if (storeOpt != null) {
				CreditDebitNotes mobileOpt = creditDebitNotesRepo
						.findByMobileNumberAndFlag(debitNotesVo.getMobileNumber(), flag);
				if (mobileOpt == null) {
					throw new RecordNotFoundException("barcode record is not found");

				} else {

					debitNotes.add(mobileOpt);
					List<CreditDebitNotesVo> creditList = creditDebitNotesMapper.EntityToVo(debitNotes);
					return creditList;

				}
			} else {
				throw new RecordNotFoundException("No record found with storeId");
			}
		}

		else {
			List<CreditDebitNotes> debitNotes1 = creditDebitNotesRepo.findAll();
			List<CreditDebitNotesVo> debitList1 = creditDebitNotesMapper.EntityToVo(debitNotes1);
			return debitList1;
		}

		List<CreditDebitNotesVo> debitList = creditDebitNotesMapper.EntityToVo(debitNotes);
		log.warn("we are checking if debit notes is fetching...");
		log.info("after fetching all debit notes  details:" + debitList.toString());
		return debitList;
	}

	public UserDetailsVo getUserDetailsFromURM(@RequestParam String mobileNumber) {

		GetUserRequestVo userRequestVo = new GetUserRequestVo();
		String phoneNumber="+"+mobileNumber.trim();
		userRequestVo.setPhoneNo(phoneNumber);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GetUserRequestVo> entity = new HttpEntity<>(userRequestVo, headers);

		ResponseEntity<?> returnSlipListResponse = restTemplate.exchange(
				config.getGetCustomerDetailsFromURM() + "?mobileNumber=" + phoneNumber, HttpMethod.GET, entity,
				GateWayResponse.class);

		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		GateWayResponse<?> gatewayResponse = mapper.convertValue(returnSlipListResponse.getBody(),
				GateWayResponse.class);

		UserDetailsVo userDetailsVo = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<UserDetailsVo>() {
				});

		return userDetailsVo;

	}

	public List<UserDetailsVo> getCustomersForGivenIds(List<Long> userIds) throws URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		URI uri = UriComponentsBuilder.fromUri(new URI(config.getCustomerDetails())).build().encode().toUri();

		HttpEntity<List<Long>> request = new HttpEntity<List<Long>>(userIds, headers);

		ResponseEntity<?> newsaleResponse = restTemplate.exchange(uri, HttpMethod.POST, request, GateWayResponse.class);

		System.out.println("Received Request to getUserDetails:" + newsaleResponse);
		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(newsaleResponse.getBody(), GateWayResponse.class);

		List<UserDetailsVo> userDetailsVo = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<List<UserDetailsVo>>() {
				});
		return userDetailsVo;

	}

	@Override
	public LedgerLogBookVo saveNotes(LedgerLogBookVo ledgerLogBookVo) {
		LedgerLogBook ledgerLogBook = null;
		// to accounting type credit
		if (ledgerLogBookVo.getAccountType().equals(AccountType.CREDIT)) {
			ledgerLogBook = processCreditAccounting(ledgerLogBookVo);

		}
		// to do accounting type debit
		else if (ledgerLogBookVo.getAccountType().equals(AccountType.DEBIT)) {
			ledgerLogBook = processDebitAccounting(ledgerLogBookVo);

		}

		ledgerLogBookVo = ledgerLogBookMapper.entityToVo(ledgerLogBook);
		return ledgerLogBookVo;

	}

	private LedgerLogBook processCreditAccounting(LedgerLogBookVo ledgerLogBookVo) {
		LedgerLogBook ledgerLogBook = ledgerLogBookMapper.voToEntity(ledgerLogBookVo);
		// when customer credit note is created
		if (ledgerLogBookVo.getTransactionType().equals(AccountType.CREDIT)) {
			if (ledgerLogBookVo.getPaymentType().equals(PaymentType.Cash)
					|| ledgerLogBookVo.getPaymentType().equals(PaymentType.RTSlip)) {
				ledgerLogBook.setPaymentStatus(PaymentStatus.SUCCESS);
				ledgerLogBookVo = calculateAmount(ledgerLogBook);
			}
		}

		else if (ledgerLogBookVo.getTransactionType().equals(AccountType.DEBIT)) {
			// when the customer used the credit amount
			UserDetailsVo userDetailsVo = getUserDetailsFromURM(ledgerLogBookVo.getMobileNumber());
			if (userDetailsVo != null) {
				AccountingBook accountingBook = accountingBookRepo.findByCustomerIdAndAccountType(
						userDetailsVo.getUserId(), ledgerLogBookVo.getAccountType());

				ledgerLogBook.setPaymentStatus(PaymentStatus.DEBIT);
				ledgerLogBook.setReferenceNumber("DB" + RandomStringUtils.randomAlphanumeric(10));
				ledgerLogBook.setCustomerId(userDetailsVo.getUserId());
				ledgerLogBook.setAccountingBookId(accountingBook.getAccountingBookId());
				// to update used amount in accounting book
				if (accountingBook.getUsedAmount() == null) {
					accountingBook.setUsedAmount(ledgerLogBookVo.getAmount());
				} else {
					accountingBook.setUsedAmount(accountingBook.getUsedAmount() + ledgerLogBookVo.getAmount());
					accountingBook.setLastModifiedDate(LocalDateTime.now());
				}
				accountingBookRepo.save(accountingBook);
			}

		}
		ledgerLogBook = ledgerLogBookRepo.save(ledgerLogBook);

		return ledgerLogBook;
	}

	private LedgerLogBook processDebitAccounting(LedgerLogBookVo ledgerLogBookVo) {
		LedgerLogBook ledgerLogBook = ledgerLogBookMapper.voToEntity(ledgerLogBookVo);
		// when the customer debit note is created from sale
		if (ledgerLogBookVo.getTransactionType().equals(AccountType.DEBIT)) {
			ledgerLogBook.setPaymentStatus(PaymentStatus.DEBIT);
			ledgerLogBookVo = calculateAmount(ledgerLogBook);
		}

		else if (ledgerLogBookVo.getTransactionType().equals(AccountType.CREDIT)) {
			//UserDetailsVo userDetailsVo = getUserDetailsFromURM(ledgerLogBookVo.getMobileNumber());
			//if (userDetailsVo != null) {
				AccountingBook accountingBook = accountingBookRepo.findByCustomerIdAndStoreIdAndAccountType(
						ledgerLogBookVo.getCustomerId(), ledgerLogBookVo.getStoreId(), ledgerLogBookVo.getAccountType());
				if(ledgerLogBookVo.getPaymentType().equals(PaymentType.Cash)) {
					ledgerLogBook.setPaymentStatus(PaymentStatus.SUCCESS);
				}
				ledgerLogBook.setAccountingBookId(accountingBook.getAccountingBookId());
				ledgerLogBook.setReferenceNumber("CR" + RandomStringUtils.randomAlphanumeric(10));
				ledgerLogBook.setCustomerId(accountingBook.getCustomerId());
				// to update deducted amount in accounting book
				accountingBook.setAmount(Math.abs(ledgerLogBookVo.getAmount() - accountingBook.getAmount()));
				accountingBook.setLastModifiedDate(LocalDateTime.now());
				accountingBookRepo.save(accountingBook);
			//}

		}
		ledgerLogBook = ledgerLogBookRepo.save(ledgerLogBook);
		return ledgerLogBook;
	}

	private LedgerLogBookVo calculateAmount(LedgerLogBook ledgerLogBook) {
		AccountingBook accountingBookCustomer = accountingBookRepo
				.findByCustomerIdAndAccountType(ledgerLogBook.getCustomerId(), ledgerLogBook.getAccountType());
		if (accountingBookCustomer != null) {
			accountingBookCustomer.setAmount(accountingBookCustomer.getAmount() + ledgerLogBook.getAmount());
			accountingBookCustomer.setLastModifiedDate(LocalDateTime.now());
			AccountingBook accountingBookSave = accountingBookRepo.save(accountingBookCustomer);
			ledgerLogBook.setAccountingBookId(accountingBookSave.getAccountingBookId());

		} else {
			AccountingBook accountingBook = new AccountingBook();
			accountingBook.setAmount(ledgerLogBook.getAmount());
			accountingBook.setAccountType(ledgerLogBook.getAccountType());
			accountingBook.setCustomerId(ledgerLogBook.getCustomerId());
			accountingBook.setStoreId(ledgerLogBook.getStoreId());
			AccountingBook accountingBookSave = accountingBookRepo.save(accountingBook);
			ledgerLogBook.setAccountingBookId(accountingBookSave.getAccountingBookId());
		}
		LedgerLogBook ledgerLogBookSave = ledgerLogBookRepo.save(ledgerLogBook);
		LedgerLogBookVo ledgerLogBookVo = ledgerLogBookMapper.entityToVo(ledgerLogBookSave);
		return ledgerLogBookVo;

	}

	@Override
	public List<AccountingBookVo> getNotes(AccountType accountType, Long storeId) {
		List<AccountingBook> accountingBookOpt = accountingBookRepo.findByAccountTypeAndStoreId(accountType, storeId);
		if (accountingBookOpt == null) {
			log.error("accounting book id  is Not Found:" + accountingBookOpt);
			throw new RecordNotFoundException("accounting book id  is Not Found:" + accountingBookOpt);
		}
		List<AccountingBookVo> accountingBookVos = accountingBookMapper.entityToVo(accountingBookOpt);
		return accountingBookVos;
	}

	/*
	 * @Override public String delete(Long accountingBookId) {
	 * Optional<AccountingBook> accountingBookOpt =
	 * accountingBookRepo.findByAccountingBookId(accountingBookId); if
	 * (!(accountingBookOpt.isPresent())) {
	 * log.error("accounting book id  is Not Found:" + accountingBookOpt); throw new
	 * RecordNotFoundException("accounting book id  is Not Found:" +
	 * accountingBookOpt); } accountingBookRepo.delete(accountingBookOpt.get());
	 * return ""; }
	 */

	@Override
	public LedgerLogBookVo update(LedgerLogBookVo ledgerLogBookVo) {

		LedgerLogBook ledBook = ledgerLogBookRepo.findByLedgerLogBookId(ledgerLogBookVo.getLedgerLogBookId());
		if (ledBook == null) {
			throw new RecordNotFoundException("record not found with id:" + ledgerLogBookVo.getLedgerLogBookId());
		}
		AccountingBook accountingBookCustomer = accountingBookRepo.findByCustomerIdAndStoreIdAndAccountType(
				ledgerLogBookVo.getCustomerId(), ledgerLogBookVo.getStoreId(), ledgerLogBookVo.getAccountType());
		LedgerLogBook ledgerLogBook = ledgerLogBookMapper.voToEntityUpdate(ledBook, ledgerLogBookVo);
		LedgerLogBook ledgerLogBookSave = ledgerLogBookRepo.save(ledgerLogBook);

		if (accountingBookCustomer != null) {
			Long paymentValue = 0L;
			List<LedgerLogBook> ledgerLogAccount = ledgerLogBookRepo
					.findByAccountingBookId(ledBook.getAccountingBookId());
			paymentValue = ledgerLogAccount.stream().mapToLong(x -> x.getAmount()).sum();
			accountingBookCustomer.setAmount(paymentValue);
			AccountingBook accountingBookSave = accountingBookRepo.save(accountingBookCustomer);
			ledgerLogBook.setAccountingBookId(accountingBookSave.getAccountingBookId());
		} else {
			AccountingBook accountingBook = new AccountingBook();
			accountingBook.setAccountingBookId(ledgerLogBookSave.getAccountingBookId());
			accountingBook.setAmount(ledgerLogBookVo.getAmount());
			accountingBook.setAccountType(ledgerLogBookVo.getAccountType());
			accountingBook.setCustomerId(ledgerLogBookVo.getCustomerId());
			accountingBook.setStoreId(ledgerLogBookVo.getStoreId());
			AccountingBook accountingBookSave = accountingBookRepo.save(accountingBook);
			ledgerLogBook.setAccountingBookId(accountingBookSave.getAccountingBookId());
		}

		ledgerLogBookVo = ledgerLogBookMapper.entityToVo(ledgerLogBookSave);
		return ledgerLogBookVo;
	}

	@Override
	public Page<AccountingBookVo> getAllNotes(SearchFilterVo searchFilterVo, Pageable pageable) {
		Page<AccountingBook> accountingBooks = null;
		/*
		 * using from date and accountType and storeId
		 */
		if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() == null
				&& searchFilterVo.getStoreId() != null && searchFilterVo.getMobileNumber() == null
				&& searchFilterVo.getAccountType() != null) {
			LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
			LocalDateTime fromTime1 = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getFromDate());
			accountingBooks = accountingBookRepo.findByCreatedDateBetweenAndStoreIdAndAccountType(fromTime, fromTime1,
					searchFilterVo.getStoreId(), searchFilterVo.getAccountType(), pageable);

		}

		/*
		 * using dates and accountType and storeId
		 */
		else if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() != null
				&& searchFilterVo.getStoreId() != null && searchFilterVo.getMobileNumber() == null
				&& searchFilterVo.getAccountType() != null) {
			LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
			LocalDateTime toTime = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getToDate());
			accountingBooks = accountingBookRepo
					.findByLastModifiedDateBetweenAndStoreIdAndAccountTypeOrderByLastModifiedDateDesc(fromTime, toTime,
							searchFilterVo.getStoreId(), searchFilterVo.getAccountType(), pageable);

		}

		/*
		 * using dates and mobile number and accountType and storeId
		 */

		else if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() != null
				&& searchFilterVo.getMobileNumber() != null && searchFilterVo.getStoreId() != null
				&& searchFilterVo.getAccountType() != null) {

			UserDetailsVo userDetailsVo = getUserDetailsFromURM(searchFilterVo.getMobileNumber());
			if (userDetailsVo != null) {
				LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
				LocalDateTime toTime = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getToDate());
				accountingBooks = accountingBookRepo
						.findByCreatedDateBetweenAndCustomerIdAndStoreIdAndAccountTypeOrderByLastModifiedDateAsc(
								fromTime, toTime, userDetailsVo.getUserId(), searchFilterVo.getStoreId(),
								searchFilterVo.getAccountType(), pageable);
			}
		}

		/*
		 * using storeId and account type
		 */

		else if (searchFilterVo.getFromDate() == null && searchFilterVo.getToDate() == null
				&& searchFilterVo.getMobileNumber() == null && searchFilterVo.getStoreId() != null
				&& searchFilterVo.getAccountType() != null) {
			accountingBooks = accountingBookRepo.findByStoreIdAndAccountTypeOrderByCreatedDateDesc(
					searchFilterVo.getStoreId(), searchFilterVo.getAccountType(), pageable);
		}

		/*
		 * using mobile number and accountType and storeId
		 */

		else if (searchFilterVo.getFromDate() == null && searchFilterVo.getToDate() == null
				&& searchFilterVo.getMobileNumber() != null && searchFilterVo.getStoreId() != null
				&& searchFilterVo.getAccountType() != null) {

			UserDetailsVo userDetailsVo = getUserDetailsFromURM(searchFilterVo.getMobileNumber());
			if (userDetailsVo != null) {
				accountingBooks = accountingBookRepo.findByCustomerIdAndStoreIdAndAccountType(userDetailsVo.getUserId(),
						searchFilterVo.getStoreId(), searchFilterVo.getAccountType(), pageable);
			}
		}
		if (accountingBooks != null && accountingBooks.hasContent()) {
			return accountingBooks.map(accounting -> accountMapToVo(accounting));
		} else
			return Page.empty();
	}

	private AccountingBookVo accountMapToVo(AccountingBook accountingBook) {
		AccountingBookVo accountingBookVo = accountingBookMapper.entityToVo(accountingBook);

		List<AccountingBook> customerIds = accountingBookRepo.findAllByCustomerId(accountingBookVo.getCustomerId());
		List<Long> userIds = customerIds.stream().map(x -> x.getCustomerId()).distinct().collect(Collectors.toList());
		List<UserDetailsVo> userDetailsVo = null;
		try {
			userDetailsVo = getCustomersForGivenIds(userIds);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		if (userDetailsVo != null) {
			userDetailsVo.stream().forEach(x -> {
				accountingBookVo.setCustomerName(x.getUserName());
				accountingBookVo.setMobileNumber(x.getPhoneNumber());
			});
		}
		if (accountingBookVo.getUsedAmount() != null) {
			accountingBookVo
					.setBalanceAmount(Math.abs(accountingBookVo.getAmount() - accountingBookVo.getUsedAmount()));
		}
		return accountingBookVo;

	}

	@Override
	public Page<LedgerLogBookVo> getAllLedgerLogs(SearchFilterVo searchFilterVo, Pageable pageable) {

		Page<LedgerLogBook> ledgerLogs;
		/*
		 * using from date and storeId
		 */
		if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() == null
				&& searchFilterVo.getStoreId() != null && searchFilterVo.getMobileNumber() == null) {
			LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
			LocalDateTime fromTime1 = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getFromDate());
			ledgerLogs = ledgerLogBookRepo.findByCustomerIdAndCreatedDateBetweenAndStoreIdAndAccountType(
					searchFilterVo.getCustomerId(), fromTime, fromTime1, searchFilterVo.getStoreId(),
					searchFilterVo.getAccountType(), pageable);

		}

		/*
		 * using dates and storeId
		 */
		else if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() != null
				&& searchFilterVo.getStoreId() != null && searchFilterVo.getMobileNumber() == null) {
			LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
			LocalDateTime toTime = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getToDate());
			ledgerLogs = ledgerLogBookRepo
					.findByCustomerIdAndCreatedDateBetweenAndStoreIdAndAccountTypeOrderByLastModifiedDateAsc(
							searchFilterVo.getCustomerId(), fromTime, toTime, searchFilterVo.getStoreId(),
							searchFilterVo.getAccountType(), pageable);

		}
		/*
		 * using storeId
		 */

		else if (searchFilterVo.getStoreId() != null) {
			ledgerLogs = ledgerLogBookRepo.findByCustomerIdAndStoreIdAndAccountTypeOrderByCreatedDateDesc(
					searchFilterVo.getCustomerId(), searchFilterVo.getStoreId(), searchFilterVo.getAccountType(),
					pageable);
		}

		else
			ledgerLogs = ledgerLogBookRepo.findByCustomerIdAndAccountType(searchFilterVo.getCustomerId(),
					searchFilterVo.getAccountType(), pageable);

		if (ledgerLogs.hasContent()) {
			return ledgerLogs.map(ledgerLog -> ledgerLogBookMapper.entityToVo(ledgerLog));
		}

		return Page.empty();
	}

	@RabbitListener(queues = "payment_creditNotes_queue")
	public void paymentConfirmation(PaymentDetailsVo paymentDetails) {

		// AccountingBook entity =
		// accountingBookRepo.findByReferenceNumber(paymentDetails.getReferenceNumber());

		LedgerLogBook ledgerbook = ledgerLogBookRepo.findByReferenceNumber(paymentDetails.getReferenceNumber());

		if (ledgerbook != null) {
			ledgerbook.setPaymentId(paymentDetails.getRazorPayId());
			ledgerbook.setPaymentStatus(PaymentStatus.INITIATED);
			ledgerLogBookRepo.save(ledgerbook);

			log.info("save payment details for credit : " + ledgerbook.getReferenceNumber());
		}

	}

	@Override
	public Boolean paymentConfirmationFromRazorpay(String razorPayId, boolean payStatus) {
		if (payStatus) {
			// todo confirm payment success from razorpay
			LedgerLogBook ledgerLogBook = ledgerLogBookRepo.findByPaymentId(razorPayId);
			ledgerLogBook.setPaymentStatus(PaymentStatus.SUCCESS);

			ledgerLogBookRepo.save(ledgerLogBook);
			LedgerLogBookVo ledgerBookVo = calculateAmount(ledgerLogBook);
			log.info("update payment details for razorpay Id: " + razorPayId);

		} else {
			log.info("Payment failed for razoer pay id : " + razorPayId);
			LedgerLogBook ledgerLogBook = ledgerLogBookRepo.findByPaymentId(razorPayId);
			ledgerLogBook.setPaymentStatus(PaymentStatus.FAILED);
			ledgerLogBookRepo.save(ledgerLogBook);
			// LedgerLogBookVo ledgerBookVo = calculateAmount(ledgerLogBook);
		}
		return payStatus;

	}

}
