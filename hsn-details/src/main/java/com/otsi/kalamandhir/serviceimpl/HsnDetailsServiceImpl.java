/**
 * service implementation class for HsnDetails
 *
 */
package com.otsi.kalamandhir.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.otsi.kalamandhir.enums.Description;
import com.otsi.kalamandhir.enums.TaxAppliesOn;
import com.otsi.kalamandhir.exceptions.RecordNotFoundException;
import com.otsi.kalamandhir.mapper.HsnDetailsMapper;
import com.otsi.kalamandhir.mapper.SlabMapper;
import com.otsi.kalamandhir.mapper.TaxMapper;
import com.otsi.kalamandhir.model.HsnDetails;
import com.otsi.kalamandhir.model.Slab;
import com.otsi.kalamandhir.model.Tax;
import com.otsi.kalamandhir.repo.HsnDetailsRepo;
import com.otsi.kalamandhir.repo.SlabRepo;
import com.otsi.kalamandhir.repo.TaxRepo;
import com.otsi.kalamandhir.service.HsnDetailsService;
import com.otsi.kalamandhir.vo.EnumVo;
import com.otsi.kalamandhir.vo.HsnDetailsVo;
import com.otsi.kalamandhir.vo.SlabVo;
import com.otsi.kalamandhir.vo.TaxVo;

/**
 * @author vasavi
 *
 */
@Component
public class HsnDetailsServiceImpl implements HsnDetailsService {

	private Logger log = LoggerFactory.getLogger(HsnDetailsServiceImpl.class);

	@Autowired
	private HsnDetailsRepo hsnDetailsRepo;

	@Autowired
	private HsnDetailsMapper hsnDetailsMapper;

	@Autowired
	private TaxRepo taxRepo;

	@Autowired
	private SlabRepo slabRepo;

	@Autowired
	private TaxMapper taxMapper;

	@Autowired
	private SlabMapper slabMapper;

	/*
	 * save functionality for hsn_details.In this,will save tax and slab in
	 * hsn_details
	 */
	@Override
	public HsnDetailsVo hsnSave(HsnDetailsVo vo) {

		List<SlabVo> slabVos = new ArrayList<>();
		HsnDetails dto = hsnDetailsMapper.mapVoToEntity(vo);
		HsnDetails save = hsnDetailsRepo.save(dto);
		Optional<Tax> taxOpt = taxRepo.findById(vo.getTaxVo().getId());
		//
		if (taxOpt.isPresent()) {
			save.setTax(taxOpt.get().getId());
		}
		/*
		 * if vo.isSlabBased() is true,it will save hsn_details along with Slab
		 * information.Otherwise it will save hsn_details information only
		 */
		if (vo.isSlabBased()) {
			// will set tax_id and hsn_id to slab entity
			vo.getSlabVos().stream().forEach(vos -> {
				Slab slab = new Slab();
				slab.setPriceFrom(vos.getPriceFrom());
				slab.setPriceTo(vos.getPriceTo());
				slab.setHsnDetails(save);
				Optional<Tax> tax = taxRepo.findById(vos.getTaxVo().getId());
				if (tax.isPresent()) {
					slab.setTax(tax.get().getId());
				}
				slab = slabRepo.save(slab);
				TaxVo taxVo = taxMapper.EntityToVo(tax.get());
				vos.setId(slab.getId());
				vos.setTaxVo(taxVo);
				slabVos.add(vos);
			});
		}
		vo = hsnDetailsMapper.EntityToVo(save);
		vo.setTaxVo(taxMapper.EntityToVo(taxOpt.get()));
		vo.setSlabVos(slabVos);
		log.info("after saving" + vo.toString());
		return vo;
	}

	/*
	 * update functionality for hsn_details
	 */
	@Override
	public HsnDetailsVo hsnUpdate(HsnDetailsVo vo) {
		List<SlabVo> slabVos = new ArrayList<>();
		try {
			Optional<HsnDetails> dto = hsnDetailsRepo.findById(vo.getId());
			// if id is not present,it will throw custom exception "RecordNotFoundException"
			if (!dto.isPresent()) {

				throw new RecordNotFoundException("Record Not Found");
			}
			// if id is present,it will save data based on id.
			HsnDetails update = hsnDetailsMapper.mapVoToEntity(vo);
			HsnDetails save = hsnDetailsRepo.save(update);
			Optional<Tax> taxOpt = taxRepo.findById(vo.getTaxVo().getId());
			// tax id is present,it will update tax
			if (taxOpt.isPresent()) {
				update.setTax(taxOpt.get().getId());

			}
			// will set tax_id and hsn_id to slab entity
			vo.getSlabVos().stream().forEach(vos -> {
				Slab slab = new Slab();
				slab.setId(vos.getId());
				slab.setPriceFrom(vos.getPriceFrom());
				slab.setPriceTo(vos.getPriceTo());
				slab.setTax(vos.getTaxVo().getId());
				slab.setHsnDetails(save);
				slabMapper.EntityToVo(slabRepo.save(slab));
				slab.setHsnDetails(save);
				Optional<Tax> tax = taxRepo.findById(vos.getTaxVo().getId());
				if (tax.isPresent()) {
					slab.setTax(tax.get().getId());
				}
				TaxVo taxUpdate = taxMapper.EntityToVo(taxOpt.get());
				vos.setId(slab.getId());
				vos.setTaxVo(taxUpdate);
				slabVos.add(vos);
			});

			vo = hsnDetailsMapper.EntityToVo(save);
			vo.setTaxVo(taxMapper.EntityToVo(taxOpt.get()));
			vo.setSlabVos(slabVos);
			log.info("after updating" + vo.toString());
			return vo;
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}

	}

	/*
	 * fetching functionality for description and taxAppliesOn fields
	 */
	@Override
	public List<EnumVo> getEnums(String enumName) {

		List<EnumVo> enumVos = new ArrayList<>();
		// if we pass "description" keyword through link,it will show "GOODS" and
		// "SERVICES"
		if (enumName.equalsIgnoreCase("description")) {
			Description[] list = Description.values();
			List<Description> list1 = Arrays.asList(list);
			list1.stream().forEach(li -> {
				EnumVo vo = new EnumVo();
				vo.setId(li.getId());
				vo.setName(li.getName());
				enumVos.add(vo);
			});
			// if we pass "taxAppliesOn" keyword through link,it will show "NET PRICE" and
			// "RSP"
		} else if (enumName.equalsIgnoreCase("taxAppliesOn")) {
			TaxAppliesOn[] list = TaxAppliesOn.values();
			List<TaxAppliesOn> list1 = Arrays.asList(list);
			list1.stream().forEach(li -> {
				EnumVo vo = new EnumVo();
				vo.setId(li.getId());
				vo.setName(li.getName());
				enumVos.add(vo);
			});
		} else
			// if we didn't pass "description" and "taxAppliesOn",it will throw
			// "RuntimeException"
			throw new RuntimeException("no data found");

		return enumVos;
	}

	/*
	 * delete functionality for hsn_details
	 */

	@Override
	public String hsnDelete(long id) {

		Optional<HsnDetails> hsnOpt = hsnDetailsRepo.findById(id);
		// if id is present,it will delete that id information only
		if (hsnOpt.isPresent()) {

			HsnDetails dto = new HsnDetails();
			hsnDetailsRepo.delete(hsnOpt.get());
			log.info("deleted succesfully" + id);
			return "deleted successfully with id:" + id;

		} else {
			// if id is not present,it will throw error
			throw new RuntimeException("hsn details not found with id: " + id);
		}

	}

}
