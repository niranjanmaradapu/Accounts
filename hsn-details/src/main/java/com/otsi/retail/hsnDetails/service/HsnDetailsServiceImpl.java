/**
 * 
 */
package com.otsi.retail.hsnDetails.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.otsi.retail.hsnDetails.enums.Description;
import com.otsi.retail.hsnDetails.enums.TaxAppliedType;
import com.otsi.retail.hsnDetails.enums.TaxAppliesOn;
import com.otsi.retail.hsnDetails.exceptions.DataNotFoundException;
import com.otsi.retail.hsnDetails.exceptions.DuplicateRecordException;
import com.otsi.retail.hsnDetails.exceptions.RecordNotFoundException;
import com.otsi.retail.hsnDetails.mapper.HsnDetailsMapper;
import com.otsi.retail.hsnDetails.mapper.SlabMapper;
import com.otsi.retail.hsnDetails.model.HsnDetails;
import com.otsi.retail.hsnDetails.model.Slab;
import com.otsi.retail.hsnDetails.model.Tax;
import com.otsi.retail.hsnDetails.repository.HsnDetailsRepository;
import com.otsi.retail.hsnDetails.repository.SlabRepository;
import com.otsi.retail.hsnDetails.repository.TaxRepository;
import com.otsi.retail.hsnDetails.vo.EnumVo;
import com.otsi.retail.hsnDetails.vo.HsnDetailsVO;

/**
 * @author vasavi
 *
 */
@Component
public class HsnDetailsServiceImpl implements HsnDetailsService {

	private Logger log = LogManager.getLogger(HsnDetailsServiceImpl.class);

	@Autowired
	private HsnDetailsRepository hsnDetailsRepository;

	@Autowired
	private HsnDetailsMapper hsnDetailsMapper;

	@Autowired
	private TaxRepository taxRepository;

	@Autowired
	private SlabRepository slabRepository;

	@Autowired
	private SlabMapper slabMapper;

	@PersistenceContext
	EntityManager em;

	/*
	 * save functionality for hsn_details.In this,will save tax and slab in
	 * hsn_details
	 */
	@Override
	public HsnDetailsVO hsnSave(HsnDetailsVO hsnDetailsVo, Long userId, Long clientId) {
		log.debug("debugging hsnSave:" + hsnDetailsVo);

		boolean hsncode = hsnDetailsRepository.existsByHsnCode(hsnDetailsVo.getHsnCode());
		if (hsncode) {
			throw new DuplicateRecordException("HSN code already exist " + hsnDetailsVo.getHsnCode());
		}

		if (hsnDetailsVo.getTaxAppliedType().equals(TaxAppliedType.Priceslab)) {
			validateTaxSlabs(hsnDetailsVo);
		}

		List<Slab> slabs = new ArrayList<>();

		HsnDetails hsnDetails = hsnDetailsMapper.mapVoToEntity(hsnDetailsVo);
		hsnDetails.setCreatedBy(userId);
		hsnDetails.setModifiedBy(userId);
		hsnDetails.setClientId(clientId);
		if (hsnDetailsVo.getTaxId() != null) {
			Optional<Tax> taxOpt = taxRepository.findById(hsnDetailsVo.getTaxId());

			if (hsnDetails.getTaxAppliedType().equals(TaxAppliedType.Hsncode)) {

				hsnDetails.setTax(taxOpt.get());
			}
		}
		HsnDetails hsnDetailsSave = hsnDetailsRepository.save(hsnDetails);
		// if isSlabBased is true,it will print hsnDetails,slab and tax otherwise,it
		// will only print hsn and tax details
		if (hsnDetailsVo.getTaxAppliedType().equals(TaxAppliedType.Priceslab) && !hsnDetailsVo.getSlabs().isEmpty()) {
			hsnDetailsVo.getSlabs().forEach(slabVO -> {

				Slab slab = slabMapper.voToEntity(slabVO);
				if (slabVO.getTaxId() != null) {
					Optional<Tax> taxOpt = taxRepository.findById(slabVO.getTaxId());
					slab.setTax(taxOpt.get());
				}
				slab.setHsnDetails(hsnDetailsSave);
				slabs.add(slabRepository.save(slab));

			});
		}

		hsnDetailsVo = hsnDetailsMapper.entityToVO(hsnDetailsSave);
		hsnDetailsVo.setSlabs(slabMapper.entityToVO(slabs));
		return hsnDetailsVo;
	}

