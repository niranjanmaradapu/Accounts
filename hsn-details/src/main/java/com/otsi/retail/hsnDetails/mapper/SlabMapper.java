/*
 * mapper for slab 
*/
package com.otsi.retail.hsnDetails.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.otsi.retail.hsnDetails.model.Slab;
import com.otsi.retail.hsnDetails.vo.SlabVO;

/**
 * @author vasavi
 *
 */
@Component
public class SlabMapper {

	/*
	 * EntityToVo converts dto to vo
	 * 
	 */
	public SlabVO entityToVO(Slab slab) {
		SlabVO slabVO = new SlabVO();
		slabVO.setId(slab.getId());
		slabVO.setPriceFrom(slab.getPriceFrom());
		slabVO.setPriceTo(slab.getPriceTo());
		if (slab.getTax() != null) {
			slabVO.setTaxId(slab.getTax().getId());
		}
		slabVO.setCreatedDate(slab.getCreatedDate());
		slabVO.setLastModifiedDate(slab.getLastModifiedDate());
		slabVO.setCreatedBy(slab.getCreatedBy());
		slabVO.setModifiedBy(slab.getModifiedBy());
		return slabVO;

	}

	/*
	 * to convert list dto's to vo's
	 */

	public List<SlabVO> entityToVO(List<Slab> slabs) {
		return slabs.stream().map(slab -> entityToVO(slab)).collect(Collectors.toList());

	}

	/*
	 * VoToEntity converts vo to dto
	 * 
	 */

	public Slab voToEntity(SlabVO slabVO) {
		Slab slab = new Slab();
		slab.setId(slabVO.getId());
		slab.setPriceFrom(slabVO.getPriceFrom());
		slab.setPriceTo(slabVO.getPriceTo());
		return slab;

	}
	/*
	 * to convert list vo's to dto's
	 */

	public List<Slab> VoToEntity(List<SlabVO> slabVos) {
		return slabVos.stream().map(slabVo -> voToEntity(slabVo)).collect(Collectors.toList());

	}
}
