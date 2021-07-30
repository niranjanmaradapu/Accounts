/**
 *   serviceImplementation class implements from service class for addNewTax,updateTax
 */

package com.otsi.kalamandir.serviceImp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.otsi.kalamandir.Repository.TaxRepository;
import com.otsi.kalamandir.exceptions.RecordNotFoundException;
import com.otsi.kalamandir.mapper.TaxMapper;
import com.otsi.kalamandir.model.TaxModel;
import com.otsi.kalamandir.service.TaxService;
import com.otsi.kalamandir.vo.TaxVo;
/**
 * @author Lakshmi
 */
@Component
public class TaxServiceImp implements TaxService {

	@Autowired
	private TaxRepository taxRepo;
	@Autowired
	private TaxMapper taxmapper;

	// Method use: add new tax information
	@Override
	public ResponseEntity<?> addNewTax(TaxVo taxvo) {

		// calling a mapper to convert vo to entity
		TaxModel tax = taxmapper.convertVoToEntity(taxvo);
		taxRepo.save(tax);
		TaxVo taxv =  taxmapper.convertEntityToVo(tax);
		return  new ResponseEntity<>("tax details  saved successfully",HttpStatus.OK);
	}

	/*
	 * update functionality for tax information
	 */	
	@Override
	public ResponseEntity<?> updateTax(Long id, TaxVo taxvo) {
		Optional<TaxModel> tax = taxRepo.findById(taxvo.getId());
		try {
		// if id is not present,it will throw custom exception "RecordNotFoundException"
		if (!tax.isPresent()) {
			throw new RecordNotFoundException("Record Not Found");
		}
		/*
		 * calling a mapper to convert vo to entity
		 */

		TaxModel taxsave = taxmapper.convertVoToEntity(taxvo);
		taxRepo.save(taxsave);
		
		TaxVo taxvosave=taxmapper.convertEntityToVo(taxsave);

		return  new ResponseEntity<>("tax details updated successfully with the given id  "  +taxvo.getId()+ "",HttpStatus.OK);
	
		}catch (Exception ex) {
		throw new RuntimeException(ex.getMessage());

}
	}
}
