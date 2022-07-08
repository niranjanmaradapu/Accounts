/**
 * repository for Slab
 */
package com.otsi.retail.hsnDetails.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.hsnDetails.model.Slab;

/**
 * @author vasavi
 *
 */
@Repository
public interface SlabRepository extends JpaRepository<Slab, Long> {

	List<Slab> findByHsnDetailsId(Long id);

}
