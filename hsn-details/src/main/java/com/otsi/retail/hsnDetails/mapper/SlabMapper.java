/*
 * mapper for slab 
*/
package com.otsi.retail.hsnDetails.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.otsi.retail.hsnDetails.model.Slab;
import com.otsi.retail.hsnDetails.vo.SlabVo;

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
	public SlabVo EntityToVo(Slab slab) {
		SlabVo slabVo = new SlabVo();
		slabVo.setId(slab.getId());
		slabVo.setPriceFrom(slab.getPriceFrom());
		slabVo.setPriceTo(slab.getPriceTo());
		slabVo.setTaxId(slab.getTax().getId());
		slabVo.setCreatedDate(slab.getCreatedDate());
		slabVo.setLastModifiedDate(slab.getLastModifiedDate());
		slabVo.setCreatedBy(slab.getCreatedBy());
		slabVo.setModifiedBy(slab.getModifiedBy());
		return slabVo;

	}

	/*
	 * to convert list dto's to vo's
	 */

	public List<SlabVo> EntityToVo(List<Slab> slabs) {
		return slabs.stream().map(slab -> EntityToVo(slab)).collect(Collectors.toList());

	}

	/*
	 * VoToEntity converts vo to dto
	 * 
	 */

	public Slab VoToEntity(SlabVo slabVo) {
		Slab slab = new Slab();
		slab.setId(slabVo.getId());
		slab.setPriceFrom(slabVo.getPriceFrom());
		slab.setPriceTo(slabVo.getPriceTo());
		return slab;

	}
	/*
	 * to convert list vo's to dto's
	 */

	public List<Slab> VoToEntity(List<SlabVo> slabVos) {
		return slabVos.stream().map(slabVo -> VoToEntity(slabVo)).collect(Collectors.toList());

	}
}
