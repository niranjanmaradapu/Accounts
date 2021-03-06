package com.otsi.retail.hsnDetails.controller;
/*  
 * controller for addNewTax And updateTax
*/
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.otsi.retail.hsnDetails.exceptions.RecordNotFoundException;
import com.otsi.retail.hsnDetails.gatewayresponse.GateWayResponse;
import com.otsi.retail.hsnDetails.model.Tax;
import com.otsi.retail.hsnDetails.service.TaxService;
import com.otsi.retail.hsnDetails.vo.TaxVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "TaxController", description = "REST APIs related to TaxEntity !!!!")
@RestController
@RequestMapping("tax")
public class TaxController {

	private Logger log = LogManager.getLogger(TaxController.class);

	@Autowired
	private TaxService taxService;

	/*
	 * Purpose of this method : adding new tax to the tax master table arguments :
	 * taxVo object
	 */
	@ApiOperation(value = "addnewtax", notes = "adding tax details", response = TaxVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = TaxVo.class, responseContainer = "String") })
	@PostMapping(value = "/addnewtax")
	public GateWayResponse<?> AddNewTax(@RequestBody TaxVo taxvo) {
		log.info("Recieved request to AddNewTax()");
		String saveTax = taxService.addNewTax(taxvo);
		return new GateWayResponse<>("new tax added successfully", saveTax);

	}

	@ApiOperation(value = "getTax", notes = "fetching tax details using id", response = TaxVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = TaxVo.class, responseContainer = "Object") })
	@GetMapping("/getTax")
	public GateWayResponse<?> getTax(@RequestParam("id") Long id) {
		log.info("Recieved request to getTax:" + id);
		Optional<Tax> tax = taxService.getTaxById(id);
		return new GateWayResponse<>("fetching tax data details successfully with id", tax);
	}

	/*
	 * get functionality through service
	 */

	@ApiOperation(value = "getTaxDetails", notes = "fetching all tax details", response = TaxVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = TaxVo.class, responseContainer = "List") })
	@GetMapping("/getTaxDetails")
	public GateWayResponse<?> getTaxDetails() {
		log.info("Received Request to get TaxDetails");
		List<TaxVo> tax = taxService.getTaxDetails();
		return new GateWayResponse<>("fetching tax details successfully", tax);

	}

	/*
	 * update functionality for the Tax with the help of arguments: id,taxvo object
	 */
	@ApiOperation(value = "updatetax", notes = "updating tax details", response = TaxVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = TaxVo.class, responseContainer = "String") })
	@PutMapping(value = "/updatetax")
	public GateWayResponse<?> updateExistingTax(@RequestBody TaxVo taxvo) throws RecordNotFoundException {
		log.info("Recieved request to updateExistingTax()");
		String updateTax = taxService.updateTax(taxvo);
		return new GateWayResponse<>("updated tax successfully", updateTax);

	}

	@ApiOperation(value = "deleteTax", notes = "deleting tax details", response = TaxVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = TaxVo.class, responseContainer = "String") })
	@DeleteMapping("/deleteTax")
	public GateWayResponse<?> deleteTax(@RequestParam("id") Long id) throws Exception {
		log.info("Recieved request to deleteTax:" + id);
		String deleteTax = taxService.deleteTax(id);
		return new GateWayResponse<>("tax data deleted successfully", deleteTax);

	}

}