	/*
	 * update functionality for hsn_details
	 */
	@Override
	public HsnDetailsVO hsnUpdate(HsnDetailsVO hsnDetailsVo) {
		log.debug(" debugging hsnUpdate:" + hsnDetailsVo);
		List<Slab> slabs = new ArrayList<>();

		Optional<HsnDetails> dto = hsnDetailsRepository.findById(hsnDetailsVo.getId());
		// if id is not present,it will throw custom exception "RecordNotFoundException"
		if (!dto.isPresent()) {
			log.error("Record Not Found");
			throw new RecordNotFoundException("Record Not Found");
		}
		// if id is present,it will update data based on id.
		HsnDetails hsnUpdate = hsnDetailsMapper.voToEntityUpdate(hsnDetailsVo);
		hsnUpdate.setClientId(dto.get().getClientId());
		if (hsnDetailsVo.getTaxId() != null) {
			Optional<Tax> taxOpt = taxRepository.findById(hsnDetailsVo.getTaxId());

			if (hsnDetailsVo.getTaxAppliedType().equals(TaxAppliedType.Hsncode)) {

				hsnUpdate.setTax(taxOpt.get());
			}
		}
		HsnDetails save = hsnDetailsRepository.save(hsnUpdate);

		// here,will loop

		if (hsnDetailsVo.getSlabs() != null && !hsnDetailsVo.getSlabs().isEmpty()) {
			hsnDetailsVo.getSlabs().stream().forEach(vos -> {
				Slab slab = new Slab();
				slab.setId(vos.getId());
				slab.setPriceFrom(vos.getPriceFrom());
				slab.setPriceTo(vos.getPriceTo());
				slab.setHsnDetails(save);
				if (vos.getTaxId() != null) {
					Optional<Tax> taxOpt = taxRepository.findById(vos.getTaxId());
					slab.setTax(taxOpt.get());
				}
				vos.setId(slab.getId());
				slabs.add(slabRepository.save(slab));
			});
			hsnDetailsVo.setSlabs(slabMapper.entityToVO(slabs));
		}
		hsnDetailsVo = hsnDetailsMapper.entityToVO(save);
		if (hsnDetailsVo.getTaxId() != null) {
			hsnDetailsVo.setTaxId(save.getTax().getId());
		}
		log.info("after updating hsn details:" + hsnDetailsVo.toString());
		return hsnDetailsVo;
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
		Optional<HsnDetails> hsnOpt = hsnDetailsRepository.findById(id);
		// if id is present,it will delete that id information only
		if (!hsnOpt.isPresent()) {
			log.error("hsn details not found with id");
			throw new RecordNotFoundException("hsn details not found with id: " + id);
		} else {
			hsnDetailsRepository.delete(hsnOpt.get());
			log.warn("we are checking if hsn is deleted based on id...");
			log.info("deleted succesfully:" + id);
			return "deleted successfully with id:" + id;
		}
	}

	/*
	 * get functionality for hsn_details
	 */
	@Override
	public List<HsnDetailsVO> getHsnDetails(String hsnCode, String description, TaxAppliedType taxAppliedType,
			Long clientId) {
		log.debug(" debugging getHsnDetails");
		log.info("clientId:" + clientId);
		List<HsnDetails> hsnDetails = new ArrayList<>();
		List<HsnDetailsVO> voList = new ArrayList<>();
		// here, find all details through repository
		if (hsnCode != null || description != null || taxAppliedType != null) {
			if (hsnCode != null) {
				Optional<HsnDetails> hsn = hsnDetailsRepository.findByHsnCodeAndClientId(hsnCode, clientId);
				if (hsn.isPresent()) {
					hsnDetails.add(hsn.get());
				}
			} else if (description != null)
				hsnDetails = hsnDetailsRepository.findByDescription(description);
			else if (taxAppliedType != null)
				hsnDetails = hsnDetailsRepository.findByTaxAppliedType(taxAppliedType);
		} else if (clientId != null) {
			hsnDetails = hsnDetailsRepository.findByClientId(clientId);
		}

		else {
			hsnDetails = hsnDetailsRepository.findAll();
		}

		voList = hsnDetailsMapper.entityToVO(hsnDetails);

		// here,will loop based on hsn id
		voList.stream().forEach(t -> {
			t.setSlabs(slabMapper.entityToVO(slabRepository.findByHsnDetailsId(t.getId())));
		});

		log.warn("we are checking if hsn details is fetching...");
		log.info("after getting hsn details:" + voList);
		return voList;

	}

