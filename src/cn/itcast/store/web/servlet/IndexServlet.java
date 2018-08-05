package cn.itcast.store.web.servlet;

import cn.itcast.store.domain.Category;
import cn.itcast.store.domain.Product;
import cn.itcast.store.service.CategoryService;
import cn.itcast.store.service.ProductService;
import cn.itcast.store.service.serviceImp.CategoryServiceImp;
import cn.itcast.store.service.serviceImp.ProductServiceImp;
import cn.itcast.store.web.base.BaseServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class IndexServlet extends BaseServlet {

   @Override
	public String execute(HttpServletRequest req, HttpServletResponse resp)throws Exception{
	     ProductService productService = new ProductServiceImp();

	   List<Product> hotList= productService.findByHot();
		List<Product> newList= productService.findByNew();
		req.setAttribute("hotList",hotList);
		req.setAttribute("newList",newList);
		//查询全部分类
		CategoryService CategoryService=new CategoryServiceImp();
		List<Category> list=CategoryService.findAllCats();
		//放入request域对象
		req.setAttribute("allCats", list);
		//转发到/jsp/index.jsp
		return "/jsp/index.jsp";
	}
}
