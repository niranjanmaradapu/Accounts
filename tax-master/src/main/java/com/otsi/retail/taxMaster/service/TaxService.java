/**
 * service interface for Tax
 */

package com.otsi.retail.taxMaster.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otsi.retail.taxMaster.vo.TaxVo;
/**
 * @author Lakshmi
 */
@Service
public interface TaxService {

	ResponseEntity<?> addNewTax(TaxVo taxvo);

    ResponseEntity<?> updateTax(Long id, TaxVo taxvo); 



}