/*
 * mapper for HsnDetails
 * 
*/
package com.otsi.kalamandhir.mapper;

import org.springframework.stereotype.Component;
import com.otsi.kalamandhir.model.HsnDetails;
import com.otsi.kalamandhir.vo.HsnDetailsVo;

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

	public HsnDetailsVo EntityToVo(HsnDetails dto) {

		HsnDetailsVo vo = new HsnDetailsVo();
		vo.setId(dto.getId());
		vo.setHsnCode(dto.getHsnCode());
		vo.setDescription(dto.getDescription());
		vo.setId(dto.getId());
		vo.setSlabBased(dto.isSlabBased());
		vo.setTaxAppliesOn(dto.getTaxAppliesOn());
		return vo;
	}

	/*
	 * mapVoToEntity converts vo to dto
	 * 
	 */

	public HsnDetails mapVoToEntity(HsnDetailsVo vo) {

		HsnDetails dto = new HsnDetails();
		if (vo.getId() != 0)
			dto.setId(vo.getId());
		dto.setHsnCode(vo.getHsnCode());
		dto.setDescription(vo.getDescription());
		dto.setId(vo.getId());
		dto.setSlabBased(vo.isSlabBased());
		dto.setTaxAppliesOn(vo.getTaxAppliesOn());
		dto.setTax(vo.getTaxVo().getId());
		return dto;

	}

}
