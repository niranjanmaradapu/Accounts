/**
 * service implementation class for HsnDetails
 *
 */
package com.otsi.retail.hsnDetails.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.otsi.retail.hsnDetails.enums.Description;
import com.otsi.retail.hsnDetails.enums.TaxAppliesOn;
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
import com.otsi.retail.hsnDetails.service.HsnDetailsService;
import com.otsi.retail.hsnDetails.vo.EnumVo;
import com.otsi.retail.hsnDetails.vo.HsnDetailsVo;
import com.otsi.retail.hsnDetails.vo.SlabVo;
import com.otsi.retail.hsnDetails.vo.TaxVo;

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
       log.debug("debugging hsnSave:"+vo);
		List<Slab> slabs = new ArrayList<>();
		HsnDetails dto = hsnDetailsMapper.mapVoToEntity(vo);
		HsnDetails save = hsnDetailsRepo.save(dto);
		// if isSlabBased is true,it will print hsnDetails,slab and tax otherwise,it
		// will only print hsn and tax details
		if (vo.isSlabBased() && !vo.getSlabVos().isEmpty()) {
			vo.getSlabVos().forEach(s -> {

				Slab slab = slabMapper.VoToEntity(s);
				slab.setHsnDetails(save);
				slabs.add(slabRepo.save(slab));

			});
		}
		vo = hsnDetailsMapper.EntityToVo(save);
		vo.setSlabVos(slabMapper.EntityToVo(slabs));
		vo.setTaxVo(taxMapper.EntityToVo(save.getTax()));
		log.info("after saving hsn details:" + vo.toString());
		return vo;
	}

	/*
	 * update functionality for hsn_details
	 */
	@Override
	public HsnDetailsVo hsnUpdate(HsnDetailsVo vo) {
		log.debug(" debugging hsnUpdate:" + vo);
		List<Slab> slabs = new ArrayList<>();
		try {
			Optional<HsnDetails> dto = hsnDetailsRepo.findById(vo.getId());
			// if id is not present,it will throw custom exception "RecordNotFoundException"
			if (!dto.isPresent()) {

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
			log.info("after updating hsn details:" + vo.toString());
			return vo;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new RuntimeException(ex.getMessage());
		}
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
			throw new RuntimeException("no data found");
		}
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
		if (hsnOpt.isPresent()) {
			hsnDetailsRepo.delete(hsnOpt.get());
			log.info("deleted succesfully:" + id);
			return "deleted successfully with id:" + id;

		} else {
			// if id is not present,it will throw error
			log.error("hsn details not found with id: " + id);
			throw new RuntimeException("hsn details not found with id: " + id);
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
		//here,will loop basedon hsn id
		voList.stream().forEach(t -> {
			t.setSlabVos(slabMapper.EntityToVo(slabRepo.findByHsnDetailsId(t.getId())));
		});
		log.info("after getting hsn details:" + voList);
		return voList;
	}
	
	/*
	 * get functionality for tax_details
	 */

	@Override
	public List<TaxVo> getTaxDetails() {
		List<Tax> taxs = new ArrayList<>();
		List<TaxVo> VOList = new ArrayList<>();
		//here,will find all tax's through taxRepo
		taxs = taxRepo.findAll();
		//here,will map and assign to VOList and return voList
		VOList = taxMapper.EntityToVo(taxs);
		log.info("after getting tax details:"+VOList);
        return VOList;
	}
}
