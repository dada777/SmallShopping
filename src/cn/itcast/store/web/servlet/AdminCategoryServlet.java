package cn.itcast.store.web.servlet;

import cn.itcast.store.domain.Category;
import cn.itcast.store.service.CategoryService;
import cn.itcast.store.service.ProductService;
import cn.itcast.store.service.serviceImp.CategoryServiceImp;
import cn.itcast.store.service.serviceImp.ProductServiceImp;
import cn.itcast.store.utils.PageModel;
import cn.itcast.store.utils.UUIDUtils;
import cn.itcast.store.web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminCategoryServlet  extends BaseServlet {

	public String findAllCats(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		CategoryService categoryService = new CategoryServiceImp();
		List<Category> list =categoryService.findAllCats();
		request.getSession().setAttribute("allCats",list);
		return "/admin/category/list.jsp";
	}
	public String addCategoryUI(HttpServletRequest request, HttpServletResponse response){
		//空跳转
		return  "/admin/category/add.jsp";
	}
	public String addCategory(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String categoryName = request.getParameter("cname");
        String cid = UUIDUtils.getId();
		//获取分类名称
       //创建分类ID
     //调用业务层添加分类功能
		CategoryService categoryService = new CategoryServiceImp();
		Category category = new Category();
		category.setCid(cid);
		category.setCname(categoryName);
		categoryService.saveCat(category);
//重定向到查询全部分类信息
         response.sendRedirect("/AdminCategoryServlet?method=findAllCats");
		return null;
	}

}
