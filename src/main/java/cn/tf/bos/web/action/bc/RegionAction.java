package cn.tf.bos.web.action.bc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import cn.tf.bos.domain.bc.Region;
import cn.tf.bos.domain.bc.Staff;
import cn.tf.bos.domain.bc.Standard;
import cn.tf.bos.domain.user.User;
import cn.tf.bos.page.PageRequestBean;
import cn.tf.bos.page.PageResponseBean;
import cn.tf.bos.web.action.BaseAction;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;


//标准管理
public class RegionAction extends BaseAction  implements ModelDriven<Region>{
	
	private static  final Logger LOG=Logger.getLogger(RegionAction.class);
	private Region region=new Region();
	
	public Region getModel() {
		return region;
	}

	//分页查询
	public String findByPage(){
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Region.class);
		PageRequestBean pageRequestBean=initPageRequestBean(detachedCriteria);
		PageResponseBean  pageResponseBean=regionService.findByPage(pageRequestBean);
		ActionContext.getContext().put("pageResponseBean", pageResponseBean);

		return "findByPage";
	}
	
	
	
	//接收上传的数据
	public String OCimport() throws FileNotFoundException, IOException{
		// 1、 工作薄对象
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(upload));
		// 解析工作薄
		hssfWorkbook.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK); // 避免空指针异常

		// 2、 获得Sheet
		HSSFSheet sheet = hssfWorkbook.getSheetAt(0); // 获得第一个sheet

		// 3、遍历每一行
		for (Row row : sheet) {
			// 进行解析 ， 每一行数据，对应 Region 区域信息
			if (row.getRowNum() == 0) {// 第一行（表头，无需解析）
				continue;
			}
			// 从第二行 开始解析
			Region region = new Region();
			String id = row.getCell(0).getStringCellValue(); // 获得第一个单元格信息
			if (id.trim().equals("")) {
				// id 无值，跳过
				continue;
			}
			region.setId(id);
			region.setProvince(row.getCell(1).getStringCellValue());
			region.setCity(row.getCell(2).getStringCellValue());
			region.setDistrict(row.getCell(3).getStringCellValue());
			region.setPostcode(row.getCell(4).getStringCellValue());
			region.setShortcode(row.getCell(5).getStringCellValue());
			region.setCitycode(row.getCell(6).getStringCellValue());
			
			//保存数据时出错
			try {
				regionService.saveRegion(region);
			} catch (Exception e) {
				// 导入region失败，记录日志
				LOG.error("区域导入失败，编号：" + region.getId(), e);
			}
			
		}
		
		//返回json
		Map<String,Object>  map=new HashMap<String,Object>();
		map.put("result", "success");
		map.put("msg", "区域导入完成");
		ActionContext.getContext().put("map", map);
		
		return "OCimport";
	}
	
	
	private File upload;
	public void setUpload(File upload) {
		this.upload = upload;
	}
	

}
