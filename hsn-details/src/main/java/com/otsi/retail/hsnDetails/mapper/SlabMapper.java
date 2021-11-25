/*
 * mapper for slab 
*/
package com.otsi.retail.hsnDetails.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.otsi.retail.hsnDetails.model.Slab;
import com.otsi.retail.hsnDetails.vo.SlabVo;

/**
 * @author vasavi
 *
 */
@Component
public class SlabMapper {
	
	@Autowired
	private TaxMapper taxMapper;
	/*
	 * EntityToVo converts dto to vo
	 * 
	 */
	public SlabVo EntityToVo(Slab dto) {
		SlabVo vo = new SlabVo();
		vo.setId(dto.getId());
		vo.setPriceFrom(dto.getPriceFrom());
		vo.setPriceTo(dto.getPriceTo());
		vo.setTaxVo(taxMapper.EntityToVo(dto.getTax()));
		return vo;

	}

	/*
	 * to convert list dto's to vo's
	 */

	public List<SlabVo> EntityToVo(List<Slab> dtos) {
		return dtos.stream().map(dto -> EntityToVo(dto)).collect(Collectors.toList());

	}

	/*
	 * VoToEntity converts vo to dto
	 * 
	 */

	public Slab VoToEntity(SlabVo vo) {
		Slab dto = new Slab();
		dto.setId(vo.getId());
		dto.setPriceFrom(vo.getPriceFrom());
		dto.setPriceTo(vo.getPriceTo());
		dto.setTax(taxMapper.VoToEntity(vo.getTaxVo()));
		return dto;

	}
	/*
	 * to convert list vo's to dto's
	 */

	public List<Slab> VoToEntity(List<SlabVo> vos) {
		return vos.stream().map(vo -> VoToEntity(vo)).collect(Collectors.toList());

	}
}
