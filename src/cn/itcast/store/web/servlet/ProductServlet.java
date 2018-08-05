package cn.itcast.store.web.servlet;

import cn.itcast.store.domain.Product;
import cn.itcast.store.service.ProductService;
import cn.itcast.store.service.serviceImp.ProductServiceImp;
import cn.itcast.store.utils.PageModel;
import cn.itcast.store.web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductServlet extends BaseServlet {
	private ProductService productService =new ProductServiceImp();

	public String findByPid(HttpServletRequest req, HttpServletResponse resp)throws Exception{
		String pid = req.getParameter("pid");
        Product product=productService.findByPid(pid);
        req.setAttribute("product",product);
       return "jsp/product_info.jsp";
	}
	//findProductsByCidWithPage
	public String findProductsByCidWithPage(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		//获取cid,num  获取商品分类的ID  从第几页开始拿 默认第一页就是全部显示
       String cid = req.getParameter("cid"); String num = req.getParameter("num");
       int curNum = Integer.parseInt(num);

		//调用业务层功能:以分页形式查询当前类别下商品信息
		PageModel pageModel=productService.findProductsByCidWithPage(cid,curNum);
		//返回PageModel对象(1_当前页商品信息2_分页3_url)

		//将PageModel对象放入request
		req.getSession().setAttribute("page",pageModel);

		//转发到/jsp/product_list.jsp
        return "jsp/product_list.jsp";
	}


}
