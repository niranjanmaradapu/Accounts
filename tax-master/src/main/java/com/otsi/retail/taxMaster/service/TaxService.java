/**
 * service interface for Tax
 */

package com.otsi.retail.taxMaster.service;

import org.springframework.stereotype.Service;
import com.otsi.retail.taxMaster.exceptions.RecordNotFoundException;
import com.otsi.retail.taxMaster.vo.TaxVo;

/**
 * @author Lakshmi
 */
@Service
public interface TaxService {

	String addNewTax(TaxVo taxvo);

	String updateTax(Long id, TaxVo taxvo) throws RecordNotFoundException;

}