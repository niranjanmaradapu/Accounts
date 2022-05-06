package com.otsi.retail.hsnDetails.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.otsi.retail.hsnDetails.config.Config;
import com.otsi.retail.hsnDetails.enums.AccountType;
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
import com.otsi.retail.hsnDetails.vo.SearchFilterVo;
import com.otsi.retail.hsnDetails.vo.UpdateCreditRequest;
import com.otsi.retail.hsnDetails.vo.UserDetailsVo;

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

	public List<UserDetailsVo> getUserDetailsFromURM(@RequestParam String MobileNumber, @RequestParam Long UserId) {

		// UserDetailsVo vo = new UserDetailsVo();
		GetUserRequestVo uvo = new GetUserRequestVo();
		uvo.setPhoneNo(MobileNumber);

		uvo.setId(UserId);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GetUserRequestVo> entity = new HttpEntity<>(uvo, headers);

		ResponseEntity<?> returnSlipListResponse = restTemplate.exchange(config.getGetCustomerDetailsFromURM(),
				HttpMethod.POST, entity, GateWayResponse.class);

		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		GateWayResponse<?> gatewayResponse = mapper.convertValue(returnSlipListResponse.getBody(),
				GateWayResponse.class);

		List<UserDetailsVo> vo = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<List<UserDetailsVo>>() {
				});

		return vo;

	}

	@Override
	public LedgerLogBookVo saveNotes(LedgerLogBookVo ledgerLogBookVo) {
		AccountingBook accountingBookCustomer = accountingBookRepo.findByCustomerIdAndStoreIdAndAccountType(
				ledgerLogBookVo.getCustomerId(), ledgerLogBookVo.getStoreId(), ledgerLogBookVo.getAccountType());
		LedgerLogBook ledgerLogBook = ledgerLogBookMapper.voToEntity(ledgerLogBookVo);
		if (accountingBookCustomer != null) {
			accountingBookCustomer.setAmount(accountingBookCustomer.getAmount() + ledgerLogBookVo.getAmount());
			accountingBookRepo.save(accountingBookCustomer);
		} else {
			AccountingBook accountingBook = new AccountingBook();
			accountingBook.setAmount(ledgerLogBookVo.getAmount());
			accountingBook.setAccountType(ledgerLogBookVo.getAccountType());
			accountingBook.setCustomerId(ledgerLogBookVo.getCustomerId());
			accountingBook.setStoreId(ledgerLogBookVo.getStoreId());
			AccountingBook accountingBookSave = accountingBookRepo.save(accountingBook);
			ledgerLogBook.setAccountingBookId(accountingBookSave.getAccountingBookId());
		}
		LedgerLogBook ledgerLogBookSave = ledgerLogBookRepo.save(ledgerLogBook);
		ledgerLogBookVo = ledgerLogBookMapper.entityToVo(ledgerLogBookSave);
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
			accountingBookCustomer.setAmount(ledgerLogBookVo.getAmount());
			accountingBookRepo.save(accountingBookCustomer);
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
	public List<AccountingBookVo> getAllNotes(SearchFilterVo searchFilterVo, AccountType accountType) {
		List<AccountingBook> accountingBooks = new ArrayList<>();
		/*
		 * using from date and storeId
		 */
		if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() == null
				&& searchFilterVo.getStoreId() != null && searchFilterVo.getMobileNumber() == null) {
			LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
			LocalDateTime fromTime1 = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getFromDate());
			accountingBooks = accountingBookRepo.findByCreatedDateBetweenAndStoreIdAndAccountType(fromTime, fromTime1,
					searchFilterVo.getStoreId(), accountType);

		}

		/*
		 * using dates and storeId
		 */
		else if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() != null
				&& searchFilterVo.getStoreId() != null && searchFilterVo.getMobileNumber() == null) {
			LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
			LocalDateTime toTime = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getToDate());
			accountingBooks = accountingBookRepo
					.findByCreatedDateBetweenAndStoreIdAndAccountTypeOrderByLastModifiedDateAsc(fromTime, toTime,
							searchFilterVo.getStoreId(), accountType);

		}

		/*
		 * using dates and mobile number and storeId
		 */

		else if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() != null
				&& searchFilterVo.getMobileNumber() != null && searchFilterVo.getStoreId() != null) {

			List<UserDetailsVo> uvo = getUserDetailsFromURM(searchFilterVo.getMobileNumber(), 0L);
			if (uvo != null) {
				List<Long> userIds = uvo.stream().map(x -> x.getUserId()).collect(Collectors.toList());
				LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
				LocalDateTime toTime = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getToDate());
				accountingBooks = accountingBookRepo
						.findByCreatedDateBetweenAndCustomerIdInAndStoreIdAndAccountTypeOrderByLastModifiedDateAsc(
								fromTime, toTime, userIds, searchFilterVo.getStoreId(), accountType);
			}
		}

		/*
		 * using storeId
		 */

		else if (searchFilterVo.getFromDate() == null && searchFilterVo.getToDate() == null
				&& searchFilterVo.getMobileNumber() == null && searchFilterVo.getStoreId() != null) {
			accountingBooks = accountingBookRepo.findByStoreIdAndAccountType(searchFilterVo.getStoreId(), accountType);
		}

		/*
		 * using mobile number and storeId
		 */

		else if (searchFilterVo.getFromDate() == null && searchFilterVo.getToDate() == null
				&& searchFilterVo.getMobileNumber() != null && searchFilterVo.getStoreId() != null) {

			List<UserDetailsVo> uvo = getUserDetailsFromURM(searchFilterVo.getMobileNumber(), 0L);
			if (uvo != null) {
				List<Long> userIds = uvo.stream().map(x -> x.getUserId()).collect(Collectors.toList());
				accountingBooks = accountingBookRepo.findByCustomerIdInAndStoreIdAndAccountType(userIds,
						searchFilterVo.getStoreId(), accountType);
			}
		}

		List<AccountingBookVo> notesList = accountingBookMapper.entityToVo(accountingBooks);
		return notesList;
	}

	@Override
	public List<LedgerLogBookVo> getAllLedgerLogs(SearchFilterVo searchFilterVo, AccountType accountType) {

		List<LedgerLogBook> ledgerLogs = new ArrayList<>();
		/*
		 * using from date and storeId
		 */
		if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() == null
				&& searchFilterVo.getStoreId() != null && searchFilterVo.getMobileNumber() == null) {
			LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
			LocalDateTime fromTime1 = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getFromDate());
			ledgerLogs = ledgerLogBookRepo.findByCreatedDateBetweenAndStoreIdAndAccountType(fromTime, fromTime1,
					searchFilterVo.getStoreId(), accountType);

		}

		/*
		 * using dates and storeId
		 */
		else if (searchFilterVo.getFromDate() != null && searchFilterVo.getToDate() != null
				&& searchFilterVo.getStoreId() != null && searchFilterVo.getMobileNumber() == null) {
			LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(searchFilterVo.getFromDate());
			LocalDateTime toTime = DateConverters.convertToLocalDateTimeMax(searchFilterVo.getToDate());
			ledgerLogs = ledgerLogBookRepo.findByCreatedDateBetweenAndStoreIdAndAccountTypeOrderByLastModifiedDateAsc(
					fromTime, toTime, searchFilterVo.getStoreId(), accountType);

		}
		/*
		 * using storeId
		 */

		else if (searchFilterVo.getFromDate() == null && searchFilterVo.getToDate() == null
				&& searchFilterVo.getMobileNumber() == null && searchFilterVo.getStoreId() != null) {
			ledgerLogs = ledgerLogBookRepo.findByStoreIdAndAccountType(searchFilterVo.getStoreId(), accountType);
		}

		List<LedgerLogBookVo> ledgerList = ledgerLogBookMapper.entityToVo(ledgerLogs);
		return ledgerList;
	}

}
