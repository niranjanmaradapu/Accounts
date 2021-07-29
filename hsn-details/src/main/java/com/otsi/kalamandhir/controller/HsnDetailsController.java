/*
 * controller for save,update,delete and getEnums
 */
package com.otsi.kalamandhir.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.otsi.kalamandhir.gatewayresponse.GateWayResponse;
import com.otsi.kalamandhir.service.HsnDetailsService;
import com.otsi.kalamandhir.vo.EnumVo;
import com.otsi.kalamandhir.vo.HsnDetailsVo;

/**
 * @author vasavi
 *
 */
@RestController
@RequestMapping("/hsnDetails")
public class HsnDetailsController {

	private Logger log = LoggerFactory.getLogger(HsnDetailsController.class);

	@Autowired
	private HsnDetailsService hsnDetailsService;

	/*
	 * save functionality through service by HsnDetailsVo
	 */
	@PostMapping("/saveHsn")
	public GateWayResponse<HsnDetailsVo> saveHsn(@RequestBody HsnDetailsVo vo) {
		log.info("Received Request to add new hsn : " + vo.toString());
		return new GateWayResponse<HsnDetailsVo>(hsnDetailsService.hsnSave(vo));

	}
	/*
	 * fetch functionality through service by @pathVariable("enumName)
	 */

	@GetMapping("/getEnums/{enumName}")
	public GateWayResponse<List<EnumVo>> getEnums(@PathVariable("enumName") String enumName) {
		log.info("Received Request to get enums: " + enumName);
		return new GateWayResponse<List<EnumVo>>(hsnDetailsService.getEnums(enumName));

	}
	/*
	 * update functionality through service by HsnDetailsVo
	 */

	@PutMapping(value = "/updateHsn")
	public GateWayResponse<HsnDetailsVo> updateHsn(@RequestBody HsnDetailsVo vo) {
		log.info("Received Request to update hsn :" + vo.toString());
		return new GateWayResponse<HsnDetailsVo>(hsnDetailsService.hsnUpdate(vo));
	}
	/*
	 * get functionality through service
	 */

	@GetMapping("/getTaxDetails")
	public GateWayResponse<?> getTaxDetails() {
		log.info("Received Request to get TaxDetails");
		return new GateWayResponse<>(hsnDetailsService.getTaxDetails());

	}
        /*
	 * delete functionality through service by id
	 */
	@DeleteMapping("/deleteHsn")
	public GateWayResponse<String> deleteHsn(@RequestParam long id) {
		log.info("Received Request to delete hsn :" + id);
		return new GateWayResponse<String>(hsnDetailsService.hsnDelete(id));

	}

}
