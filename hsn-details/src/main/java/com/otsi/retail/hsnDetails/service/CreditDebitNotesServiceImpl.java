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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
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
import com.otsi.retail.hsnDetails.repository.AccountingBookRepository;
import com.otsi.retail.hsnDetails.repository.CreditDebitNotesRepository;
import com.otsi.retail.hsnDetails.repository.LedgerLogBookRepository;
import com.otsi.retail.hsnDetails.util.DateConverters;
import com.otsi.retail.hsnDetails.vo.AccountingBookVO;
import com.otsi.retail.hsnDetails.vo.CreditDebitNotesVO;
import com.otsi.retail.hsnDetails.vo.GetUserRequestVO;
import com.otsi.retail.hsnDetails.vo.LedgerLogBookVO;
import com.otsi.retail.hsnDetails.vo.PaymentDetailsVO;
import com.otsi.retail.hsnDetails.vo.SearchFilterVO;
import com.otsi.retail.hsnDetails.vo.UpdateCreditRequest;
import com.otsi.retail.hsnDetails.vo.UserDetailsVO;

/**
 * @author vasavi
 *
 */
@Component
public class CreditDebitNotesServiceImpl implements CreditDebitNotesService {

	private Logger log = LoggerFactory.getLogger(CreditDebitNotesServiceImpl.class);

	@Autowired
	private CreditDebitNotesRepository CreditDebitNotesRepository;

	@Autowired
	private CreditDebitNotesMapper creditDebitNotesMapper;

	@Autowired
	private AccountingBookMapper accountingBookMapper;

	@Autowired
	private LedgerLogBookMapper ledgerLogBookMapper;

	@Autowired
	private AccountingBookRepository AccountingBookRepository;

	@Autowired
	private LedgerLogBookRepository ledgerLogBookRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Config config;

	@Override
	public String saveCreditDebitNotes(CreditDebitNotesVO CreditDebitNotesVO) {
		boolean flag = true;
		CreditDebitNotes cred = CreditDebitNotesRepository.findByMobileNumberAndCustomerIdAndFlag(
				CreditDebitNotesVO.getMobileNumber(), CreditDebitNotesVO.getCustomerId(), flag);
		if (cred != null) {
			cred.setFlag(false);
			CreditDebitNotesRepository.save(cred);

			CreditDebitNotes cd = creditDebitNotesMapper.VoToEntity(CreditDebitNotesVO);
			cd.setFlag(true);
			cd.setActualAmount(CreditDebitNotesVO.getActualAmount() + cred.getActualAmount());
			cd.setStatus("credited");
			CreditDebitNotesRepository.save(cd);
		} else {

			CreditDebitNotes cd = creditDebitNotesMapper.VoToEntity(CreditDebitNotesVO);
			if (CreditDebitNotesVO.getCreditDebit().equals("C")) {
				cd.setStatus("credited");
			} else if (CreditDebitNotesVO.getCreditDebit().equals("D")) {
				cd.setStatus("debited");
			}
			CreditDebitNotes notesSave = CreditDebitNotesRepository.save(cd);
		}
		log.warn("we are checking if notes is saved...");
		log.info("note details  saved successfully");
		return "note details  saved successfully";

	}

