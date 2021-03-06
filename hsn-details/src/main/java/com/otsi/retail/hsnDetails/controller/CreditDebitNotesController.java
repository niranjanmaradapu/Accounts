package com.otsi.retail.hsnDetails.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.otsi.retail.hsnDetails.gatewayresponse.GateWayResponse;
import com.otsi.retail.hsnDetails.model.CreditDebitNotes;
import com.otsi.retail.hsnDetails.service.CreditDebitNotesService;
import com.otsi.retail.hsnDetails.vo.CreditDebitNotesVo;
import com.otsi.retail.hsnDetails.vo.UpdateCreditRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "CreditDebitNotesController", description = "REST APIs related to CreditDebitNotes !!!!")
@RestController
@RequestMapping("/credit-debit-notes")
public class CreditDebitNotesController {

	private Logger log = LoggerFactory.getLogger(CreditDebitNotesController.class);

	@Autowired
	private CreditDebitNotesService creditDebitNotesService;

	@ApiOperation(value = "saveCreditDebitNotes", notes = "saving credit/debit notes", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = CreditDebitNotesVo.class, responseContainer = "String") })
	@PostMapping("/saveCreditDebitNotes")
	public GateWayResponse<?> saveCreditDebitNotes(@RequestBody CreditDebitNotesVo debitNotesVo) {
		log.info("Received Request to saveDebitNotes : " + debitNotesVo);
		String debitNotesSave = creditDebitNotesService.saveCreditDebitNotes(debitNotesVo);
		return new GateWayResponse<>("saved notes successfully", debitNotesSave);

	}

	@ApiOperation(value = "getCreditNotes", notes = "fetching credit notes using customerId", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = CreditDebitNotesVo.class, responseContainer = "List") })
	@GetMapping("/getCreditNotes")
	public GateWayResponse<?> getMobileNumber(@RequestParam("mobileNumber") String mobileNumber,@RequestParam("customerId") Long customerId ) {
		log.info("Received Request to getCreditNotes : " + mobileNumber+"and"+customerId);
		List<CreditDebitNotesVo> mobNo = creditDebitNotesService.getCreditNotes(mobileNumber,customerId);
		return new GateWayResponse<>("fetching  notes successfully with id", mobNo);
	}

	@ApiOperation(value = "getAllCreditDebitNotes", notes = "fetching all credit and debit notes", response = CreditDebitNotes.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = CreditDebitNotes.class, responseContainer = "List") })
	@GetMapping("/getAllCreditDebitNotes")
	public GateWayResponse<?> getAllCreditDebitNotes() {
		log.info("Received Request to getAllCreditDebitNotes");
		List<CreditDebitNotes> allCreditDebitNotes = creditDebitNotesService.getAllCreditDebitNotes();
		return new GateWayResponse<>("fetching all  notes sucessfully", allCreditDebitNotes);

	}

	@ApiOperation(value = "saveListCreditDebitNotes", notes = "adding bulk of credit and debit notes", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = CreditDebitNotesVo.class, responseContainer = "List") })
	@PostMapping("/saveListCreditDebitNotes")
	public GateWayResponse<?> saveListCreditDebitNotes(@RequestBody List<CreditDebitNotesVo> creditDebitNotesVo) {
		log.info("Received Request to saveListCreditDebitNotes:" + creditDebitNotesVo);
		List<CreditDebitNotesVo> saveVoList = creditDebitNotesService.saveListCreditDebitNotes(creditDebitNotesVo);
		return new GateWayResponse<>("saving list of notes", saveVoList);

	}

	@ApiOperation(value = "updateCreditDebitNotes", notes = "updating credit/debit notes", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = CreditDebitNotesVo.class, responseContainer = "String") })
	@PostMapping(value = "/updateCreditDebitNotes")
	public GateWayResponse<?> updateCreditDebitNotes(@RequestBody UpdateCreditRequest vo) {
		log.info("Recieved request to updateCreditDebitNotes:" + vo);
		String updateNotes = creditDebitNotesService.updateCreditDebitNotes(vo);
		return new GateWayResponse<>("updated notes successfully", updateNotes);

	}

	@ApiOperation(value = "updateNotes", notes = "updating credit/debit notes from newsale", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = CreditDebitNotesVo.class, responseContainer = "String") })
	@PutMapping(value = "/updateNotes")
	public GateWayResponse<?> updateNotes(@RequestBody CreditDebitNotesVo vo) {
		log.info("Recieved request to updateCreditDebitNotes:" + vo);
		String updateNotes = creditDebitNotesService.updateNotes(vo);
		return new GateWayResponse<>("updated notes successfully", updateNotes);

	}
	
	@ApiOperation(value = "deleteCreditDebitNotes", notes = "deleting credit/debit notes using id", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = CreditDebitNotesVo.class, responseContainer = "String") })
	@DeleteMapping("/deleteCreditDebitNotes")
	public GateWayResponse<?> deleteCreditDebitNotes(@RequestParam("creditDebitId") Long creditDebitId) {
		log.info("Recieved request to deleteCreditDebitNotes:" + creditDebitId);
		String deleteNotes = creditDebitNotesService.deleteCreditDebitNotes(creditDebitId);
		return new GateWayResponse<>("notes deleted successfully", deleteNotes);

	}

	@ApiOperation(value = "getAllCreditNotes", notes = "fetching all credit notes", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = CreditDebitNotesVo.class, responseContainer = "List") })
	@PostMapping("/getAllCreditNotes")
	public GateWayResponse<?> getAllCreditNotes(@RequestBody CreditDebitNotesVo vo) {
		log.info("Recieved request to getAllCreditNotes:" + vo);
		List<CreditDebitNotesVo> allCreditNotes = creditDebitNotesService.getAllCreditNotes(vo);
		return new GateWayResponse<>("fetching all credit notes details sucessfully", allCreditNotes);
	}

	@ApiOperation(value = "getAllDebitNotes", notes = "fetching all debit notes", response = CreditDebitNotesVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", 
			response = CreditDebitNotesVo.class, responseContainer = "List") })
	@PostMapping("/getAllDebitNotes")
	public GateWayResponse<?> getAllDebitNotes(@RequestBody CreditDebitNotesVo vo) {
		log.info("Recieved request to getAllDebitNotes:" + vo);
		List<CreditDebitNotesVo> allDebitNotes = creditDebitNotesService.getAllDebitNotes(vo);
		return new GateWayResponse<>("fetching all debit notes details sucessfully", allDebitNotes);
	}
	
}
