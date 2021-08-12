/**
 * repository for Hsn_details
 */
package com.otsi.retail.hsnDetails.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.hsnDetails.model.HsnDetails;

/**
 * @author vasavi
 *
 */
@Repository
public interface HsnDetailsRepo extends JpaRepository<HsnDetails, Long> {

}
