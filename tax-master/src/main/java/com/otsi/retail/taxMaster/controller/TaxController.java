
/*  
 * controller for addNewTax And updateTax
*/
package com.otsi.retail.taxMaster.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.otsi.retail.taxMaster.gatewayresponse.GateWayResponse;
import com.otsi.retail.taxMaster.service.TaxService;
import com.otsi.retail.taxMaster.vo.TaxVo;

/*
 * @author Lakshmi
 */

@RestController
@RequestMapping("tax")
public class TaxController {

	private Logger log = LoggerFactory.getLogger(TaxController.class);

	@Autowired
	private TaxService taxservice;

	/*
	 * Purpose of this method : adding new tax to the tax master table arguments :
	 * taxVo object
	 */
	@PostMapping(value = "/addnewtax")
	public GateWayResponse<?> AddNewTax(@RequestBody TaxVo taxvo) {
		log.info("Recieved request to AddNewTax()");
		try {
			String saveTax = taxservice.addNewTax(taxvo);
			return new GateWayResponse<>(HttpStatus.OK, saveTax.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

	/*
	 * update functionality for the Tax with the help of arguments: id,taxvo object
	 */
	@PutMapping(value = "/updatetax/{id}")
	public GateWayResponse<?> updateExistingTax(@PathVariable Long id, @RequestBody TaxVo taxvo) {
		log.info("Recieved request to updateExistingTax()");
		try {
			String updateTax = taxservice.updateTax(id, taxvo);
			return new GateWayResponse<>(HttpStatus.OK, updateTax.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

}
