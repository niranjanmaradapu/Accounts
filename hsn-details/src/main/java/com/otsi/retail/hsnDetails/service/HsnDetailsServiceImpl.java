/**
 * 
 */
package com.otsi.retail.hsnDetails.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.otsi.retail.hsnDetails.enums.Description;
import com.otsi.retail.hsnDetails.enums.TaxAppliedType;
import com.otsi.retail.hsnDetails.enums.TaxAppliesOn;
import com.otsi.retail.hsnDetails.exceptions.DataNotFoundException;
import com.otsi.retail.hsnDetails.exceptions.DuplicateRecordException;
import com.otsi.retail.hsnDetails.exceptions.InvalidDataException;
import com.otsi.retail.hsnDetails.exceptions.RecordNotFoundException;
import com.otsi.retail.hsnDetails.mapper.HsnDetailsMapper;
import com.otsi.retail.hsnDetails.mapper.SlabMapper;
import com.otsi.retail.hsnDetails.mapper.TaxMapper;
import com.otsi.retail.hsnDetails.model.HsnDetails;
import com.otsi.retail.hsnDetails.model.Slab;
import com.otsi.retail.hsnDetails.model.Tax;
import com.otsi.retail.hsnDetails.repo.HsnDetailsRepo;
import com.otsi.retail.hsnDetails.repo.SlabRepo;
import com.otsi.retail.hsnDetails.repo.TaxRepo;
import com.otsi.retail.hsnDetails.vo.EnumVo;
import com.otsi.retail.hsnDetails.vo.HsnDetailsVo;

/**
 * @author vasavi
 *
 */
@Component
public class HsnDetailsServiceImpl implements HsnDetailsService {

	private Logger log = LogManager.getLogger(HsnDetailsServiceImpl.class);

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

	@PersistenceContext
	EntityManager em;

	/*
	 * save functionality for hsn_details.In this,will save tax and slab in
	 * hsn_details
	 */
	@Override
	public String hsnSave(HsnDetailsVo hsnDetailsVo) {
		log.debug("debugging hsnSave:" + hsnDetailsVo);

		boolean hsncode = hsnDetailsRepo.existsByHsnCode(hsnDetailsVo.getHsnCode());
		if (hsncode) {
			throw new DuplicateRecordException("given hsn code already exist " + hsnDetailsVo.getHsnCode());
		}

		if (hsnDetailsVo.getTaxAppliedType().equals(TaxAppliedType.Priceslab)) {
			validateTaxSlabs(hsnDetailsVo);
		}
		List<Slab> slabs = new ArrayList<>();
		HsnDetails dto = hsnDetailsMapper.mapVoToEntity(hsnDetailsVo);
		HsnDetails save = hsnDetailsRepo.save(dto);
		// if isSlabBased is true,it will print hsnDetails,slab and tax otherwise,it
		// will only print hsn and tax details
		if (hsnDetailsVo.getTaxAppliedType().equals(TaxAppliedType.Priceslab) && !hsnDetailsVo.getSlabVos().isEmpty()) {
			hsnDetailsVo.getSlabVos().forEach(s -> {

				Slab slab = slabMapper.VoToEntity(s);
				slab.setHsnDetails(save);
				slabs.add(slabRepo.save(slab));

			});
		}
		hsnDetailsVo = hsnDetailsMapper.EntityToVo(save);
		hsnDetailsVo.setSlabVos(slabMapper.EntityToVo(slabs));
		hsnDetailsVo.setTaxVo(taxMapper.EntityToVo(save.getTax()));
		log.warn("we are checking,if hsn details is saved...");
		log.info("after saving hsn details:" + hsnDetailsVo.toString());
		return "hsn-details saved succesfully:" + save.getId();
	}

