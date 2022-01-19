package com.otsi.retail.hsnDetails.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.otsi.retail.hsnDetails.exceptions.RecordNotFoundException;
import com.otsi.retail.hsnDetails.mapper.CreditDebitNotesMapper;
import com.otsi.retail.hsnDetails.model.CreditDebitNotes;
import com.otsi.retail.hsnDetails.repo.CreditDebitNotesRepo;
import com.otsi.retail.hsnDetails.vo.CreditDebitNotesVo;

@Component
public class CreditDebitNotesServiceImpl implements CreditDebitNotesService {

	private Logger log = LoggerFactory.getLogger(CreditDebitNotesServiceImpl.class);

	@Autowired
	private CreditDebitNotesRepo creditDebitNotesRepo;

	@Autowired
	private CreditDebitNotesMapper creditDebitNotesMapper;

	@Override
	public String saveCreditDebitNotes(CreditDebitNotesVo debitNotesVo) {
		CreditDebitNotes cd = creditDebitNotesMapper.VoToEntity(debitNotesVo);
		CreditDebitNotes notesSave = creditDebitNotesRepo.save(cd);
		log.warn("we are checking if notes is saved...");
		log.info("note details  saved successfully:" + notesSave);
		return "note details  saved successfully:" + notesSave;

	}

	@Override
	public CreditDebitNotesVo getMobileNumber(String mobileNumber) {
		log.debug("debugging getMobileNumber:" + mobileNumber);
		CreditDebitNotes mob = creditDebitNotesRepo.findByMobileNumber(mobileNumber);
		if (mob == null) {
			log.info("notes record not found with mobileNumber:" + mobileNumber);
			throw new RecordNotFoundException("notes record not found with mobileNumber:" + mobileNumber);
		}
		CreditDebitNotesVo vo = creditDebitNotesMapper.EntityToVo(mob);
		log.warn("we are testing is fetching MobileNumber ");
		log.info("after fetching MobileNumber :" + vo.toString());
		return vo;
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
	public String updateCreditDebitNotes(CreditDebitNotesVo notesVo) {
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
		return "Notes updated successfully:" + notesUpdate.getCreditDebitId();
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
		List<CreditDebitNotes> storeOpt = creditDebitNotesRepo.findAllByStoreIdAndCreditDebit(vo.getStoreId(),
				creditDebit);

		/*
		 * using dates and storeId
		 */
		if (vo.getFromDate() != null && vo.getToDate() != null && vo.getStoreId() != null
				&& vo.getMobileNumber() == "") {

			if (storeOpt != null) {

				creditNotes = creditDebitNotesRepo
						.findByCreatedDateBetweenAndStoreIdAndCreditDebitOrderByLastModifiedDateAsc(vo.getFromDate(),
								vo.getToDate(), vo.getStoreId(), creditDebit);

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
				CreditDebitNotes mobileOpt = creditDebitNotesRepo.findByMobileNumber(vo.getMobileNumber());
				if (mobileOpt != null) {
					creditNotes = creditDebitNotesRepo
							.findByCreatedDateBetweenAndMobileNumberAndStoreIdAndCreditDebitOrderByLastModifiedDateAsc(
									vo.getFromDate(), vo.getToDate(), vo.getMobileNumber(), vo.getStoreId(),
									creditDebit);
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
				CreditDebitNotes mobileOpt = creditDebitNotesRepo.findByMobileNumber(vo.getMobileNumber());
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
		List<CreditDebitNotes> storeOpt = creditDebitNotesRepo.findAllByStoreIdAndCreditDebit(vo.getStoreId(),
				creditDebit);

		/*
		 * using dates and storeId
		 */
		if (vo.getFromDate() != null && vo.getToDate() != null && vo.getStoreId() != null
				&& vo.getMobileNumber() == "") {

			if (storeOpt != null) {

				debitNotes = creditDebitNotesRepo
						.findByCreatedDateBetweenAndStoreIdAndCreditDebitOrderByLastModifiedDateAsc(vo.getFromDate(),
								vo.getToDate(), vo.getStoreId(), creditDebit);

				if (debitNotes.isEmpty()) {
					log.error("No record found with given information");
					throw new RecordNotFoundException("No record found with given information");
				}
			} else {
				throw new RecordNotFoundException("No record found with storeId");
			}
		}
		/*
		 * using dates and mobile number and storeId
		 */
		else if (vo.getFromDate() != null && vo.getToDate() != null && vo.getMobileNumber() != null
				&& vo.getStoreId() != null) {

			if (storeOpt != null) {
				CreditDebitNotes mobileOpt = creditDebitNotesRepo.findByMobileNumberAndCreditDebit(vo.getMobileNumber(),
						creditDebit);
				if (mobileOpt != null) {
					debitNotes = creditDebitNotesRepo
							.findByCreatedDateBetweenAndMobileNumberAndStoreIdAndCreditDebitOrderByLastModifiedDateAsc(
									vo.getFromDate(), vo.getToDate(), vo.getMobileNumber(), vo.getStoreId(),
									creditDebit);
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
			if (storeOpt != null) {
				List<CreditDebitNotesVo> productList = creditDebitNotesMapper.EntityToVo(storeOpt);
				return productList;
			}
		}
		/*
		 * using mobile number and storeId
		 */
		else if (vo.getFromDate() == null && vo.getToDate() == null && vo.getMobileNumber() != null
				&& vo.getStoreId() != null) {

			if (storeOpt != null) {
				CreditDebitNotes mobileOpt = creditDebitNotesRepo.findByMobileNumberAndCreditDebit(vo.getMobileNumber(),
						creditDebit);
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
		} else {
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
