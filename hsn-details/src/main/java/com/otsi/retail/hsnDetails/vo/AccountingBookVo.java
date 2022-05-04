package com.otsi.retail.hsnDetails.vo;

import java.time.LocalDateTime;
import java.util.List;
import com.otsi.retail.hsnDetails.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountingBookVo extends BaseEntityVo {

	private Long accountingBookId;

	private Long amount;

	private Long storeId;

	private Long customerId;

	private AccountType accountType;

	private Long createdBy;

	private LocalDateTime createdDate;

	private Long modifiedBy;

	private LocalDateTime lastModifiedDate;

	private List<LedgerLogBookVo> ledgerLogBooks;
}
