package com.otsi.retail.hsnDetails.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.otsi.retail.hsnDetails.exceptions.RecordNotFoundException;
import com.otsi.retail.hsnDetails.model.Tax;
import com.otsi.retail.hsnDetails.vo.TaxVo;

@Service
public interface TaxService {

	String addNewTax(TaxVo taxvo);

	String updateTax(TaxVo taxvo) throws RecordNotFoundException;

	Optional<Tax> getTaxById(Long id);

	String deleteTax(Long id);

	List<TaxVo> getTaxDetails();

	

}
