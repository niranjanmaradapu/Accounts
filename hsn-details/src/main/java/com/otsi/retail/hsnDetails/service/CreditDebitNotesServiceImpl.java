package com.otsi.retail.hsnDetails.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.otsi.retail.hsnDetails.exceptions.RecordNotFoundException;
import com.otsi.retail.hsnDetails.mapper.CreditDebitNotesMapper;
import com.otsi.retail.hsnDetails.model.CreditDebitNotes;
import com.otsi.retail.hsnDetails.repo.CreditDebitNotesRepo;
import com.otsi.retail.hsnDetails.vo.CreditDebitNotesVo;
import com.otsi.retail.hsnDetails.vo.UpdateCreditRequest;

@Component
public class CreditDebitNotesServiceImpl implements CreditDebitNotesService {

	private Logger log = LoggerFactory.getLogger(CreditDebitNotesServiceImpl.class);

	@Autowired
	private CreditDebitNotesRepo creditDebitNotesRepo;

	@Autowired
	private CreditDebitNotesMapper creditDebitNotesMapper;

	@Override
	public String saveCreditDebitNotes(CreditDebitNotesVo debitNotesVo) {
		boolean flag = true;
		CreditDebitNotes cred = creditDebitNotesRepo.findByMobileNumberAndCustomerIdAndFlag(
				debitNotesVo.getMobileNumber(), debitNotesVo.getCustomerId(), flag);
		if (cred != null) {
			cred.setFlag(false);
			creditDebitNotesRepo.save(cred);

			CreditDebitNotes cd = creditDebitNotesMapper.VoToEntity(debitNotesVo);
			cd.setFlag(true);
			cd.setActualAmount(debitNotesVo.getActualAmount() + cred.getActualAmount());
			cd.setStatus("credited");
			creditDebitNotesRepo.save(cd);
		} else {

			CreditDebitNotes cd = creditDebitNotesMapper.VoToEntity(debitNotesVo);
			if (debitNotesVo.getCreditDebit().equals("C")) {
				cd.setStatus("credited");
			} else if (debitNotesVo.getCreditDebit().equals("D")) {
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
		if (mob != null && !mob.isEmpty()) {
			mob.stream().forEach(m -> {
				if (m.getCreditDebit().equals("C")) {
					CreditDebitNotesVo vo = creditDebitNotesMapper.EntityToVo(m);
					voList.add(vo);
				}
			});
			return voList;
		} else
			log.error("no record found with mobileNumber and customerId:" + mobileNumber + "and" + customerId);
		throw new RecordNotFoundException(
				"no record found with mobileNumber and customerId:" + mobileNumber + "and" + customerId);

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
	public List<CreditDebitNotesVo> saveListCreditDebitNotes(List<CreditDebitNotesVo> debitNotesVos) {
		log.debug("debugging saveListDebitNotes:" + debitNotesVos);
		List<CreditDebitNotes> voList = creditDebitNotesMapper.VoToEntity(debitNotesVos);
		debitNotesVos = creditDebitNotesMapper.EntityToVo(creditDebitNotesRepo.saveAll(voList));
		log.warn("we are testing is saving all credit-debit notes");
		log.info("after saving all credit-debit notes:" + debitNotesVos.toString());
		return debitNotesVos;
	}

	@Override
	public String updateCreditDebitNotes(UpdateCreditRequest notesVo) {
		log.debug("debugging updateCreditDebitNotes:" + notesVo);
		boolean flag = true;
		if (notesVo.getCreditDebit().equals("C")) {
			CreditDebitNotes cred = creditDebitNotesRepo.findByMobileNumberAndStoreIdAndCreditDebitAndFlag(
					notesVo.getMobileNumber(), notesVo.getStoreId(), notesVo.getCreditDebit(), flag);

			if ((cred.getMobileNumber().equals(notesVo.getMobileNumber()))
					&& (cred.getStoreId() == notesVo.getStoreId())
					&& (cred.getCreditDebit().equals(notesVo.getCreditDebit()))) {

				if (cred.getActualAmount() >= notesVo.getAmount()) {
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
					credVo.setTransactionAmount(notesVo.getAmount());
					credVo.setActualAmount(Math.abs(cred.getActualAmount() - notesVo.getAmount()));
					credVo.setStatus("used");
					credVo.setComments(notesVo.getAmount() + " used ");
					CreditDebitNotes credMapper = creditDebitNotesMapper.VoToEntity(credVo);
					creditDebitNotesRepo.save(credMapper);
				}

			}

		} else if (notesVo.getCreditDebit().equals("D")) {
			CreditDebitNotes deb = new CreditDebitNotes();
			deb.setActualAmount(notesVo.getAmount());
			deb.setStoreId(notesVo.getStoreId());
			deb.setCreditDebit("D");
			deb.setComments("debit");
			deb.setCreatedDate(LocalDate.now());
			deb.setLastModifiedDate(LocalDate.now());
			deb.setMobileNumber(notesVo.getMobileNumber());
			creditDebitNotesRepo.save(deb);
		}

		log.warn("we are checking if notes is updated...");
		log.info("notes details updated successfully with the given id");
		return "Notes updated successfully";
	}

	@Override
	public String updateNotes(CreditDebitNotesVo notesVo) {
		log.debug("debugging updateCreditDebitNotes:" + notesVo);
		Optional<CreditDebitNotes> notesOpt = creditDebitNotesRepo.findByCreditDebitId(notesVo.getCreditDebitId());

		// if id is not present,it will throw custom exception "RecordNotFoundException"
		if (!notesOpt.isPresent()) {
			log.error("Record Not Found with id:" + notesOpt.get());
			throw new RecordNotFoundException("Record Not Found with id:" + notesOpt.get());
		}

		CreditDebitNotes notes = creditDebitNotesMapper.VoToEntity(notesVo);
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
	public List<CreditDebitNotesVo> getAllCreditNotes(CreditDebitNotesVo vo) {
		List<CreditDebitNotes> creditNotes = new ArrayList<>();
		String creditDebit = "C";
		boolean flag = true;
		List<CreditDebitNotes> storeOpt = creditDebitNotesRepo.findAllByStoreIdAndCreditDebitAndFlag(vo.getStoreId(),
				creditDebit, flag);

		/*
		 * using dates and storeId
		 */
		if (vo.getFromDate() != null && vo.getToDate() != null && vo.getStoreId() != null
				&& vo.getMobileNumber() == "") {

			if (storeOpt != null) {

				creditNotes = creditDebitNotesRepo
						.findByCreatedDateBetweenAndStoreIdAndCreditDebitAndFlagOrderByLastModifiedDateAsc(
								vo.getFromDate(), vo.getToDate(), vo.getStoreId(), creditDebit, flag);

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
		else if (vo.getFromDate() != null && vo.getToDate() != null && vo.getMobileNumber() != null
				&& vo.getStoreId() != null) {

			if (storeOpt != null) {
				List<CreditDebitNotes> mobileOpt = creditDebitNotesRepo.findAllByMobileNumber(vo.getMobileNumber());
				if (mobileOpt != null) {
					creditNotes = creditDebitNotesRepo
							.findByCreatedDateBetweenAndMobileNumberAndStoreIdAndCreditDebitAndFlagOrderByLastModifiedDateAsc(
									vo.getFromDate(), vo.getToDate(), vo.getMobileNumber(), vo.getStoreId(),
									creditDebit, flag);
				} else {
					log.error("No record found with given productItemId");
					throw new RecordNotFoundException("No record found with given productItemId");
				}
			}
		}

		/*
		 * using storeId
		 */
		else if (vo.getFromDate() == null && vo.getToDate() == null && vo.getMobileNumber() == ""
				&& vo.getStoreId() != null) {
			if (storeOpt.isEmpty()) {
				log.error("retail record is not found with storeId:" + vo.getStoreId());
				throw new RecordNotFoundException("retail record is not found with storeId:" + vo.getStoreId());
			}
			if (vo.getStoreId() != null) {
				List<CreditDebitNotesVo> creditList = creditDebitNotesMapper.EntityToVo(storeOpt);
				return creditList;
			} else {
				throw new RecordNotFoundException("No record found with storeId");
			}
		}
		/*
		 * using mobile number and storeId
		 */
		else if (vo.getFromDate() == null && vo.getToDate() == null && vo.getMobileNumber() != null
				&& vo.getStoreId() != null) {

			if (storeOpt != null) {
				CreditDebitNotes mobileOpt = creditDebitNotesRepo.findByMobileNumberAndFlag(vo.getMobileNumber(), flag);
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
	public List<CreditDebitNotesVo> getAllDebitNotes(CreditDebitNotesVo vo) {
		List<CreditDebitNotes> debitNotes = new ArrayList<>();
		String creditDebit = "D";
		boolean flag = true;
		List<CreditDebitNotes> storeOpt = creditDebitNotesRepo.findAllByStoreIdAndCreditDebitAndFlag(vo.getStoreId(),
				creditDebit, flag);

		/*
		 * using dates and storeId
		 */
		if (vo.getFromDate() != null && vo.getToDate() != null && vo.getStoreId() != null
				&& vo.getMobileNumber() == "") {

			if (storeOpt != null) {

				debitNotes = creditDebitNotesRepo
						.findByCreatedDateBetweenAndStoreIdAndCreditDebitAndFlagOrderByLastModifiedDateAsc(
								vo.getFromDate(), vo.getToDate(), vo.getStoreId(), creditDebit, flag);

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
		else if (vo.getFromDate() != null && vo.getToDate() != null && vo.getMobileNumber() != null
				&& vo.getStoreId() != null) {

			if (storeOpt != null) {
				List<CreditDebitNotes> mobileOpt = creditDebitNotesRepo.findAllByMobileNumber(vo.getMobileNumber());
				if (mobileOpt != null) {
					debitNotes = creditDebitNotesRepo
							.findByCreatedDateBetweenAndMobileNumberAndStoreIdAndCreditDebitAndFlagOrderByLastModifiedDateAsc(
									vo.getFromDate(), vo.getToDate(), vo.getMobileNumber(), vo.getStoreId(),
									creditDebit, flag);
				} else {
					log.error("No record found with given productItemId");
					throw new RecordNotFoundException("No record found with given productItemId");
				}
			}
		}

		/*
		 * using storeId
		 */
		else if (vo.getFromDate() == null && vo.getToDate() == null && vo.getMobileNumber() == ""
				&& vo.getStoreId() != null) {
			if (storeOpt.isEmpty()) {
				log.error("retail record is not found with storeId:" + vo.getStoreId());
				throw new RecordNotFoundException("retail record is not found with storeId:" + vo.getStoreId());
			}
			if (vo.getStoreId() != null) {
				List<CreditDebitNotesVo> debitList = creditDebitNotesMapper.EntityToVo(storeOpt);
				return debitList;
			} else {
				throw new RecordNotFoundException("No record found with storeId");
			}
		}
		/*
		 * using mobile number and storeId
		 */
		else if (vo.getFromDate() == null && vo.getToDate() == null && vo.getMobileNumber() != null
				&& vo.getStoreId() != null) {

			if (storeOpt != null) {
				CreditDebitNotes mobileOpt = creditDebitNotesRepo.findByMobileNumberAndFlag(vo.getMobileNumber(), flag);
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
		log.warn("we are checking if credit notes is fetching...");
		log.info("after fetching all credit notes  details:" + debitList.toString());
		return debitList;
	}

}
