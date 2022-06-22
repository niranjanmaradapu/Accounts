package com.otsi.retail.hsnDetails.vo;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class UserDetailsVO {

	private Long id;

	private String userName;

	private String phoneNumber;

	private String gender;

	private LocalDate createdDate;

	private LocalDate lastModifiedDate;

	private String createdBy;

	private Role role;

	private List<UserAv> userAv;

	private List<StoreVO> stores;

	private StoreVO ownerOf;

	private Boolean isActive;

	private Boolean isSuperAdmin;

	private Boolean isCustomer;

	private Long modifiedBy;

}
