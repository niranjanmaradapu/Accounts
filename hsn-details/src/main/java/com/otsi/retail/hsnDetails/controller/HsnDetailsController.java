/*
 * controller for save,update,delete and getEnums
 */
package com.otsi.retail.hsnDetails.controller;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.otsi.retail.hsnDetails.enums.TaxAppliedType;
import com.otsi.retail.hsnDetails.gatewayresponse.GateWayResponse;
import com.otsi.retail.hsnDetails.service.HsnDetailsService;
import com.otsi.retail.hsnDetails.vo.EnumVo;
import com.otsi.retail.hsnDetails.vo.HsnDetailsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author vasavi
 *
 */

@Api(value = "HsnDetailsController", description = "REST APIs related to HsnDetails Entity !!!!")
@RestController
@RequestMapping("/hsn-details")
public class HsnDetailsController {

	private Logger log = LogManager.getLogger(HsnDetailsController.class);

	@Autowired
	private HsnDetailsService hsnDetailsService;
	

	/*
	 * save functionality through service by HsnDetailsVo
	 */

	@ApiOperation(value = "", notes = "saving hsn details", response = HsnDetailsVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = HsnDetailsVo.class, responseContainer = "String") })
	@PostMapping("/save")
	public ResponseEntity<?> saveHsn(@RequestBody HsnDetailsVo hsnDetailsVo,@RequestHeader("userId") Long userId,@RequestHeader("clientId") Long clientId) {
		
		log.info("Received Request to add new hsn : " + hsnDetailsVo.toString());
		HsnDetailsVo hsnSave = hsnDetailsService.hsnSave(hsnDetailsVo,userId,clientId);
		return ResponseEntity.ok(hsnSave);

	}
	
	
	/*
	 * fetch functionality through service by @pathVariable("enumName)
	 */

	@ApiOperation(value = "getEnums", notes = "fetching enums using enum name", response = EnumVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = EnumVo.class, responseContainer = "List") })
	@GetMapping("/getEnums/{enumName}")
	public GateWayResponse<List<EnumVo>> getEnums(@PathVariable("enumName") String enumName) {
		log.info("Received Request to get enums: " + enumName);
		List<EnumVo> enumVo = hsnDetailsService.getEnums(enumName);
		return new GateWayResponse<List<EnumVo>>("fetching enum details successfully", enumVo);

	}
	
	
	/*
	 * update functionality through service by HsnDetailsVo
	 */

	@ApiOperation(value = "updateHsn", notes = "updating hsn details", response = HsnDetailsVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = HsnDetailsVo.class, responseContainer = "String") })
	@PutMapping(value = "/updateHsn")
	public ResponseEntity<?> updateHsn(@RequestBody HsnDetailsVo vo) {
		log.info("Received Request to update hsn :" + vo.toString());
		HsnDetailsVo hsnUpdate = hsnDetailsService.hsnUpdate(vo);
		return ResponseEntity.ok(hsnUpdate);
	}

	
	/*
	 * delete functionality through service by id
	 */
	@ApiOperation(value = "deleteHsn", notes = "delete hsn details", response = HsnDetailsVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = HsnDetailsVo.class, responseContainer = "String") })
	@DeleteMapping("/deleteHsn")
	public GateWayResponse<?> deleteHsn(@RequestParam long id) {
		log.info("Received Request to delete hsn :" + id);
		String hsnDelete = hsnDetailsService.hsnDelete(id);
		return new GateWayResponse<>("hsn deleted successfully", hsnDelete);

	}

	/*
	 * fetch functionality through service
	 */

	@ApiOperation(value = "getHsnDetails", notes = "fetching hsn-details", response = HsnDetailsVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = HsnDetailsVo.class, responseContainer = "List") })
	@GetMapping("/getHsnDetails")
	public GateWayResponse<?> getHsnDetails(Pageable pageable,@RequestParam(required = false) String hsnCode,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) TaxAppliedType taxAppliedType,@RequestHeader(required = false) Long clientId) {
		log.info("Received Request to get HsnDetails");
		log.info("clientId is:"+clientId);
		Page<HsnDetailsVo> hsnDetails = hsnDetailsService.getHsnDetails(hsnCode,description,taxAppliedType,clientId,pageable);
		return new GateWayResponse<>("fetching all hsn-details", hsnDetails);

	}

	@ApiOperation(value = "getHsnDetails", notes = "fetching hsn-details", response = HsnDetailsVo.class)
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = HsnDetailsVo.class, responseContainer = "List") })
	@GetMapping("/getAllHsnDetails")
	public GateWayResponse<?> getAllHsnDetails(@RequestParam("hsnCode") String hsnCode) {
		log.info("Received Request to get HsnDetails");
		List<HsnDetailsVo> hsnDetails = hsnDetailsService.getAllHsnDetails(hsnCode);
		return new GateWayResponse<>("fetching all hsn-details", hsnDetails);

	}

}
