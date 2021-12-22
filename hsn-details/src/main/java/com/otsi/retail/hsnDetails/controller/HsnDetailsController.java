/*
 * controller for save,update,delete and getEnums
 */
package com.otsi.retail.hsnDetails.controller;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.hsnDetails.gatewayresponse.GateWayResponse;
import com.otsi.retail.hsnDetails.service.HsnDetailsService;
import com.otsi.retail.hsnDetails.vo.EnumVo;
import com.otsi.retail.hsnDetails.vo.HsnDetailsVo;

/**
 * @author vasavi
 *
 */
@RestController
@RequestMapping("/hsnDetails")
public class HsnDetailsController {

	private Logger log = LogManager.getLogger(HsnDetailsController.class);

	@Autowired
	private HsnDetailsService hsnDetailsService;

	/*
	 * save functionality through service by HsnDetailsVo
	 */
	@PostMapping("/saveHsn")
	public GateWayResponse<?> saveHsn(@RequestBody HsnDetailsVo vo) {
		log.info("Received Request to add new hsn : " + vo.toString());
		HsnDetailsVo hsnSave = hsnDetailsService.hsnSave(vo);
		return new GateWayResponse<>("hsn-details saved successfully", hsnSave);

	}
	/*
	 * fetch functionality through service by @pathVariable("enumName)
	 */

	@GetMapping("/getEnums/{enumName}")
	public GateWayResponse<List<EnumVo>> getEnums(@PathVariable("enumName") String enumName) {
		log.info("Received Request to get enums: " + enumName);
		List<EnumVo> enumVo = hsnDetailsService.getEnums(enumName);
		return new GateWayResponse<List<EnumVo>>("fetching enum details successfully", enumVo);

	}
	/*
	 * update functionality through service by HsnDetailsVo
	 */

	@PutMapping(value = "/updateHsn")
	public GateWayResponse<?> updateHsn(@RequestBody HsnDetailsVo vo) {
		log.info("Received Request to update hsn :" + vo.toString());
		HsnDetailsVo hsnUpdate = hsnDetailsService.hsnUpdate(vo);
		return new GateWayResponse<>("hsn-details updated successfully", hsnUpdate);
	}

	/*
	 * delete functionality through service by id
	 */
	@DeleteMapping("/deleteHsn")
	public GateWayResponse<?> deleteHsn(@RequestParam long id) {
		log.info("Received Request to delete hsn :" + id);
		String hsnDelete = hsnDetailsService.hsnDelete(id);
		return new GateWayResponse<>("hsn deleted successfully", hsnDelete);

	}

	/*
	 * fetch functionality through service
	 */

	@GetMapping("/getHsnDetails")
	public GateWayResponse<?> getHsnDetails() {
		log.info("Received Request to get HsnDetails");
		List<HsnDetailsVo> hsnDetails = hsnDetailsService.getHsnDetails();
		return new GateWayResponse<>("fetching all hsn-details", hsnDetails);

	}

	/*
	 * @GetMapping("/gettaxvalue") public GateWayResponse<?>
	 * getTaxValue(@RequestParam Double netAmount) {
	 * log.info("Received Request to get HsnDetails");
	 * 
	 * double result = hsnDetailsService.getTaxValue(netAmount); return new
	 * GateWayResponse<>(result);
	 * 
	 * }
	 */
}
