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

@RestController
@RequestMapping("/credit-debit-notes")
public class CreditDebitNotesController {

	private Logger log = LoggerFactory.getLogger(CreditDebitNotesController.class);

	@Autowired
	private CreditDebitNotesService creditDebitNotesService;

	@PostMapping("/saveCreditDebitNotes")
	public GateWayResponse<?> saveCreditDebitNotes(@RequestBody CreditDebitNotesVo debitNotesVo) {
		log.info("Received Request to saveDebitNotes : " + debitNotesVo);
		String debitNotesSave = creditDebitNotesService.saveCreditDebitNotes(debitNotesVo);
		return new GateWayResponse<>("saved notes successfully", debitNotesSave);

	}

	@GetMapping("/getMobileNumber")
	public GateWayResponse<?> getMobileNumber(@RequestParam("mobileNumber") String mobileNumber) {
		log.info("Received Request to getMobileNumber : " + mobileNumber);
		CreditDebitNotesVo mobNo = creditDebitNotesService.getMobileNumber(mobileNumber);
		return new GateWayResponse<>("fetching  notes successfully with id", mobNo);
	}

	@GetMapping("/getAllCreditDebitNotes")
	public GateWayResponse<?> getAllCreditDebitNotes() {
		log.info("Received Request to getAllCreditDebitNotes");
		List<CreditDebitNotes> allCreditDebitNotes = creditDebitNotesService.getAllCreditDebitNotes();
		return new GateWayResponse<>("fetching all  notes sucessfully", allCreditDebitNotes);

	}

	@PostMapping("/saveListCreditDebitNotes")
	public GateWayResponse<?> saveListCreditDebitNotes(@RequestBody List<CreditDebitNotesVo> creditDebitNotesVo) {
		log.info("Received Request to saveListCreditDebitNotes:" + creditDebitNotesVo);
		List<CreditDebitNotesVo> saveVoList = creditDebitNotesService.saveListCreditDebitNotes(creditDebitNotesVo);
		return new GateWayResponse<>("saving list of notes", saveVoList);

	}

	@PostMapping(value = "/updateCreditDebitNotes")
	public GateWayResponse<?> updateCreditDebitNotes(@RequestBody UpdateCreditRequest vo) {
		log.info("Recieved request to updateCreditDebitNotes:" + vo);
		String updateNotes = creditDebitNotesService.updateCreditDebitNotes(vo);
		return new GateWayResponse<>("updated notes successfully", updateNotes);

	}

	@PutMapping(value = "/updateNotes")
	public GateWayResponse<?> updateNotes(@RequestBody CreditDebitNotesVo vo) {
		log.info("Recieved request to updateCreditDebitNotes:" + vo);
		String updateNotes = creditDebitNotesService.updateNotes(vo);
		return new GateWayResponse<>("updated notes successfully", updateNotes);

	}
	
	@DeleteMapping("/deleteCreditDebitNotes")
	public GateWayResponse<?> deleteCreditDebitNotes(@RequestParam("creditDebitId") Long creditDebitId) {
		log.info("Recieved request to deleteCreditDebitNotes:" + creditDebitId);
		String deleteNotes = creditDebitNotesService.deleteCreditDebitNotes(creditDebitId);
		return new GateWayResponse<>("notes deleted successfully", deleteNotes);

	}

	@PostMapping("/getAllCreditNotes")
	public GateWayResponse<?> getAllCreditNotes(@RequestBody CreditDebitNotesVo vo) {
		log.info("Recieved request to getAllCreditNotes:" + vo);
		List<CreditDebitNotesVo> allCreditNotes = creditDebitNotesService.getAllCreditNotes(vo);
		return new GateWayResponse<>("fetching all credit notes details sucessfully", allCreditNotes);
	}

	@PostMapping("/getAllDebitNotes")
	public GateWayResponse<?> getAllDebitNotes(@RequestBody CreditDebitNotesVo vo) {
		log.info("Recieved request to getAllDebitNotes:" + vo);
		List<CreditDebitNotesVo> allDebitNotes = creditDebitNotesService.getAllDebitNotes(vo);
		return new GateWayResponse<>("fetching all credit notes details sucessfully", allDebitNotes);
	}
	
	

}
