/*
 * mapper for HsnDetails
 * 
*/
package com.otsi.retail.hsnDetails.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.otsi.retail.hsnDetails.enums.TaxAppliedType;
import com.otsi.retail.hsnDetails.model.HsnDetails;
import com.otsi.retail.hsnDetails.vo.HsnDetailsVo;

/**
 * @author vasavi
 *
 */
@Component
public class HsnDetailsMapper {

	/*
	 * EntityToVo converts dto to vo
	 * 
	 */

	public HsnDetailsVo EntityToVo(HsnDetails hsnDetails) {

		HsnDetailsVo hsnDetailsVo = new HsnDetailsVo();
		hsnDetailsVo.setId(hsnDetails.getId());
		hsnDetailsVo.setHsnCode(hsnDetails.getHsnCode());
		hsnDetailsVo.setDescription(hsnDetails.getDescription());
		hsnDetailsVo.setTaxAppliedType(hsnDetails.getTaxAppliedType());
		hsnDetailsVo.setTaxAppliesOn(hsnDetails.getTaxAppliesOn());
		if(hsnDetailsVo.getTaxId()!=null) {
			hsnDetailsVo.setTaxId(hsnDetails.getTax().getId());
			}
		hsnDetailsVo.setCreatedDate(hsnDetails.getCreatedDate());
		hsnDetailsVo.setLastModifiedDate(hsnDetails.getLastModifiedDate());
		hsnDetailsVo.setCreatedBy(hsnDetails.getCreatedBy());
		hsnDetailsVo.setModifiedBy(hsnDetails.getModifiedBy());
		hsnDetailsVo.setClientId(hsnDetails.getClientId());
		return hsnDetailsVo;
	}

	/*
	 * to convert list dto's to vo's
	 */
	public List<HsnDetailsVo> EntityToVo(List<HsnDetails> hsnDetailsList) {
		return hsnDetailsList.stream().map(hsnDetails -> EntityToVo(hsnDetails)).collect(Collectors.toList());

	}

	/*
	 * mapVoToEntity converts vo to dto
	 * 
	 */

	public HsnDetails mapVoToEntity(HsnDetailsVo hsnDetailsVo) {
		HsnDetails hsnDetails = new HsnDetails();
		if (hsnDetailsVo.getId() != 0)
			hsnDetails.setId(hsnDetailsVo.getId());
		hsnDetails.setHsnCode(hsnDetailsVo.getHsnCode());
		hsnDetails.setDescription(hsnDetailsVo.getDescription());
		hsnDetails.setTaxAppliedType(hsnDetailsVo.getTaxAppliedType());
		hsnDetails.setTaxAppliesOn(hsnDetailsVo.getTaxAppliesOn());
		return hsnDetails;

	}

	/*
	 * to convert list vo's to dto's
	 */
	public List<HsnDetails> mapVoToEntity(List<HsnDetailsVo> hsnDetailsVos) {
		return hsnDetailsVos.stream().map(hsnDetailsVo -> mapVoToEntity(hsnDetailsVo)).collect(Collectors.toList());

	}

}