	@Override
	public List<CreditDebitNotesVO> getCreditNotes(String mobileNumber, Long customerId) {
		log.debug("debugging getMobileNumber:" + mobileNumber);
		List<CreditDebitNotesVO> voList = new ArrayList<>();
		boolean flag = true;
		List<CreditDebitNotes> mob = CreditDebitNotesRepository.findAllByMobileNumberAndCustomerIdAndFlag(mobileNumber,
				customerId, flag);
		List<CreditDebitNotes> mob1 = CreditDebitNotesRepository.findByCustomerIdAndFlag(customerId, flag);
		if (mob1 != null && !mob1.isEmpty()) {
			mob1.stream().forEach(m -> {
				if (m.getCustomerId() != null) {
					CreditDebitNotesVO vo = creditDebitNotesMapper.EntityToVo(m);
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
		List<CreditDebitNotes> vo = CreditDebitNotesRepository.findAll();
		log.warn("we are testing is fetching all credit-debit notes");
		log.info("after fetching all credit-debitnotes:" + vo.toString());
		return vo;
	}

	@Override
	public List<CreditDebitNotesVO> saveListCreditDebitNotes(List<CreditDebitNotesVO> CreditDebitNotesVOs) {
		log.debug("debugging saveListDebitNotes:" + CreditDebitNotesVOs);
		List<CreditDebitNotes> voList = creditDebitNotesMapper.VoToEntity(CreditDebitNotesVOs);
		CreditDebitNotesVOs = creditDebitNotesMapper.EntityToVo(CreditDebitNotesRepository.saveAll(voList));
		log.warn("we are testing is saving all credit-debit notes");
		log.info("after saving all credit-debit notes:" + CreditDebitNotesVOs.toString());
		return CreditDebitNotesVOs;
	}

	@Override
	public String updateCreditDebitNotes(UpdateCreditRequest updateCreditRequest) {
		log.debug("debugging updateCreditDebitNotes:" + updateCreditRequest);
		boolean flag = true;
		if (updateCreditRequest.getCreditDebit().equals("C")) {
			CreditDebitNotes cred = CreditDebitNotesRepository.findByMobileNumberAndStoreIdAndCreditDebitAndFlag(
					updateCreditRequest.getMobileNumber(), updateCreditRequest.getStoreId(),
					updateCreditRequest.getCreditDebit(), flag);

			if ((cred.getMobileNumber().equals(updateCreditRequest.getMobileNumber()))
					&& (cred.getStoreId() == updateCreditRequest.getStoreId())
					&& (cred.getCreditDebit().equals(updateCreditRequest.getCreditDebit()))) {

				if (cred.getActualAmount() >= updateCreditRequest.getAmount()) {
					cred.setFlag(false);
					CreditDebitNotesRepository.save(cred);
					CreditDebitNotesVO credVo = new CreditDebitNotesVO();
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
					CreditDebitNotesRepository.save(credMapper);
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
			CreditDebitNotesRepository.save(deb);
		}

		log.warn("we are checking if notes is updated...");
		log.info("notes details updated successfully with the given id");
		return "Notes updated successfully";
	}

	@Override
	public String updateNotes(CreditDebitNotesVO CreditDebitNotesVO) {
		log.debug("debugging updateCreditDebitNotes:" + CreditDebitNotesVO);
		Optional<CreditDebitNotes> notesOpt = CreditDebitNotesRepository
				.findByCreditDebitId(CreditDebitNotesVO.getCreditDebitId());

		// if id is not present,it will throw custom exception "RecordNotFoundException"
		if (!notesOpt.isPresent()) {
			log.error("Record Not Found with id:" + notesOpt.get());
			throw new RecordNotFoundException("Record Not Found with id:" + notesOpt.get());
		}

		CreditDebitNotes notes = creditDebitNotesMapper.VoToEntity(CreditDebitNotesVO);
		CreditDebitNotes notesSave = CreditDebitNotesRepository.save(notes);
		CreditDebitNotesVO notesUpdate = creditDebitNotesMapper.EntityToVo(notesSave);
		log.warn("we are checking if notes is updated...");
		log.info("notes details updated successfully with the given id:" + notesUpdate);
		return "Notes updated successfully:" + notesUpdate.getMobileNumber();
	}

	@Override
	public String deleteCreditDebitNotes(Long creditDebitId) {
		log.debug("debugging deleteCreditDebitNotes:" + creditDebitId);
		Optional<CreditDebitNotes> notes = CreditDebitNotesRepository.findById(creditDebitId);
		if (!(notes.isPresent())) {
			log.error("Record Not Found with id:" + notes.get());
			throw new RecordNotFoundException("Record Not Found with id:" + notes.get());
		}
		CreditDebitNotesRepository.delete(notes.get());
		log.warn("we are checking if notes data is fetching by id...");
		log.info("after deleting notes:" + creditDebitId);
		return "deleted notes successfully:" + creditDebitId;
	}

	@Override
	public List<CreditDebitNotesVO> getAllCreditNotes(CreditDebitNotesVO creditNotesVo) {
		List<CreditDebitNotes> creditNotes = new ArrayList<>();
		String creditDebit = "C";
		boolean flag = true;
		List<CreditDebitNotes> storeOpt = CreditDebitNotesRepository
				.findAllByStoreIdAndCreditDebitAndFlag(creditNotesVo.getStoreId(), creditDebit, flag);

		/*
		 * using dates and storeId
		 */
		if (creditNotesVo.getFromDate() != null && creditNotesVo.getToDate() != null
				&& creditNotesVo.getStoreId() != null && creditNotesVo.getMobileNumber() == "") {

			if (storeOpt != null) {

				creditNotes = CreditDebitNotesRepository
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
				List<CreditDebitNotes> mobileOpt = CreditDebitNotesRepository
						.findAllByMobileNumber(creditNotesVo.getMobileNumber());
				if (mobileOpt != null) {
					creditNotes = CreditDebitNotesRepository
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
				List<CreditDebitNotesVO> creditList = creditDebitNotesMapper.EntityToVo(storeOpt);
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
				CreditDebitNotes mobileOpt = CreditDebitNotesRepository
						.findByMobileNumberAndFlag(creditNotesVo.getMobileNumber(), flag);
				if (mobileOpt == null) {
					throw new RecordNotFoundException("barcode record is not found");

				} else {

					creditNotes.add(mobileOpt);
					List<CreditDebitNotesVO> creditList = creditDebitNotesMapper.EntityToVo(creditNotes);
					return creditList;

				}
			} else {
				throw new RecordNotFoundException("No record found with storeId");
			}
		}

		else {
			List<CreditDebitNotes> creditNotes1 = CreditDebitNotesRepository.findAll();
			List<CreditDebitNotesVO> creditList1 = creditDebitNotesMapper.EntityToVo(creditNotes1);
			return creditList1;
		}

		List<CreditDebitNotesVO> creditList = creditDebitNotesMapper.EntityToVo(creditNotes);
		log.warn("we are checking if credit notes is fetching...");
		log.info("after fetching all credit notes  details:" + creditList.toString());
		return creditList;
	}

	@Override
	public List<CreditDebitNotesVO> getAllDebitNotes(CreditDebitNotesVO debitNotesVo) {
		List<CreditDebitNotes> debitNotes = new ArrayList<>();
		String creditDebit = "D";
		boolean flag = true;
		List<CreditDebitNotes> storeOpt = CreditDebitNotesRepository
				.findAllByStoreIdAndCreditDebitAndFlag(debitNotesVo.getStoreId(), creditDebit, flag);

		/*
		 * using dates and storeId
		 */
		if (debitNotesVo.getFromDate() != null && debitNotesVo.getToDate() != null && debitNotesVo.getStoreId() != null
				&& debitNotesVo.getMobileNumber() == "") {

			if (storeOpt != null) {

				debitNotes = CreditDebitNotesRepository
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
				List<CreditDebitNotes> mobileOpt = CreditDebitNotesRepository
						.findAllByMobileNumber(debitNotesVo.getMobileNumber());
				if (mobileOpt != null) {
					debitNotes = CreditDebitNotesRepository
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
				List<CreditDebitNotesVO> debitList = creditDebitNotesMapper.EntityToVo(storeOpt);
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
				CreditDebitNotes mobileOpt = CreditDebitNotesRepository
						.findByMobileNumberAndFlag(debitNotesVo.getMobileNumber(), flag);
				if (mobileOpt == null) {
					throw new RecordNotFoundException("barcode record is not found");

				} else {

					debitNotes.add(mobileOpt);
					List<CreditDebitNotesVO> creditList = creditDebitNotesMapper.EntityToVo(debitNotes);
					return creditList;

				}
			} else {
				throw new RecordNotFoundException("No record found with storeId");
			}
		}

		else {
			List<CreditDebitNotes> debitNotes1 = CreditDebitNotesRepository.findAll();
			List<CreditDebitNotesVO> debitList1 = creditDebitNotesMapper.EntityToVo(debitNotes1);
			return debitList1;
		}

		List<CreditDebitNotesVO> debitList = creditDebitNotesMapper.EntityToVo(debitNotes);
		log.warn("we are checking if debit notes is fetching...");
		log.info("after fetching all debit notes  details:" + debitList.toString());
		return debitList;
	}

	public UserDetailsVO getUserDetailsFromURM(@RequestParam String mobileNumber) {

		GetUserRequestVO userRequestVo = new GetUserRequestVO();
		String phoneNumber = "+" + mobileNumber.trim();
		userRequestVo.setPhoneNo(phoneNumber);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GetUserRequestVO> entity = new HttpEntity<>(userRequestVo, headers);

		ResponseEntity<?> returnSlipListResponse = restTemplate.exchange(
				config.getGetCustomerDetailsFromURM() + "?mobileNumber=" + phoneNumber, HttpMethod.GET, entity,
				GateWayResponse.class);

		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		GateWayResponse<?> gatewayResponse = mapper.convertValue(returnSlipListResponse.getBody(),
				GateWayResponse.class);

		UserDetailsVO UserDetailsVO = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<UserDetailsVO>() {
				});

		return UserDetailsVO;

	}

	public List<UserDetailsVO> getCustomersForGivenIds(List<Long> userIds) throws URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		URI uri = UriComponentsBuilder.fromUri(new URI(config.getCustomerDetails())).build().encode().toUri();

		HttpEntity<List<Long>> request = new HttpEntity<List<Long>>(userIds, headers);

		ResponseEntity<?> newsaleResponse = restTemplate.exchange(uri, HttpMethod.POST, request, GateWayResponse.class);

		System.out.println("Received Request to getUserDetails:" + newsaleResponse);
		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(newsaleResponse.getBody(), GateWayResponse.class);

		List<UserDetailsVO> UserDetailsVO = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<List<UserDetailsVO>>() {
				});
		return UserDetailsVO;

	}

	@Override
	public LedgerLogBookVO saveNotes(LedgerLogBookVO LedgerLogBookVO) {
		LedgerLogBook ledgerLogBook = null;
		// to accounting type credit
		if (LedgerLogBookVO.getAccountType().equals(AccountType.CREDIT)) {
			ledgerLogBook = processCreditAccounting(LedgerLogBookVO);

		}
		// to do accounting type debit
		else if (LedgerLogBookVO.getAccountType().equals(AccountType.DEBIT)) {
			ledgerLogBook = processDebitAccounting(LedgerLogBookVO);

		}

		LedgerLogBookVO = ledgerLogBookMapper.entityToVo(ledgerLogBook);
		return LedgerLogBookVO;

	}

	private LedgerLogBook processCreditAccounting(LedgerLogBookVO LedgerLogBookVO) {
		LedgerLogBook ledgerLogBook = ledgerLogBookMapper.voToEntity(LedgerLogBookVO);
		// when customer credit note is created
		if (LedgerLogBookVO.getTransactionType().equals(AccountType.CREDIT)) {
			if (LedgerLogBookVO.getPaymentType().equals(PaymentType.Cash)
					|| LedgerLogBookVO.getPaymentType().equals(PaymentType.RTSlip)) {
				ledgerLogBook.setPaymentStatus(PaymentStatus.SUCCESS);
				LedgerLogBookVO = calculateAmount(ledgerLogBook);
			}
		}

		else if (LedgerLogBookVO.getTransactionType().equals(AccountType.DEBIT)) {
			// when the customer used the credit amount
			UserDetailsVO UserDetailsVO = getUserDetailsFromURM(LedgerLogBookVO.getMobileNumber());
			if (UserDetailsVO != null) {
				AccountingBook accountingBook = AccountingBookRepository
						.findByCustomerIdAndAccountType(UserDetailsVO.getId(), LedgerLogBookVO.getAccountType());

				ledgerLogBook.setPaymentStatus(PaymentStatus.DEBIT);
				ledgerLogBook.setReferenceNumber("DB" + RandomStringUtils.randomAlphanumeric(10));
				ledgerLogBook.setCustomerId(UserDetailsVO.getId());
				ledgerLogBook.setAccountingBookId(accountingBook.getAccountingBookId());
				// to update used amount in accounting book
				if (accountingBook.getUsedAmount() == null) {
					accountingBook.setUsedAmount(LedgerLogBookVO.getAmount());
				} else {
					accountingBook.setUsedAmount(accountingBook.getUsedAmount() + LedgerLogBookVO.getAmount());
					accountingBook.setLastModifiedDate(LocalDateTime.now());
				}
				AccountingBookRepository.save(accountingBook);
			}

		}
		ledgerLogBook = ledgerLogBookRepository.save(ledgerLogBook);

		return ledgerLogBook;
	}

	private LedgerLogBook processDebitAccounting(LedgerLogBookVO LedgerLogBookVO) {
		LedgerLogBook ledgerLogBook = ledgerLogBookMapper.voToEntity(LedgerLogBookVO);
		// when the customer debit note is created from sale
		if (LedgerLogBookVO.getTransactionType().equals(AccountType.DEBIT)) {
			ledgerLogBook.setPaymentStatus(PaymentStatus.DEBIT);
			LedgerLogBookVO = calculateAmount(ledgerLogBook);
		}

		else if (LedgerLogBookVO.getTransactionType().equals(AccountType.CREDIT)) {
			// UserDetailsVO UserDetailsVO =
			// getUserDetailsFromURM(LedgerLogBookVO.getMobileNumber());
			// if (UserDetailsVO != null) {
			AccountingBook accountingBook = AccountingBookRepository.findByCustomerIdAndStoreIdAndAccountType(
					LedgerLogBookVO.getCustomerId(), LedgerLogBookVO.getStoreId(), LedgerLogBookVO.getAccountType());
			if (accountingBook.getAmount() == 0) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Due for this Customer");
			}
			if (LedgerLogBookVO.getPaymentType().equals(PaymentType.Cash)) {
				ledgerLogBook.setPaymentStatus(PaymentStatus.SUCCESS);
			}
			ledgerLogBook.setAccountingBookId(accountingBook.getAccountingBookId());
			ledgerLogBook.setReferenceNumber("CR" + RandomStringUtils.randomAlphanumeric(10));
			ledgerLogBook.setCustomerId(accountingBook.getCustomerId());
			// to update deducted amount in accounting book
			accountingBook.setAmount(Math.abs(LedgerLogBookVO.getAmount() - accountingBook.getAmount()));
			accountingBook.setLastModifiedDate(LocalDateTime.now());
			AccountingBookRepository.save(accountingBook);
			// }

		}
		ledgerLogBook = ledgerLogBookRepository.save(ledgerLogBook);
		return ledgerLogBook;
	}

	private LedgerLogBookVO calculateAmount(LedgerLogBook ledgerLogBook) {
		AccountingBook accountingBookCustomer = AccountingBookRepository
				.findByCustomerIdAndAccountType(ledgerLogBook.getCustomerId(), ledgerLogBook.getAccountType());
		if (accountingBookCustomer != null) {
			accountingBookCustomer.setAmount(accountingBookCustomer.getAmount() + ledgerLogBook.getAmount());
			accountingBookCustomer.setLastModifiedDate(LocalDateTime.now());
			AccountingBook accountingBookSave = AccountingBookRepository.save(accountingBookCustomer);
			ledgerLogBook.setAccountingBookId(accountingBookSave.getAccountingBookId());

		} else {
			AccountingBook accountingBook = new AccountingBook();
			accountingBook.setAmount(ledgerLogBook.getAmount());
			accountingBook.setAccountType(ledgerLogBook.getAccountType());
			accountingBook.setCustomerId(ledgerLogBook.getCustomerId());
			accountingBook.setStoreId(ledgerLogBook.getStoreId());
			AccountingBook accountingBookSave = AccountingBookRepository.save(accountingBook);
			ledgerLogBook.setAccountingBookId(accountingBookSave.getAccountingBookId());
		}
		LedgerLogBook ledgerLogBookSave = ledgerLogBookRepository.save(ledgerLogBook);
		LedgerLogBookVO LedgerLogBookVO = ledgerLogBookMapper.entityToVo(ledgerLogBookSave);
		return LedgerLogBookVO;

	}

	@Override
	public List<AccountingBookVO> getNotes(AccountType accountType, Long storeId) {
		List<AccountingBook> accountingBookOpt = AccountingBookRepository.findByAccountTypeAndStoreId(accountType,
				storeId);
		if (accountingBookOpt == null) {
			log.error("accounting book id  is Not Found:" + accountingBookOpt);
			throw new RecordNotFoundException("accounting book id  is Not Found:" + accountingBookOpt);
		}
		List<AccountingBookVO> accountingBookVos = accountingBookMapper.entityToVo(accountingBookOpt);
		return accountingBookVos;
	}

	/*
	 * @Override public String delete(Long accountingBookId) {
	 * Optional<AccountingBook> accountingBookOpt =
	 * AccountingBookRepository.findByAccountingBookId(accountingBookId); if
	 * (!(accountingBookOpt.isPresent())) {
	 * log.error("accounting book id  is Not Found:" + accountingBookOpt); throw new
	 * RecordNotFoundException("accounting book id  is Not Found:" +
	 * accountingBookOpt); }
	 * AccountingBookRepository.delete(accountingBookOpt.get()); return ""; }
	 */

	@Override
	public LedgerLogBookVO update(LedgerLogBookVO LedgerLogBookVO) {

		LedgerLogBook ledBook = ledgerLogBookRepository.findByLedgerLogBookId(LedgerLogBookVO.getLedgerLogBookId());
		if (ledBook == null) {
			throw new RecordNotFoundException("record not found with id:" + LedgerLogBookVO.getLedgerLogBookId());
		}
		AccountingBook accountingBookCustomer = AccountingBookRepository.findByCustomerIdAndStoreIdAndAccountType(
				LedgerLogBookVO.getCustomerId(), LedgerLogBookVO.getStoreId(), LedgerLogBookVO.getAccountType());
		LedgerLogBook ledgerLogBook = ledgerLogBookMapper.voToEntityUpdate(ledBook, LedgerLogBookVO);
		LedgerLogBook ledgerLogBookSave = ledgerLogBookRepository.save(ledgerLogBook);

		if (accountingBookCustomer != null) {
			Long paymentValue = 0L;
			List<LedgerLogBook> ledgerLogAccount = ledgerLogBookRepository
					.findByAccountingBookId(ledBook.getAccountingBookId());
			paymentValue = ledgerLogAccount.stream().mapToLong(x -> x.getAmount()).sum();
			accountingBookCustomer.setAmount(paymentValue);
			AccountingBook accountingBookSave = AccountingBookRepository.save(accountingBookCustomer);
			ledgerLogBook.setAccountingBookId(accountingBookSave.getAccountingBookId());
		} else {
			AccountingBook accountingBook = new AccountingBook();
			accountingBook.setAccountingBookId(ledgerLogBookSave.getAccountingBookId());
			accountingBook.setAmount(LedgerLogBookVO.getAmount());
			accountingBook.setAccountType(LedgerLogBookVO.getAccountType());
			accountingBook.setCustomerId(LedgerLogBookVO.getCustomerId());
			accountingBook.setStoreId(LedgerLogBookVO.getStoreId());
			AccountingBook accountingBookSave = AccountingBookRepository.save(accountingBook);
			ledgerLogBook.setAccountingBookId(accountingBookSave.getAccountingBookId());
		}

		LedgerLogBookVO = ledgerLogBookMapper.entityToVo(ledgerLogBookSave);
		return LedgerLogBookVO;
	}

	@Override
	public Page<AccountingBookVO> getAllNotes(SearchFilterVO searchFilterVo, Pageable pageable) {
		Page<AccountingBook> accountingBooks = null;
		/*
		 * using from date and accountType and storeId
		 */
		if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() == null
				&& searchFilterVo.getStoreId() != null && searchFilterVo.getMobileNumber() == null
				&& searchFilterVo.getAccountType() != null) {
			LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
			LocalDateTime fromTime1 = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getFromDate());
			accountingBooks = AccountingBookRepository.findByCreatedDateBetweenAndStoreIdAndAccountType(fromTime,
					fromTime1, searchFilterVo.getStoreId(), searchFilterVo.getAccountType(), pageable);

		}

		/*
		 * using dates and accountType and storeId
		 */
		else if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() != null
				&& searchFilterVo.getStoreId() != null && searchFilterVo.getMobileNumber() == null
				&& searchFilterVo.getAccountType() != null) {
			LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
			LocalDateTime toTime = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getToDate());
			accountingBooks = AccountingBookRepository
					.findByLastModifiedDateBetweenAndStoreIdAndAccountTypeOrderByLastModifiedDateDesc(fromTime, toTime,
							searchFilterVo.getStoreId(), searchFilterVo.getAccountType(), pageable);

		}

		/*
		 * using dates and mobile number and accountType and storeId
		 */

		else if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() != null
				&& searchFilterVo.getMobileNumber() != null && searchFilterVo.getStoreId() != null
				&& searchFilterVo.getAccountType() != null) {

			UserDetailsVO UserDetailsVO = getUserDetailsFromURM(searchFilterVo.getMobileNumber());
			if (UserDetailsVO != null) {
				LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
				LocalDateTime toTime = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getToDate());
				accountingBooks = AccountingBookRepository
						.findByCreatedDateBetweenAndCustomerIdAndStoreIdAndAccountTypeOrderByLastModifiedDateAsc(
								fromTime, toTime, UserDetailsVO.getId(), searchFilterVo.getStoreId(),
								searchFilterVo.getAccountType(), pageable);
			}
		}

		/*
		 * using storeId and account type
		 */

		else if (searchFilterVo.getFromDate() == null && searchFilterVo.getToDate() == null
				&& searchFilterVo.getMobileNumber() == null && searchFilterVo.getStoreId() != null
				&& searchFilterVo.getAccountType() != null) {
			accountingBooks = AccountingBookRepository.findByStoreIdAndAccountTypeOrderByCreatedDateDesc(
					searchFilterVo.getStoreId(), searchFilterVo.getAccountType(), pageable);
		}

		/*
		 * using mobile number and accountType and storeId
		 */

		else if (searchFilterVo.getFromDate() == null && searchFilterVo.getToDate() == null
				&& searchFilterVo.getMobileNumber() != null && searchFilterVo.getStoreId() != null
				&& searchFilterVo.getAccountType() != null) {

			UserDetailsVO UserDetailsVO = getUserDetailsFromURM(searchFilterVo.getMobileNumber());
			if (UserDetailsVO != null) {
				accountingBooks = AccountingBookRepository.findByCustomerIdAndStoreIdAndAccountType(
						UserDetailsVO.getId(), searchFilterVo.getStoreId(), searchFilterVo.getAccountType(),
						pageable);
			}
		}
		if (accountingBooks != null && accountingBooks.hasContent()) {
			return accountingBooks.map(accounting -> accountMapToVo(accounting));
		} else
			return Page.empty();
	}

	private AccountingBookVO accountMapToVo(AccountingBook accountingBook) {
		AccountingBookVO accountingBookVo = accountingBookMapper.entityToVo(accountingBook);

		List<AccountingBook> customerIds = AccountingBookRepository
				.findAllByCustomerId(accountingBookVo.getCustomerId());
		List<Long> userIds = customerIds.stream().map(x -> x.getCustomerId()).distinct().collect(Collectors.toList());
		List<UserDetailsVO> UserDetailsVO = null;
		try {
			UserDetailsVO = getCustomersForGivenIds(userIds);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		if (UserDetailsVO != null) {
			UserDetailsVO.stream().forEach(x -> {
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
	public Page<LedgerLogBookVO> getAllLedgerLogs(SearchFilterVO searchFilterVo, Pageable pageable) {

		Page<LedgerLogBook> ledgerLogs;
		/*
		 * using from date and storeId
		 */
		if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() == null
				&& searchFilterVo.getStoreId() != null && searchFilterVo.getMobileNumber() == null) {
			LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
			LocalDateTime fromTime1 = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getFromDate());
			ledgerLogs = ledgerLogBookRepository.findByCustomerIdAndCreatedDateBetweenAndStoreIdAndAccountType(
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
			ledgerLogs = ledgerLogBookRepository
					.findByCustomerIdAndCreatedDateBetweenAndStoreIdAndAccountTypeOrderByLastModifiedDateAsc(
							searchFilterVo.getCustomerId(), fromTime, toTime, searchFilterVo.getStoreId(),
							searchFilterVo.getAccountType(), pageable);

		}
		/*
		 * using storeId
		 */

		else if (searchFilterVo.getStoreId() != null) {
			ledgerLogs = ledgerLogBookRepository.findByCustomerIdAndStoreIdAndAccountTypeOrderByCreatedDateDesc(
					searchFilterVo.getCustomerId(), searchFilterVo.getStoreId(), searchFilterVo.getAccountType(),
					pageable);
		}

		else
			ledgerLogs = ledgerLogBookRepository.findByCustomerIdAndAccountType(searchFilterVo.getCustomerId(),
					searchFilterVo.getAccountType(), pageable);

		if (ledgerLogs.hasContent()) {
			return ledgerLogs.map(ledgerLog -> ledgerLogBookMapper.entityToVo(ledgerLog));
		}

		return Page.empty();
	}

	@RabbitListener(queues = "payment_creditNotes_queue")
	public void paymentConfirmation(PaymentDetailsVO paymentDetails) {

		// AccountingBook entity =
		// AccountingBookRepository.findByReferenceNumber(paymentDetails.getReferenceNumber());

		LedgerLogBook ledgerbook = ledgerLogBookRepository.findByReferenceNumber(paymentDetails.getReferenceNumber());

		if (ledgerbook != null) {
			ledgerbook.setPaymentId(paymentDetails.getRazorPayId());
			ledgerbook.setPaymentStatus(PaymentStatus.INITIATED);
			ledgerLogBookRepository.save(ledgerbook);

			log.info("save payment details for credit : " + ledgerbook.getReferenceNumber());
		}

	}

	@Override
	public Boolean paymentConfirmationFromRazorpay(String razorPayId, boolean payStatus) {
		if (payStatus) {
			// todo confirm payment success from razorpay
			LedgerLogBook ledgerLogBook = ledgerLogBookRepository.findByPaymentId(razorPayId);
			ledgerLogBook.setPaymentStatus(PaymentStatus.SUCCESS);

			ledgerLogBookRepository.save(ledgerLogBook);
			LedgerLogBookVO ledgerBookVo = calculateAmount(ledgerLogBook);
			log.info("update payment details for razorpay Id: " + razorPayId);

		} else {
			log.info("Payment failed for razoer pay id : " + razorPayId);
			LedgerLogBook ledgerLogBook = ledgerLogBookRepository.findByPaymentId(razorPayId);
			ledgerLogBook.setPaymentStatus(PaymentStatus.FAILED);
			ledgerLogBookRepository.save(ledgerLogBook);
			// LedgerLogBookVO ledgerBookVo = calculateAmount(ledgerLogBook);
		}
		return payStatus;

	}

}
