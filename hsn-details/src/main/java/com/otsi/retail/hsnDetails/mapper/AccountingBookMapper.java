package com.otsi.retail.hsnDetails.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.boot.jaxb.internal.stax.LocalXmlResourceResolver;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.model.AccountingBook;
import com.otsi.retail.hsnDetails.model.LedgerLogBook;
import com.otsi.retail.hsnDetails.vo.AccountingBookVO;
import com.otsi.retail.hsnDetails.vo.LedgerLogBookVO;

/**
 * @author vasavi
 *
 */
@Component
public class AccountingBookMapper {

	@Autowired
	private LedgerLogBookMapper ledgerLogBookMapper;

	/*
	 * mapVoToEntity converts vo to dto
	 * 
	 */

	public AccountingBook voToEntity(AccountingBookVO accountingBookVo) {
		AccountingBook accountingBook = new AccountingBook();
		accountingBook.setCustomerId(accountingBookVo.getCustomerId());
		accountingBook.setAmount(accountingBookVo.getAmount());
		accountingBook.setAccountType(accountingBookVo.getAccountType());
		accountingBook.setStoreId(accountingBookVo.getStoreId());
		return accountingBook;
	}

	public List<AccountingBook> VoToEntity(List<AccountingBookVO> accountingBookVos) {
		return accountingBookVos.stream().map(accountingBookVo -> voToEntity(accountingBookVo))
				.collect(Collectors.toList());
	}

	/*
	 * EntityToVo converts dto to vo
	 * 
	 */

	public AccountingBookVO entityToVo(AccountingBook accountingBook) {
		AccountingBookVO accountingBookVo = new AccountingBookVO();
		accountingBookVo.setAccountingBookId(accountingBook.getAccountingBookId());
		accountingBookVo.setCustomerId(accountingBook.getCustomerId());
		accountingBookVo.setAmount(accountingBook.getAmount());
		accountingBookVo.setAccountType(accountingBook.getAccountType());
		accountingBookVo.setStoreId(accountingBook.getStoreId());
		accountingBookVo.setCreatedDate(accountingBook.getCreatedDate());
		accountingBookVo.setLastModifiedDate(accountingBook.getLastModifiedDate());
		accountingBookVo.setUsedAmount(accountingBook.getUsedAmount());
		return accountingBookVo;

	}

	public List<AccountingBookVO> entityToVo(List<AccountingBook> accountingBooks) {
		return accountingBooks.stream().map(accountingBook -> entityToVo(accountingBook)).collect(Collectors.toList());
	}

	public AccountingBook voToEntityUpdate(AccountingBookVO accountingBookVo) {
		AccountingBook accountingBook = new AccountingBook();
		accountingBook.setAccountingBookId(accountingBookVo.getAccountingBookId());
		accountingBook.setCustomerId(accountingBookVo.getCustomerId());
		accountingBook.setAmount(accountingBookVo.getAmount());
		accountingBook.setAccountType(accountingBookVo.getAccountType());
		accountingBook.setStoreId(accountingBookVo.getStoreId());
		return accountingBook;
	}
}
