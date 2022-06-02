/*
 * mapper for tax
*/
package com.otsi.retail.hsnDetails.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.otsi.retail.hsnDetails.model.Tax;
import com.otsi.retail.hsnDetails.vo.TaxVO;

/**
 * @author vasavi
 *
 */
@Component
public class TaxMapper {

	public TaxVO entityToVO(Tax tax) {
		TaxVO taxVO = new TaxVO();
		taxVO.setId(tax.getId());
		taxVO.setIgst(tax.getIgst());
		taxVO.setCess(tax.getCess());
		taxVO.setCgst(tax.getCgst());
		taxVO.setSgst(tax.getSgst());
		taxVO.setTaxLabel(tax.getTaxLabel());
		taxVO.setClientId(tax.getClientId());
		return taxVO;

	}

	public List<TaxVO> entityToVO(List<Tax> taxList) {
		return taxList.stream().map(tax -> entityToVO(tax)).collect(Collectors.toList());

	}

	public Tax voToEntity(TaxVO taxVO) {
		Tax tax = new Tax();
		tax.setId(taxVO.getId());
		tax.setIgst(taxVO.getIgst());
		tax.setCess(taxVO.getCess());
		tax.setCgst(taxVO.getCgst());
		tax.setSgst(taxVO.getSgst());
		tax.setTaxLabel(taxVO.getTaxLabel());

		return tax;
	}

	public List<Tax> VoToEntity(List<TaxVO> taxVOs) {
		return taxVOs.stream().map(taxVo -> voToEntity(taxVo)).collect(Collectors.toList());

	}

}
