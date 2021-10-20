package com.otsi.retail.hsnDetails.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.otsi.retail.hsnDetails.exceptions.InvalidDataException;
import com.otsi.retail.hsnDetails.exceptions.RecordNotFoundException;
import com.otsi.retail.hsnDetails.mapper.TaxMapper;
import com.otsi.retail.hsnDetails.model.Tax;
import com.otsi.retail.hsnDetails.repo.TaxRepo;
import com.otsi.retail.hsnDetails.vo.TaxVo;

@Component
public class TaxServiceImpl implements TaxService {

	private Logger log = LoggerFactory.getLogger(TaxServiceImpl.class);

	@Autowired
	private TaxRepo taxRepo;
	@Autowired
	private TaxMapper taxMapper;

	// Method use: add new tax information
	@Override
	public String addNewTax(TaxVo taxvo) {
		if (taxvo.getCess() == 0 && taxvo.getCgst() == 0 && taxvo.getIgst() == 0 && taxvo.getIgst() == 0
				|| taxvo.getTaxLabel() == null) {
			throw new InvalidDataException("please give valid data");
		}
		log.debug("debugging hsnSave:" + taxvo);
		Tax tax = taxMapper.VoToEntity(taxvo);
		Tax taxSave = taxRepo.save(tax);

		log.warn("we are checking if tax is saved...");
		log.info("tax details  saved successfully:" + taxSave);

		return "tax details  saved successfully:" + taxSave;

	}

	/*
	 * update functionality for tax information
	 */
	@Override
	public String updateTax(TaxVo taxvo) throws RecordNotFoundException {
		log.debug("debugging updateTax:" + taxvo);
		Optional<Tax> taxOpt = taxRepo.findById(taxvo.getId());

		// if id is not present,it will throw custom exception "RecordNotFoundException"
		if (!taxOpt.isPresent()) {
			log.error("Record Not Found");
			throw new RecordNotFoundException("tax Record is Not Found");
		}

		Tax tax = taxMapper.VoToEntity(taxvo);
		Tax taxSave = taxRepo.save(tax);
		TaxVo taxUpdate = taxMapper.EntityToVo(taxSave);
		log.warn("we are checking if tax is updated...");
		log.info("tax details updated successfully with the given id:" + taxUpdate);
		return "tax details updated successfully with the given id  " + taxUpdate;

	}

	@Override
	public Optional<Tax> getTaxById(Long id) {
		log.debug("debugging getTaxById:" + id);
		Optional<Tax> tax = taxRepo.findById(id);
		if (!(tax.isPresent())) {
			log.error("tax  record is not found");
			throw new RecordNotFoundException("tax record is not found");
		}
		log.warn("we are checking if tax data is fetching by id...");
		log.info("after fetching tax details:" + tax.toString());
		return tax;

	}

	/*
	 * get functionality for tax_details
	 */
	@Override
	public List<TaxVo> getTaxDetails() {
		log.debug("debugging getTaxDetails()");
		List<Tax> taxs = new ArrayList<>();
		List<TaxVo> VOList = new ArrayList<>();
		// here,will find all tax's through taxRepo
		taxs = taxRepo.findAll();
		// here,will map and assign to VOList and return voList
		VOList = taxMapper.EntityToVo(taxs);
		log.warn("we are checking if tax details is fetching...");
		log.info("after getting tax details:" + VOList);
		return VOList;
	}

	@Override
	public String deleteTax(Long id) {
		log.debug("debugging deleteDomain:" + id);
		Optional<Tax> taxOpt = taxRepo.findById(id);
		if (!(taxOpt.isPresent())) {
			throw new RecordNotFoundException("tax not found with id: " + id);
		}
		taxRepo.delete(taxOpt.get());
		log.warn("we are checking if tax is deleted...");
		log.info("after deleting tax details:" + id);
		return "tax data deleted succesfully: " + id;
	}

}
