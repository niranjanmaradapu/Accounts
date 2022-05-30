/**
 * service implementation class for HsnDetails
 *
 */
package com.otsi.retail.hsnDetails.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.otsi.retail.hsnDetails.enums.TaxAppliedType;
import com.otsi.retail.hsnDetails.vo.EnumVo;
import com.otsi.retail.hsnDetails.vo.HsnDetailsVo;


/**
 * @author vasavi
 *
 */
@Service
public interface HsnDetailsService {

	HsnDetailsVo hsnSave(HsnDetailsVo hsnDetailsVo, Long userId,Long clientId);

	HsnDetailsVo hsnUpdate(HsnDetailsVo vo);

	List<EnumVo> getEnums(String enumName);

	String hsnDelete(long id);


	List<HsnDetailsVo> getAllHsnDetails(String hsnCode);

	//List<HsnDetailsVo> getHsnDetails(String hsnCode, String description, TaxAppliedType taxAppliedType);

	List<HsnDetailsVo> getHsnDetails(String hsnCode, String description, TaxAppliedType taxAppliedType, Long clientId);

	Map<String, Float> getHsnDetails(String hsnCode, Float itemPrice, Long storeId);

}
