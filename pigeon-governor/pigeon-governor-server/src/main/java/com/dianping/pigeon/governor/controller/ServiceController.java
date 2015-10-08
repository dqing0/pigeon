package com.dianping.pigeon.governor.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dianping.pigeon.governor.bean.JqGridRespBean;
import com.dianping.pigeon.governor.bean.ServiceBean;
import com.dianping.pigeon.governor.bean.JqGridReqBean;
import com.dianping.pigeon.governor.bean.JqGridReqFilters;
import com.dianping.pigeon.governor.bean.WebResult;
import com.dianping.pigeon.governor.model.Project;
import com.dianping.pigeon.governor.model.Service;
import com.dianping.pigeon.governor.service.ProjectOwnerService;
import com.dianping.pigeon.governor.service.ProjectService;
import com.dianping.pigeon.governor.service.ServiceService;
import com.dianping.pigeon.governor.util.Constants;
import com.dianping.pigeon.registry.exception.RegistryException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ServiceController extends BaseController {
	
	private Logger log = LogManager.getLogger();
	
	@Autowired
	private ServiceService serviceService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private ProjectOwnerService projectOwnerService;
	
	@RequestMapping(value = {"/services/{projectName}"}, method = RequestMethod.GET)
	public String projectInfo(ModelMap modelMap,
								@PathVariable String projectName,
								HttpServletRequest request) {
		
		String currentUser = (String) request.getSession().getAttribute(Constants.DP_ACCOUNT);
		modelMap.addAttribute("currentUser", currentUser);
		Project project = projectService.findProject(projectName);
		
		if(project == null){
			modelMap.addAttribute("errorMsg", "找不到项目：" + projectName);
			
			return "/error/500";
		}
		
		modelMap.addAttribute("isProjectOwner", projectOwnerService.isProjectOwner(currentUser, project));
		modelMap.addAttribute("projectName", projectName);
		modelMap.addAttribute("projectId", project.getId());
		
		return "/services/list";
	}
	
	@RequestMapping(value = {"/services/{projectName}"}, method = RequestMethod.POST)
	@ResponseBody
	public JqGridRespBean servicesRetrieve(JqGridReqBean jqGridReqBean,
											@PathVariable String projectName) {
		
		/*JqGridReqFilters filters = null;
		
		if(StringUtils.isNotBlank(jqGridReqBean.getFilters())){
			
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				filters = objectMapper.readValue(jqGridReqBean.getFilters(), JqGridReqFilters.class);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		JqGridRespBean jqGridTableBean;
		
		int page = jqGridReqBean.getPage();
		int rows = jqGridReqBean.getRows();
		
		if(page > 0 && rows > 0){
			jqGridTableBean = serviceService.retrieveByJqGrid(page, rows, projectName);
		}else{
			jqGridTableBean = serviceService.retrieveByJqGrid(1, 10, projectName);
		}
		
		return jqGridTableBean;
	}
	
	@RequestMapping(value = {"/services.api/{projectId}"}, method = RequestMethod.POST)
	public void servicesapi4project(ServiceBean serviceBean,
									@PathVariable Integer projectId,
									HttpServletRequest request,
									HttpServletResponse response) {//设置为void的时候要设置response参数
		
		String oper = serviceBean.getOper();
		
		serviceBean.setProjectid(projectId);
		try {
			if("edit".equals(oper)){
				serviceService.updateById(serviceBean, "true");
				
			}else if("del".equals(oper)){
				serviceService.deleteByIdSplitByComma(serviceBean.getId(), "true");
			
			}else if("add".equals(oper)){
				serviceService.create(serviceBean, "true");
			
			}
		} catch (RegistryException e) {
			e.printStackTrace();
			log.error("update zk error");
		}
		
	}
	
	@RequestMapping(value = {"/services"}, method = RequestMethod.GET)
	public String allinone(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
		commonnav(modelMap, request);
		
		return "/services/index";
	}
	
	/*@RequestMapping(value = {"/services.api"}, method = RequestMethod.POST)
	public void servicesapi(ModelMap modelMap, ServiceBean serviceBean,
			HttpServletRequest request, HttpServletResponse response) {
		
		String oper = serviceBean.getOper();
		
		try {
			verifyIdentity(request, 3);
		} catch (Exception e) {
			
			e.printStackTrace();
			return ;
		}
		
		if("edit".equals(oper)){
			serviceService.updateById(serviceBean);
			
		}else if("del".equals(oper)){
			serviceService.deleteByIdSplitByComma(serviceBean.getId());
		
		}else if("add".equals(oper)){
			serviceService.create(serviceBean);
		
		}
		
	}*/
	
	@RequestMapping(value = {"/services.json"}, method = RequestMethod.POST)
	@ResponseBody
	public JqGridRespBean servicesjson(ModelMap modelMap, JqGridReqBean jqGridReqBean,
			HttpServletRequest request, HttpServletResponse response) {
		
		JqGridReqFilters filters = null;
		
		if(StringUtils.isNotBlank(jqGridReqBean.getFilters())){
			
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				filters = objectMapper.readValue(jqGridReqBean.getFilters(), JqGridReqFilters.class);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		JqGridRespBean jqGridTableBean;
		
		int page = jqGridReqBean.getPage();
		int rows = jqGridReqBean.getRows();
		
		if(page > 0 && rows > 0){
			jqGridTableBean = serviceService.retrieveByJqGrid(page, rows);
		}else{
			jqGridTableBean = serviceService.retrieveByJqGrid(1, 10);
		}
		
		return jqGridTableBean;
	}
	
}
