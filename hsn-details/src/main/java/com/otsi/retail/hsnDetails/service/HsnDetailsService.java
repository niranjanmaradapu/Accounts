/**
 * service implementation class for HsnDetails
 *
 */
package com.otsi.retail.hsnDetails.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.otsi.retail.hsnDetails.vo.EnumVo;
import com.otsi.retail.hsnDetails.vo.HsnDetailsVo;
import com.otsi.retail.hsnDetails.vo.TaxVo;

/**
 * @author vasavi
 *
 */
@Service
public interface HsnDetailsService {

	String hsnSave(HsnDetailsVo vo);

	String hsnUpdate(HsnDetailsVo vo);

	List<EnumVo> getEnums(String enumName);

	String hsnDelete(long id);

	List<HsnDetailsVo> getHsnDetails();

	/* double getTaxValue(Double netAmount); */

}
