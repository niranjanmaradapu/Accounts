/*
 * mapper for tax
*/
package com.otsi.retail.hsnDetails.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import com.otsi.retail.hsnDetails.model.Tax;
import com.otsi.retail.hsnDetails.vo.TaxVo;

/**
 * @author vasavi
 *
 */
@Component
public class TaxMapper {
	/*
	 * EntityToVo
	 */
	public TaxVo EntityToVo(Tax dto) {
		TaxVo vo = new TaxVo();
		/* BeanUtils.copyProperties(vo, dto); */
		vo.setId(dto.getId());
		vo.setIgst(dto.getIgst());
		vo.setCess(dto.getCess());
		vo.setCgst(dto.getCgst());
		vo.setSgst(dto.getSgst());
		vo.setTaxLabel(dto.getTaxLabel());

		return vo;

	}

	/*
	 * list of EntityToVo to convert dto's to vo's
	 */
	public List<TaxVo> EntityToVo(List<Tax> dtos) {
		return dtos.stream().map(dto -> EntityToVo(dto)).collect(Collectors.toList());

	}

	/*
	 * 
	 * VoToEntity
	 * 
	 */
	public Tax VoToEntity(TaxVo vo) {
		Tax dto = new Tax();
		/* BeanUtils.copyProperties(dto, vo); */
		dto.setId(vo.getId());
		dto.setIgst(vo.getIgst());
		dto.setCess(vo.getCess());
		dto.setCgst(vo.getCgst());
		dto.setSgst(vo.getSgst());
		dto.setTaxLabel(vo.getTaxLabel());
		return dto;
	}
	/*
	 * list of VoToEntity to convert vo's to dto's
	 */

	public List<Tax> VoToEntity(List<TaxVo> vos) {
		return vos.stream().map(vo -> VoToEntity(vo)).collect(Collectors.toList());

	}

}
