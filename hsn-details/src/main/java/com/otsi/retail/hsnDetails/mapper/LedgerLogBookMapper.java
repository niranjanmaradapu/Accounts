package com.otsi.retail.hsnDetails.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.otsi.retail.hsnDetails.model.LedgerLogBook;
import com.otsi.retail.hsnDetails.vo.LedgerLogBookVo;

@Component
public class LedgerLogBookMapper {

	/*
	 * mapVoToEntity converts vo to dto
	 * 
	 */

	public LedgerLogBook voToEntity(LedgerLogBookVo ledgerLogBookVo) {
		LedgerLogBook ledgerLogBook = new LedgerLogBook();
		ledgerLogBook.setCreatedBy(ledgerLogBookVo.getCreatedBy());
		ledgerLogBook.setCreatedDate(ledgerLogBookVo.getCreatedDate());
		ledgerLogBook.setLastModifiedDate(ledgerLogBookVo.getCreatedDate());
		ledgerLogBook.setCustomerId(ledgerLogBookVo.getCustomerId());
		ledgerLogBook.setStoreId(ledgerLogBookVo.getStoreId());
		ledgerLogBook.setTransactionType(ledgerLogBookVo.getTransactionType());
		ledgerLogBook.setModifiedBy(ledgerLogBookVo.getModifiedBy());
		return ledgerLogBook;
	}

	public List<LedgerLogBook> voToEntity(List<LedgerLogBookVo> ledgerLogBookVos) {
		return ledgerLogBookVos.stream().map(ledgerLogBookVo -> voToEntity(ledgerLogBookVo))
				.collect(Collectors.toList());
	}
	/*
	 * EntityToVo converts dto to vo
	 * 
	 */

	public LedgerLogBookVo entityToVo(LedgerLogBook ledgerLogBook) {
		LedgerLogBookVo ledgerLogBookVo = new LedgerLogBookVo();
		ledgerLogBookVo.setLedgerLogBookid(ledgerLogBook.getLedgerLogBookid());
		ledgerLogBookVo.setComments(ledgerLogBook.getComments());
		ledgerLogBookVo.setCreatedBy(ledgerLogBook.getCreatedBy());
		ledgerLogBookVo.setCreatedDate(ledgerLogBook.getCreatedDate());
		ledgerLogBookVo.setLastModifiedDate(ledgerLogBook.getCreatedDate());
		ledgerLogBookVo.setCustomerId(ledgerLogBook.getCustomerId());
		ledgerLogBookVo.setStoreId(ledgerLogBook.getStoreId());
		ledgerLogBookVo.setTransactionType(ledgerLogBook.getTransactionType());
		ledgerLogBookVo.setModifiedBy(ledgerLogBook.getModifiedBy());
		return ledgerLogBookVo;

	}

	public List<LedgerLogBookVo> entityToVo(List<LedgerLogBook> ledgerLogBooks) {
		return ledgerLogBooks.stream().map(ledgerLogBook -> entityToVo(ledgerLogBook)).collect(Collectors.toList());
	}

}
