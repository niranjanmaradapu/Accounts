package com.otsi.retail.hsnDetails.vo;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserDetailsVO {

	private Long id;

	private String userName;

	private String phoneNumber;

	@JsonIgnore
	private String gender;
	@JsonIgnore
	private LocalDate createdDate;
	@JsonIgnore
	private LocalDate lastModifiedDate;
	@JsonIgnore
	private String createdBy;
	@JsonIgnore
	private String modifiedBy;
	@JsonIgnore
	private Boolean isActive;
	@JsonIgnore
	private Role role;
	@JsonIgnore
	private List<ClientDomains> clientDomians;
	@JsonIgnore
	private List<UserAv> userAv;
	@JsonIgnore
    private List<StoreVO> stores;
	@JsonIgnore
    private StoreVO ownerOf;
	@JsonIgnore
	private Boolean isSuperAdmin;
	@JsonIgnore
	private Boolean isCustomer;

}
