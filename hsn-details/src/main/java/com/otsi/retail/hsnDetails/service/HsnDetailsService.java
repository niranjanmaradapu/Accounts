/**
 * service implementation class for HsnDetails
 *
 */
package com.otsi.retail.hsnDetails.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.otsi.retail.hsnDetails.enums.TaxAppliedType;
import com.otsi.retail.hsnDetails.vo.EnumVo;
import com.otsi.retail.hsnDetails.vo.HsnDetailsVo;
import com.otsi.retail.hsnDetails.vo.TaxVo;

/**
 * @author vasavi
 *
 */
@Service
public interface HsnDetailsService {

	HsnDetailsVo hsnSave(HsnDetailsVo hsnDetailsVo, Long userId);

	String hsnUpdate(HsnDetailsVo vo);

	List<EnumVo> getEnums(String enumName);

	String hsnDelete(long id);

	List<HsnDetailsVo> getHsnDetails(String hsnCode, String description, TaxAppliedType taxAppliedType);

	List<HsnDetailsVo> getAllHsnDetails(String hsnCode);

}
