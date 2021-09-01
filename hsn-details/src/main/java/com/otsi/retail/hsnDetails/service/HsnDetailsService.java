/**
 * service implementation class for HsnDetails
 *
 */
package com.otsi.retail.hsnDetails.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.otsi.retail.hsnDetails.enums.Description;
import com.otsi.retail.hsnDetails.enums.TaxAppliesOn;
import com.otsi.retail.hsnDetails.exceptions.RecordNotFoundException;
import com.otsi.retail.hsnDetails.mapper.HsnDetailsMapper;
import com.otsi.retail.hsnDetails.mapper.SlabMapper;
import com.otsi.retail.hsnDetails.mapper.TaxMapper;
import com.otsi.retail.hsnDetails.model.HsnDetails;
import com.otsi.retail.hsnDetails.model.Slab;
import com.otsi.retail.hsnDetails.model.Tax;
import com.otsi.retail.hsnDetails.repo.HsnDetailsRepo;
import com.otsi.retail.hsnDetails.repo.SlabRepo;
import com.otsi.retail.hsnDetails.repo.TaxRepo;
import com.otsi.retail.hsnDetails.vo.EnumVo;
import com.otsi.retail.hsnDetails.vo.HsnDetailsVo;
import com.otsi.retail.hsnDetails.vo.TaxVo;

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

	List<HsnDetailsVo> getHsnDetails();

	List<TaxVo> getTaxDetails();

	/* double getTaxValue(Double netAmount); */

	
}
