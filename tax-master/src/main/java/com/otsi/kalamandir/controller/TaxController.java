
 
/*  
 * controller for addNewTax And updateTax
*/
package com.otsi.kalamandir.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.otsi.kalamandir.service.TaxService;
import com.otsi.kalamandir.vo.TaxVo;

/*
 * @author Lakshmi
 */

@RestController
@RequestMapping("tax")
public class TaxController {

	@Autowired
	private TaxService taxservice;

	/*
	 * Purpose of this method : adding new tax to the tax master table arguments :
	 * taxVo object
	 */
	@PostMapping(value = "/addnewtax")
	public ResponseEntity<?> AddNewTax(@RequestBody TaxVo taxvo) {
		try {
		ResponseEntity<?> saveTax=	taxservice.addNewTax(taxvo);
		return new ResponseEntity<>(saveTax, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * update functionality for the Tax with the help of arguments: id,taxvo
	 * object
	 */
	@PutMapping(value = "/updatetax/{id}")
	public ResponseEntity<?> updateExistingTax(@PathVariable Long id, @RequestBody TaxVo taxvo) {
		try {

	ResponseEntity<?> updateTax=	taxservice.updateTax(id, taxvo);

		return new ResponseEntity<>(updateTax, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		

	}

}
