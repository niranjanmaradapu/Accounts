/**
 *   serviceImplementation class implements from service class for addNewTax,updateTax
 */

package com.otsi.retail.taxMaster.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.otsi.retail.taxMaster.Repository.TaxRepository;
import com.otsi.retail.taxMaster.exceptions.RecordNotFoundException;
import com.otsi.retail.taxMaster.mapper.TaxMapper;
import com.otsi.retail.taxMaster.model.TaxModel;
import com.otsi.retail.taxMaster.vo.TaxVo;

/**
 * @author Lakshmi
 */
@Component
public class TaxServiceImp implements TaxService {

	private Logger log = LoggerFactory.getLogger(TaxServiceImp.class);

	@Autowired
	private TaxRepository taxRepo;
	@Autowired
	private TaxMapper taxmapper;

	// Method use: add new tax information
	@Override
	public String addNewTax(TaxVo taxvo) {
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
	public String updateTax(Long id, TaxVo taxvo) {
		log.debug("debugging updateTax:" + taxvo);
		Optional<TaxModel> tax = taxRepo.findById(taxvo.getId());
		try {
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

		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new RuntimeException(ex.getMessage());

		}
	}
}