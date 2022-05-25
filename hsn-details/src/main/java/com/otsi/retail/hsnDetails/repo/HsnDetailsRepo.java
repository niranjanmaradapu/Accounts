/**
 * repository for Hsn_details
 */
package com.otsi.retail.hsnDetails.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.otsi.retail.hsnDetails.enums.TaxAppliedType;
import com.otsi.retail.hsnDetails.model.HsnDetails;

/**
 * @author vasavi
 *
 */
@Repository
public interface HsnDetailsRepo extends JpaRepository<HsnDetails, Long> {

	@Query(value = "select * from hsn_details h where h.hsn_code like :hsnCode%", nativeQuery = true)
	List<HsnDetails> findByHsnCode(String hsnCode);

	boolean existsByHsnCode(String hsnCode);

	Page<HsnDetails> findByClientId(Long clientId, Pageable pageable);

	Page<HsnDetails> findByHsnCodeAndClientId(String hsnCode, Long clientId, Pageable pageable);

	Page<HsnDetails> findByDescription(String description, Pageable pageable);

	Page<HsnDetails> findByTaxAppliedType(TaxAppliedType taxAppliedType, Pageable pageable);

}
