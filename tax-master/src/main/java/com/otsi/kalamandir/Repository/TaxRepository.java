/**
 * repository for Tax
 */

package com.otsi.kalamandir.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.kalamandir.model.TaxModel;


/**
 * @author Lakshmi
 */
@Repository
public interface TaxRepository extends JpaRepository<TaxModel, Long> {

	TaxModel save(Optional<TaxModel> tax);

}
