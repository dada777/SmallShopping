package cn.itcast.store.web.servlet;

import cn.itcast.store.domain.Cart;
import cn.itcast.store.domain.CartItem;
import cn.itcast.store.domain.Product;
import cn.itcast.store.service.ProductService;
import cn.itcast.store.service.serviceImp.ProductServiceImp;
import cn.itcast.store.web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CartServlet extends BaseServlet {
	//添加购物项到购物车
	public String addCartItemToCart(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      //从session获取购物车
		Cart cart = (Cart) req.getSession().getAttribute("cart");
		//如果获取不到,创建购物车对象,放在session中
		if(cart==null){
			cart = new Cart();
			req.getSession().setAttribute("cart",cart);
		}
		//如果获取到,使用即可

		//获取到商品id,数量
		String quantity = req.getParameter("quantity");
		String pid =req.getParameter("pid");
		//通过商品id查询都商品对象
        int num = Integer.parseInt(quantity);
		ProductService productService = new ProductServiceImp();
		Product product = productService.findByPid(pid);
		//获取到待购买的购物项
		CartItem cartItem = new CartItem();
		cartItem.setProduct(product);
		cartItem.setNum(num);
		//价格会自动算出来 无需设置..
		//cartItem.setSubTotal(num*product.getShop_price());
		//调用购物车上的方法
		cart.addCartItemToCar(cartItem);
		//重定向到/jsp/cart.jsp
		resp.sendRedirect(req.getContextPath()+"/jsp/cart.jsp");
		//return "/jsp/cart.jsp";
		return  null;
	}
	//马云爸爸清空你的购物车----  只是清空..
	public String clearCart(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Cart cart=(Cart)req.getSession().getAttribute("cart");
		cart.clearCart();
		resp.sendRedirect(req.getContextPath()+"/jsp/cart.jsp");
		return null;
	}

	public String removeCartItem(HttpServletRequest req, HttpServletResponse resp)throws Exception{
		//获取待删除商品pid
		String pid = req.getParameter("id");
		//获取到购物车
		Cart cart = (Cart) req.getSession().getAttribute("cart");
		//调用购物车删除购物项方法
		cart.removeCartItem(pid);
		resp.sendRedirect(req.getContextPath()+"/jsp/cart.jsp");
        return  null;
	}

}

