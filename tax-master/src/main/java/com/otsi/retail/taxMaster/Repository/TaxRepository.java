/**
 * repository for Tax
 */

package com.otsi.retail.taxMaster.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.taxMaster.model.TaxModel;


/**
 * @author Lakshmi
 */
@Repository
public interface TaxRepository extends JpaRepository<TaxModel, Long> {

	TaxModel save(Optional<TaxModel> tax);

}
