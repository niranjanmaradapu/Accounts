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
	public TaxVo EntityToVo(Tax tax) {
		TaxVo taxVo = new TaxVo();
		/* BeanUtils.copyProperties(vo, dto); */
		taxVo.setId(tax.getId());
		taxVo.setIgst(tax.getIgst());
		taxVo.setCess(tax.getCess());
		taxVo.setCgst(tax.getCgst());
		taxVo.setSgst(tax.getSgst());
		taxVo.setTaxLabel(tax.getTaxLabel());
		taxVo.setClientId(tax.getClientId());

		return taxVo;

	}

	/*
	 * list of EntityToVo to convert dto's to vo's
	 */
	public List<TaxVo> EntityToVo(List<Tax> taxs) {
		return taxs.stream().map(tax -> EntityToVo(tax)).collect(Collectors.toList());

	}

	/*
	 * 
	 * VoToEntity
	 * 
	 */
	public Tax VoToEntity(TaxVo taxVo) {
		Tax tax = new Tax();
		/* BeanUtils.copyProperties(dto, vo); */
		tax.setId(taxVo.getId());
		tax.setIgst(taxVo.getIgst());
		tax.setCess(taxVo.getCess());
		tax.setCgst(taxVo.getCgst());
		tax.setSgst(taxVo.getSgst());
		tax.setTaxLabel(taxVo.getTaxLabel());
		
		return tax;
	}
	/*
	 * list of VoToEntity to convert vo's to dto's
	 */

	public List<Tax> VoToEntity(List<TaxVo> taxVos) {
		return taxVos.stream().map(taxVo -> VoToEntity(taxVo)).collect(Collectors.toList());

	}

}
