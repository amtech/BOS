package cn.tf.bos.service.impl.bc;


import java.util.List;
import java.util.Random;

import cn.tf.bos.domain.bc.Region;
import cn.tf.bos.domain.bc.Staff;
import cn.tf.bos.domain.bc.Standard;
import cn.tf.bos.page.PageRequestBean;
import cn.tf.bos.page.PageResponseBean;
import cn.tf.bos.service.BaseService;
import cn.tf.bos.service.bc.RegionService;
import cn.tf.bos.service.bc.StaffService;


public class RegionServiceImpl extends BaseService  implements RegionService{

	public void saveRegion(Region region) {
		regionDao.save(region);
		
	}

	@Override
	public PageResponseBean findByPage(PageRequestBean pageRequestBean) {
		
		return pageQuery(pageRequestBean, regionDao);
	}


	
}
