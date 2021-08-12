/**
 * Repository for Tax
 *
 */
package com.otsi.retail.hsnDetails.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.hsnDetails.model.Tax;

/**
 * @author vasavi
 *
 */
@Repository
public interface TaxRepo extends JpaRepository<Tax, Long> {

}
