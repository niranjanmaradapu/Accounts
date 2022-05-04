/*
 * mapper for HsnDetails
 * 
*/
package com.otsi.retail.hsnDetails.mapper;

import org.springframework.stereotype.Component;

import com.otsi.retail.hsnDetails.model.HsnDetails;
import com.otsi.retail.hsnDetails.vo.HsnDetailsVo;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author vasavi
 *
 */
@Component
public class HsnDetailsMapper {
	
	@Autowired
	private TaxMapper taxmapper;

	/*
	 * EntityToVo converts dto to vo
	 * 
	 */

	public HsnDetailsVo EntityToVo(HsnDetails dto) {
		
		HsnDetailsVo vo = new HsnDetailsVo();
		vo.setId(dto.getId());
		vo.setHsnCode(dto.getHsnCode());
		vo.setDescription(dto.getDescription());
		vo.setTaxAppliedType(dto.getTaxAppliedType()); 
		vo.setTaxAppliesOn(dto.getTaxAppliesOn());
		vo.setTaxVo(taxmapper.EntityToVo(dto.getTax()));
		return vo;
	}
	/*
	 * to convert list dto's to vo's
	 */
	public List<HsnDetailsVo> EntityToVo(List<HsnDetails> dtos) {
		return dtos.stream().map(dto -> EntityToVo(dto)).collect(Collectors.toList());

	}

	/*
	 * mapVoToEntity converts vo to dto
	 * 
	 */

	public HsnDetails mapVoToEntity(HsnDetailsVo vo) {
		Random ran=new Random();
		HsnDetails dto = new HsnDetails();
		if (vo.getId() != 0)
			dto.setId(vo.getId());
		dto.setHsnCode(vo.getHsnCode());
		dto.setDescription(vo.getDescription());
		dto.setId(vo.getId());
		dto.setTaxAppliedType(vo.getTaxAppliedType()); 
		dto.setTaxAppliesOn(vo.getTaxAppliesOn());
		dto.setTax(taxmapper.VoToEntity(vo.getTaxVo()));
		return dto;

	}
	
	/*
	 * to convert list vo's to dto's
	 */
	public List<HsnDetails> mapVoToEntity(List<HsnDetailsVo> vos) {
		return vos.stream().map(vo -> mapVoToEntity(vo)).collect(Collectors.toList());

	}

}
