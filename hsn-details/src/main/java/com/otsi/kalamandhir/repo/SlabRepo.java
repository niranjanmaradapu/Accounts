/**
 * repository for Slab
 */
package com.otsi.kalamandhir.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.kalamandhir.model.Slab;

/**
 * @author vasavi
 *
 */
@Repository
public interface SlabRepo extends JpaRepository<Slab, Long> {

}
