/**
 * Repository for Tax
 *
 */
package com.otsi.kalamandhir.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.otsi.kalamandhir.model.Tax;

/**
 * @author vasavi
 *
 */
@Repository
public interface TaxRepo extends JpaRepository<Tax, Long> {

}
