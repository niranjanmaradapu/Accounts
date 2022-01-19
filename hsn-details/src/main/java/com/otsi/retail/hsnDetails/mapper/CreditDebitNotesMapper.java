/**
 * mapper for CreditDebitNotes
 */
package com.otsi.retail.hsnDetails.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.otsi.retail.hsnDetails.model.CreditDebitNotes;
import com.otsi.retail.hsnDetails.vo.CreditDebitNotesVo;

/**
 * @author vasavi
 *
 */
@Component
public class CreditDebitNotesMapper {

	/*
	 * EntityToVo converts dto to vo
	 * 
	 */

	public CreditDebitNotesVo EntityToVo(CreditDebitNotes dto) {
		CreditDebitNotesVo vo = new CreditDebitNotesVo();
		vo.setCreditDebitId(dto.getCreditDebitId());
		vo.setComments(dto.getComments());
		vo.setFromDate(dto.getCreatedDate());
		vo.setToDate(dto.getLastModifiedDate());
		vo.setCreditDebit(dto.getCreditDebit());
		vo.setAmount(dto.getAmount());
		vo.setCustomerName(dto.getCustomerName());
		vo.setMobileNumber(dto.getMobileNumber());
		vo.setApprovedBy(dto.getApprovedBy());
		vo.setStoreId(dto.getStoreId());
		vo.setCustomerId(dto.getCustomerId());
		return vo;
	}

	public List<CreditDebitNotesVo> EntityToVo(List<CreditDebitNotes> dtos) {
		return dtos.stream().map(dto -> EntityToVo(dto)).collect(Collectors.toList());

	}

	/*
	 * mapVoToEntity converts vo to dto
	 * 
	 */

	public CreditDebitNotes VoToEntity(CreditDebitNotesVo vo) {
		CreditDebitNotes dto = new CreditDebitNotes();
		dto.setCreditDebitId(vo.getCreditDebitId());
		dto.setComments(vo.getComments());
		dto.setCreatedDate(LocalDate.now());
		dto.setLastModifiedDate(LocalDate.now());
		dto.setCreditDebit(vo.getCreditDebit());
		dto.setAmount(vo.getAmount());
		dto.setCustomerName(vo.getCustomerName());
		dto.setMobileNumber(vo.getMobileNumber());
		dto.setApprovedBy(vo.getApprovedBy());
		dto.setStoreId(vo.getStoreId());
		dto.setCreditDebit(vo.getCreditDebit());
		dto.setCustomerId(vo.getCustomerId());
		return dto;

	}

	public List<CreditDebitNotes> VoToEntity(List<CreditDebitNotesVo> vos) {
		return vos.stream().map(vo -> VoToEntity(vo)).collect(Collectors.toList());

	}

}
