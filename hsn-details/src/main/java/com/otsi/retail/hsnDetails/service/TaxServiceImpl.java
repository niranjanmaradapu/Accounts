package com.otsi.retail.hsnDetails.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.otsi.retail.hsnDetails.exceptions.RecordNotFoundException;
import com.otsi.retail.hsnDetails.mapper.TaxMapper;
import com.otsi.retail.hsnDetails.model.Tax;
import com.otsi.retail.hsnDetails.repository.TaxRepository;
import com.otsi.retail.hsnDetails.vo.TaxVO;

/**
 * @author vasavi
 *
 */
@Component
public class TaxServiceImpl implements TaxService {

	private Logger log = LogManager.getLogger(TaxServiceImpl.class);

	@Autowired
	private TaxRepository taxRepository;

	@Autowired
	private TaxMapper taxMapper;

	@Override
	public TaxVO saveTax(TaxVO taxvo, Long clientId) {
		if (taxvo.getCess() == 0 && taxvo.getCgst() == 0 && taxvo.getIgst() == 0 && taxvo.getIgst() == 0
				|| taxvo.getTaxLabel() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tax inputs are missing");
		}
		Tax tax = taxMapper.voToEntity(taxvo);
		tax.setClientId(clientId);
		taxvo = taxMapper.entityToVO(taxRepository.save(tax));
		return taxvo;

	}

	@Override
	public String updateTax(TaxVO taxvo) throws RecordNotFoundException {
		log.debug("debugging updateTax:" + taxvo);
		Optional<Tax> taxOptional = taxRepository.findById(taxvo.getId());
		if (!taxOptional.isPresent()) {
			log.error("Record Not Found");
			throw new RecordNotFoundException("tax Record is Not Found");
		}
		Tax tax = taxMapper.voToEntity(taxvo);
		tax = taxRepository.save(tax);
		TaxVO taxUpdate = taxMapper.entityToVO(tax);
		return "tax details updated successfully with the given id  " + taxUpdate;

	}

	@Override
	public TaxVO getTaxById(Long id) {
		log.debug("debugging getTaxById:" + id);
		Optional<Tax> tax = taxRepository.findById(id);
		if (!(tax.isPresent())) {
			log.error("tax  record is not found");
			throw new RecordNotFoundException("tax record is not found");
		}
		return taxMapper.entityToVO(tax.get());
	}

	@Override
	public List<TaxVO> getTaxDetails(String taxLabel, Long clientId) {
		List<Tax> taxs = new ArrayList<>();
		if (taxLabel != null) {
			taxs = taxRepository.findByTaxLabelAndClientId(taxLabel, clientId);
		} else if (clientId != null) {
			taxs = taxRepository.findByClientId(clientId);
		} else {
			taxs = taxRepository.findAll();
		}
		return taxMapper.entityToVO(taxs);
	}

	@Override
	public String deleteTax(Long id) {
		log.debug("debugging deleteDomain:" + id);
		Optional<Tax> taxOpt = taxRepository.findById(id);
		if (!(taxOpt.isPresent())) {
			throw new RecordNotFoundException("tax not found with id: " + id);
		}
		taxRepository.delete(taxOpt.get());
		return "tax data deleted succesfully: " + id;
	}

	@Override
	public List<TaxVO> getTaxForGivenIds(List<Long> taxIds) {
		List<Tax> taxList = taxRepository.findByIdIn(taxIds);
		return taxMapper.entityToVO(taxList);
	}

}
