package com.otsi.retail.hsnDetails.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import com.otsi.retail.hsnDetails.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "accounting_book")
public class AccountingBook extends BaseEntity {

	@Id
	@GeneratedValue
	private Long accountingBookId;

	private Long amount;

	private Long storeId;

	private Long customerId;

	@Enumerated(EnumType.STRING)
	private AccountType accountType;

}
