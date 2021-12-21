
/*  
 * controller for addNewTax And updateTax
*/
package com.otsi.retail.taxMaster.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.taxMaster.exceptions.RecordNotFoundException;
import com.otsi.retail.taxMaster.gatewayresponse.GateWayResponse;
import com.otsi.retail.taxMaster.service.TaxService;
import com.otsi.retail.taxMaster.vo.TaxVo;

/*
 * @author Lakshmi
 */

@RestController
@RequestMapping("tax")
public class TaxController {

	private Logger log = LogManager.getLogger(TaxController.class);

	@Autowired
	private TaxService taxservice;

	/*
	 * Purpose of this method : adding new tax to the tax master table arguments :
	 * taxVo object
	 */
	@PostMapping(value = "/addnewtax")
	public GateWayResponse<?> AddNewTax(@RequestBody TaxVo taxvo) {
		log.info("Recieved request to AddNewTax()");
		String saveTax = taxservice.addNewTax(taxvo);
		return new GateWayResponse<>("new tax added successfully",saveTax);

	}

	/*
	 * update functionality for the Tax with the help of arguments: id,taxvo object
	 */
	@PutMapping(value = "/updatetax/{id}")
	public GateWayResponse<?> updateExistingTax(@PathVariable Long id, @RequestBody TaxVo taxvo)
			throws RecordNotFoundException {
		log.info("Recieved request to updateExistingTax()");
		String updateTax = taxservice.updateTax(id, taxvo);
		return new GateWayResponse<>("updated tax successfully",updateTax);

	}

}
