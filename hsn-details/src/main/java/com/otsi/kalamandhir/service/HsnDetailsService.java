
/**
 * service class for HsnDetails
 *
 */
package com.otsi.kalamandhir.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.otsi.kalamandhir.vo.EnumVo;
import com.otsi.kalamandhir.vo.HsnDetailsVo;
import com.otsi.kalamandhir.vo.TaxVo;

/**
 * @author vasavi
 *
 */
@Service
public interface HsnDetailsService {

	HsnDetailsVo hsnSave(HsnDetailsVo vo);

	HsnDetailsVo hsnUpdate(HsnDetailsVo vo);

	List<EnumVo> getEnums(String enumName);

	String hsnDelete(long id);
	
	List<TaxVo> getTaxDetails();

}
