/**
 * mapper for Tax
 */
package com.otsi.kalamandir.mapper;


import org.springframework.stereotype.Component;

import com.otsi.kalamandir.model.TaxModel;
import com.otsi.kalamandir.vo.TaxVo;
/**
 * @author Lakshmi
 */
@Component
public class TaxMapper {

	/*
	 * convertVoToEntity converts vo to entity
	 * 
	 */

	public TaxModel convertVoToEntity(TaxVo taxvo) {
		TaxModel taxmodel = new TaxModel();
		taxmodel.setId(taxvo.getId());
		taxmodel.setTaxLable(taxvo.getTaxLable());
		taxmodel.setSgst(taxvo.getSgst());
		taxmodel.setCgst(taxvo.getCgst());
		taxmodel.setIgst(taxvo.getIgst());
		taxmodel.setCess(taxvo.getCess());

		// BeanUtils.copyProperties(taxvo, taxmodel);

		return taxmodel;

	}

	/*
	 * convertEntityToVo converts entity to vo
	 * 
	 */
	public TaxVo convertEntityToVo(TaxModel taxmodel) {

		TaxVo taxvo = new TaxVo();
		taxvo.setId(taxmodel.getId());
		taxvo.setTaxLable(taxmodel.getTaxLable());
		taxvo.setSgst(taxmodel.getSgst());
		taxvo.setCgst(taxmodel.getCgst());
		taxvo.setIgst(taxmodel.getIgst());
		taxvo.setCess(taxmodel.getCess());

		// BeanUtils.copyProperties(taxvo, taxmodel);
		return taxvo;

	}

}
