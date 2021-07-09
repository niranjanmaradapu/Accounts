/**
 * repository for Hsn_details
 */
package com.otsi.kalamandhir.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.otsi.kalamandhir.model.HsnDetails;

/**
 * @author vasavi
 *
 */
@Repository
public interface HsnDetailsRepo extends JpaRepository<HsnDetails, Long> {

}