	@Override
	public List<HsnDetailsVO> getAllHsnDetails(String hsnCode) {

		log.debug(" debugging getHsnDetails:" + hsnCode);
		List<HsnDetails> hsnDetails = new ArrayList<>();
		List<HsnDetailsVO> voList = new ArrayList<>();
		// here, find all details through repository
		hsnDetails = hsnDetailsRepository.findByHsnCode(hsnCode);
		voList = hsnDetailsMapper.entityToVO(hsnDetails);
		// here,will loop based on hsn id

		voList.stream().forEach(t -> {
			t.setSlabs(slabMapper.entityToVO(slabRepository.findByHsnDetailsId(t.getId())));
		});

		log.warn("we are checking if hsn details is fetching...");
		log.info("after getting hsn details:" + voList);
		return voList;

	}

	@Override
	public Map<String, Float> getHsnDetails(String hsnCode, Float itemPrice, Long clientId) {
		Optional<HsnDetails> hsnDetailsOptional = hsnDetailsRepository.findByHsnCodeAndClientId(hsnCode, clientId);
		Map<String, Float> taxMap = new HashMap<>();
		Tax tax = null;
		if (hsnDetailsOptional.isPresent()) {
			HsnDetails hsnDetails = hsnDetailsOptional.get();
			if (hsnDetails.getTaxAppliedType().equals(TaxAppliedType.Hsncode)) {
				tax = hsnDetails.getTax();
			}

			else if (hsnDetails.getTaxAppliedType().equals(TaxAppliedType.Priceslab) && itemPrice != null) {
				List<Slab> slabs = slabRepository.findByHsnDetailsId(hsnDetails.getId());
				for (Slab slab : slabs) {
					if (itemPrice >= slab.getPriceFrom() && itemPrice <= slab.getPriceTo()) {
						tax = slab.getTax();
						break;
					}
				}
			}
			if (tax != null) {
				taxMap.put("cgstPercent", tax.getCgst());
				taxMap.put("sgstPercent", tax.getSgst());
				taxMap.put("igstPercent", tax.getIgst());
				taxMap.put("cessPercent", tax.getCess());
				// if tax is included
				if (true) {
					taxMap.put("cgstValue", (tax.getCgst() > 0 ? (itemPrice * tax.getCgst()) / 100 : 0));
					taxMap.put("sgstValue", (tax.getSgst() > 0 ? (itemPrice * tax.getSgst()) / 100 : 0));
					taxMap.put("igstValue", (tax.getIgst() > 0 ? (itemPrice * tax.getIgst()) / 100 : 0));
					taxMap.put("cessValue", (tax.getCess() > 0 ? (itemPrice * tax.getCess()) / 100 : 0));
					taxMap.put("intrastate",
							itemPrice - taxMap.get("sgstValue") - taxMap.get("cgstValue") - taxMap.get("cessValue"));
					taxMap.put("interstate", itemPrice - taxMap.get("igstValue") - taxMap.get("cessValue"));

				}
				// todo if tax is excluded
				else {
					taxMap.put("cgstValue", (tax.getCgst() > 0 ? (itemPrice * tax.getCgst()) / 100 : 0));
					taxMap.put("sgstValue", (tax.getSgst() > 0 ? (itemPrice * tax.getSgst()) / 100 : 0));
					taxMap.put("igstValue", (tax.getIgst() > 0 ? (itemPrice * tax.getIgst()) / 100 : 0));
					taxMap.put("cessValue", (tax.getCess() > 0 ? (itemPrice * tax.getCess()) / 100 : 0));
					taxMap.put("intrastate",
							itemPrice + taxMap.get("sgstValue") + taxMap.get("cgstValue") + taxMap.get("cessValue"));
					taxMap.put("interstate", itemPrice + taxMap.get("igstValue") + taxMap.get("cessValue"));

				}
			}
		}
		return taxMap;

	}

	/**
	 * validate tax slab
	 * 
	 * @param hsnDetails
	 * 
	 */

	private void validateTaxSlabs(HsnDetailsVO hsnDetails) {
		hsnDetails.getSlabs().stream().forEach(slabVO -> {

			if (slabVO.getPriceFrom() >= slabVO.getPriceTo()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Invalid slab range price from is greater than price to");
			}

			List<Slab> slabList = slabRepository.findAll();
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
