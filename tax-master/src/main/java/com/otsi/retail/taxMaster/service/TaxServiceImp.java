/**
 *   serviceImplementation class implements from service class for addNewTax,updateTax
 */

package com.otsi.retail.taxMaster.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.otsi.retail.taxMaster.Repository.TaxRepository;
import com.otsi.retail.taxMaster.exceptions.InvalidDataException;
import com.otsi.retail.taxMaster.exceptions.RecordNotFoundException;
import com.otsi.retail.taxMaster.mapper.TaxMapper;
import com.otsi.retail.taxMaster.model.TaxModel;
import com.otsi.retail.taxMaster.vo.TaxVo;

/**
 * @author Lakshmi
 */
@Component
public class TaxServiceImp implements TaxService {

	private Logger log = LogManager.getLogger(TaxServiceImp.class);

	@Autowired
	private TaxRepository taxRepo;
	@Autowired
	private TaxMapper taxmapper;

	// Method use: add new tax information
	@Override
	public String addNewTax(TaxVo taxvo) {
		if (taxvo.getCess() == 0 && taxvo.getCgst() == 0 && taxvo.getIgst() == 0 && taxvo.getIgst() == 0
				|| taxvo.getTaxLable() == null) {
			throw new InvalidDataException("please give valid data");
		}
		log.debug("debugging hsnSave:" + taxvo);
		// calling a mapper to convert vo to entity
		TaxModel tax = taxmapper.convertVoToEntity(taxvo);
		taxRepo.save(tax);
		TaxVo taxv = taxmapper.convertEntityToVo(tax);
		log.warn("we are checking if tax is saved...");
		log.info("tax details  saved successfully");

		return "tax details  saved successfully";

	}

	/*
	 * update functionality for tax information
	 */
	@Override
	public String updateTax(Long id, TaxVo taxvo) throws RecordNotFoundException {
		log.debug("debugging updateTax:" + taxvo);
		Optional<TaxModel> tax = taxRepo.findById(taxvo.getId());

		// if id is not present,it will throw custom exception "RecordNotFoundException"
		if (!tax.isPresent()) {
			log.error("Record Not Found");
			throw new RecordNotFoundException("Record Not Found");
		}
		/*
		 * calling a mapper to convert vo to entity
		 */

		TaxModel taxsave = taxmapper.convertVoToEntity(taxvo);
		taxRepo.save(taxsave);
		TaxVo taxvosave = taxmapper.convertEntityToVo(taxsave);
		log.warn("we are checking if tax is updated...");
		log.info("tax details updated successfully with the given id:" + taxvo.getId());
		return "tax details updated successfully with the given id  " + taxvo.getId();

	}
}