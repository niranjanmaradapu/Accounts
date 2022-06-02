package com.otsi.retail.hsnDetails.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class UserDetailsVO {
	private Long userId;
	
	private String userName;
	
	private String phoneNumber;
	
	private String gender;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime lastModifiedDate;
	
	private String createdBy;
	
	private Boolean isActive;
	
	private Boolean isSuperAdmin;
	
	private Boolean isCustomer;
	
	private Role role;
	
	private List<ClientDomains> clientDomians;
	
	private List<UserAv> userAv;
	
	private List<StoreVO> stores;
	
	private StoreVO ownerOf;
	
	private Long modifiedBy;

}
