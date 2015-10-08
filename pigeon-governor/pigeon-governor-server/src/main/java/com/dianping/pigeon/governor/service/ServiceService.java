package com.dianping.pigeon.governor.service;

import java.util.List;

import com.dianping.pigeon.governor.bean.JqGridRespBean;
import com.dianping.pigeon.governor.bean.ServiceBean;
import com.dianping.pigeon.governor.model.Service;
import com.dianping.pigeon.registry.exception.RegistryException;

public interface ServiceService {

	public int create(ServiceBean serviceBean, String updatezk) throws RegistryException;
	
	public int deleteByIdSplitByComma(String idsComma, String updatezk) throws RegistryException;
	
	public int updateById(ServiceBean serviceBean, String updatezk) throws RegistryException;
	
	public int deleteById(Integer id);
	
	
	public List<Service> retrieveAll();
	
	public Service retrieveById(Integer id);
	
	public List<Service> retrieveByPageAndRows(int page, int rows);
	
	public JqGridRespBean retrieveByJqGrid(int page, int rows);
	
	public List<Service> getServiceList(int projectId);
	
	public Service getService(String name, String group);
	
	public int updateById(Service service);
	
	public String publishService(String project, String service, String group, 
									String ip, String port, String updatezk) throws RegistryException;
	
	public int create(Service service);
	
	public String unpublishService(String service, String group, 
									String ip, String port, String updatezk) throws RegistryException;
	
	public JqGridRespBean retrieveByJqGrid(int page, int rows, String projectName);
	
}
