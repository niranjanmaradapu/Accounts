package com.otsi.retail.hsnDetails.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.otsi.retail.hsnDetails.exceptions.RecordNotFoundException;
import com.otsi.retail.hsnDetails.vo.TaxVO;

@Service
public interface TaxService {

	String updateTax(TaxVO taxvo) throws RecordNotFoundException;

	TaxVO getTaxById(Long id);
	
	List<TaxVO> getTaxForGivenIds(List<Long> taxIds);

	String deleteTax(Long id);

	List<TaxVO> getTaxDetails(String taxLabel, Long clientId);

	TaxVO saveTax(TaxVO taxvo, Long clientId);

}
