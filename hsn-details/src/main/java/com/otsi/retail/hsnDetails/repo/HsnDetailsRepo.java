/**
 * repository for Hsn_details
 */
package com.otsi.retail.hsnDetails.repo;

import java.util.List;
import java.util.Optional;

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

	List<HsnDetails> findByDescription(String description);

	List<HsnDetails> findByTaxAppliedType(TaxAppliedType taxAppliedType);

	Optional<HsnDetails> findByHsnCodeAndClientId(String hsnCode, Long clientId);

	List<HsnDetails> findByClientId(Long clientId);

}
