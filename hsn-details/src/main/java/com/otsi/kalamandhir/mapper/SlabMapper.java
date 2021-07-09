/*
 * mapper for slab 
*/
package com.otsi.kalamandhir.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.otsi.kalamandhir.model.Slab;
import com.otsi.kalamandhir.vo.SlabVo;

/**
 * @author vasavi
 *
 */
@Component
public class SlabMapper {
	/*
	 * EntityToVo converts dto to vo
	 * 
	 */
	public SlabVo EntityToVo(Slab dto) {
		SlabVo vo = new SlabVo();
		vo.setId(dto.getId());
		vo.setPriceFrom(dto.getPriceFrom());
		vo.setPriceTo(dto.getPriceTo());
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
		dto.setTax(vo.getTaxVo().getId());
		return dto;

	}
	/*
	 * to convert list vo's to dto's
	 */

	public List<Slab> VoToEntity(List<SlabVo> vos) {
		return vos.stream().map(vo -> VoToEntity(vo)).collect(Collectors.toList());

	}
}