	protected String getSaltString() {
		String NUMERIC = "1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 8) { // length of the random string.
			int index = (int) (rnd.nextFloat() * NUMERIC.length());
			salt.append(NUMERIC.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	/*
	 * update functionality for hsn_details
	 */
	@Override
	public String hsnUpdate(HsnDetailsVo vo) {
		log.debug(" debugging hsnUpdate:" + vo);
		List<Slab> slabs = new ArrayList<>();

		Optional<HsnDetails> dto = hsnDetailsRepo.findById(vo.getId());
		// if id is not present,it will throw custom exception "RecordNotFoundException"
		if (!dto.isPresent()) {
			log.error("Record Not Found");
			throw new RecordNotFoundException("Record Not Found");
		}
		// if id is present,it will update data based on id.
		HsnDetails update = hsnDetailsMapper.mapVoToEntity(vo);
		HsnDetails save = hsnDetailsRepo.save(update);
		update.setTax(taxMapper.VoToEntity(vo.getTaxVo()));
		// here,will loop
		vo.getSlabVos().stream().forEach(vos -> {
			Slab slab = new Slab();
			slab.setId(vos.getId());
			slab.setPriceFrom(vos.getPriceFrom());
			slab.setPriceTo(vos.getPriceTo());
			slab.setTax(taxMapper.VoToEntity(vos.getTaxVo()));
			slab.setHsnDetails(save);
			Optional<Tax> tax = taxRepo.findById(vos.getTaxVo().getId());
			if (tax.isPresent()) {
				slab.setTax(tax.get());
			}
			vos.setId(slab.getId());
			slabs.add(slab);
			slabRepo.save(slab);
		});
		vo = hsnDetailsMapper.EntityToVo(save);
		vo.setTaxVo(taxMapper.EntityToVo(save.getTax()));
		vo.setSlabVos(slabMapper.EntityToVo(slabs));
		log.warn("wea re checking if hsn details is updated..");
		log.info("after updating hsn details:" + vo.toString());
		return "hsn-details updated successfully:" + save.getId();
	}

	/*
	 * fetching functionality for description and taxAppliesOn fields
	 */
	@Override
	public List<EnumVo> getEnums(String enumName) {
		log.debug(" debugging getEnums:" + enumName);
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
		{
			log.error("no data found");
			throw new DataNotFoundException("no data found");
		}
		log.warn("we are checking if enumvos is fetching based on enumName...");
		log.info("after fetching enumVos based on enumName:" + enumName + "enumVos:" + enumVos);
		return enumVos;
	}

	/*
	 * delete functionality for hsn_details
	 */
	@Override
	public String hsnDelete(long id) {
		log.debug(" debugging hsnDelete:" + id);
		Optional<HsnDetails> hsnOpt = hsnDetailsRepo.findById(id);
		// if id is present,it will delete that id information only
		if (!hsnOpt.isPresent()) {
			log.error("hsn details not found with id");
			throw new RecordNotFoundException("hsn details not found with id: " + id);
		} else {
			hsnDetailsRepo.delete(hsnOpt.get());
			log.warn("we are checking if hsn is deleted based on id...");
			log.info("deleted succesfully:" + id);
			return "deleted successfully with id:" + id;
		}
	}

	/*
	 * get functionality for hsn_details
	 */
	@Override
	public List<HsnDetailsVo> getHsnDetails() {
		log.debug(" debugging getHsnDetails");
		List<HsnDetails> hsnDetails = new ArrayList<>();
		List<HsnDetailsVo> voList = new ArrayList<>();
		// here, find all details through repository
		hsnDetails = hsnDetailsRepo.findAll();
		voList = hsnDetailsMapper.EntityToVo(hsnDetails);
		// here,will loop based on hsn id
		voList.stream().forEach(t -> {
			t.setSlabVos(slabMapper.EntityToVo(slabRepo.findByHsnDetailsId(t.getId())));
		});
		log.warn("we are checking if hsn details is fetching...");
		log.info("after getting hsn details:" + voList);
		return voList;
	}

	@Override
	public List<HsnDetailsVo> getAllHsnDetails(String hsnCode) {

		log.debug(" debugging getHsnDetails:" + hsnCode);
		List<HsnDetails> hsnDetails = new ArrayList<>();
		List<HsnDetailsVo> voList = new ArrayList<>();
		// here, find all details through repository
		hsnDetails = hsnDetailsRepo.findByHsnCode(hsnCode);
		voList = hsnDetailsMapper.EntityToVo(hsnDetails);
		// here,will loop based on hsn id
		voList.stream().forEach(t -> {
			t.setSlabVos(slabMapper.EntityToVo(slabRepo.findByHsnDetailsId(t.getId())));
		});
		log.warn("we are checking if hsn details is fetching...");
		log.info("after getting hsn details:" + voList);
		return voList;

	}

	/**
	 * validate tax slab
	 * 
	 * @param hsnDetails
	 * 
	 */
	private void validateTaxSlabs(HsnDetailsVo hsnDetails) {
		hsnDetails.getSlabVos().stream().forEach(slabVO -> {

			if (slabVO.getPriceFrom() >= slabVO.getPriceTo()) {
				throw new InvalidDataException("Invalid slab range price from is greater than price to");
			}
			List<Slab> slabList = slabRepo.findAll();
			slabList.stream().forEach(slab -> {

				// check slab already exists with same prices
				if (slabVO.getPriceFrom() == slab.getPriceFrom() && slabVO.getPriceTo() == slab.getPriceTo()) {
					log.error("price from and price to is already exists:" + slabVO.getPriceFrom() + "and"
							+ slabVO.getPriceTo());
					throw new DuplicateRecordException("price from and price to is already exists:"
							+ slabVO.getPriceFrom() + "and" + slabVO.getPriceTo());

				}

				// check if priceFrom exits in between other slab range
				else if (slabVO.getPriceFrom() >= slab.getPriceFrom() && slabVO.getPriceTo() <= slab.getPriceTo()) {
					log.error("price from  exists in other slab range :" + slabVO.getPriceFrom() + "and"
							+ slabVO.getPriceTo());
					throw new DuplicateRecordException("price from  exists in other slab range :"
							+ slabVO.getPriceFrom() + "and" + slabVO.getPriceTo());

				}

				// check if priceTo exits in between other slab range
				else if (slabVO.getPriceTo() >= slab.getPriceFrom() && slabVO.getPriceTo() <= slab.getPriceTo()) {
					log.error("price to  exists in other slab range :" + slabVO.getPriceFrom() + "and"
							+ slabVO.getPriceTo());
					throw new DuplicateRecordException("price to  exists in other slab range :" + slabVO.getPriceFrom()
							+ "and" + slabVO.getPriceTo());

				}

				// check if other slabs exits in this range
				else if (slab.getPriceFrom() >= slabVO.getPriceFrom() && slab.getPriceTo() <= slabVO.getPriceTo()) {
					log.error(" other slabs exists in the slab range :" + slabVO.getPriceFrom() + "and"
							+ slabVO.getPriceTo());
					throw new DuplicateRecordException(" other slabs exists in the slab range :" + slabVO.getPriceFrom()
							+ "and" + slabVO.getPriceTo());
				}

			});

		});
	}

}
