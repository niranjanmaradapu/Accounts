/**
 * repository for Slab
 */
package com.otsi.retail.hsnDetails.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.hsnDetails.model.Slab;

/**
 * @author vasavi
 *
 */
@Repository
public interface SlabRepo extends JpaRepository<Slab, Long> {

	List<Slab> findByHsnDetailsId(long id);

}
