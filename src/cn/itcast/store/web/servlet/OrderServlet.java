package cn.itcast.store.web.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.Cart;
import cn.itcast.store.domain.CartItem;
import cn.itcast.store.domain.Order;
import cn.itcast.store.domain.OrderItem;
import cn.itcast.store.domain.User;
import cn.itcast.store.service.OrderService;
import cn.itcast.store.service.serviceImp.OrderServiceImp;
import cn.itcast.store.utils.PageModel;
import cn.itcast.store.utils.PaymentUtil;
import cn.itcast.store.utils.UUIDUtils;
import cn.itcast.store.web.base.BaseServlet;

public class OrderServlet extends BaseServlet {

private 	OrderService OrderService=new OrderServiceImp();
	
	public String saveOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		 //获取用户 session
		   User user = (User) req.getSession().getAttribute("loginUser");
		   if(user==null){
		   	req.setAttribute("msg","请先登录,再购物哦");
		   	return "/jsp/login.jsp";
		   }
		 //获取到购物车
           Cart cart = (Cart) req.getSession().getAttribute("cart");
		 //创建订单对象
		   Order order = new Order();
		 //为订单对象赋予值: oid,orderTime,state 1 未付款
           order.setOid(UUIDUtils.getId()); order.setOrderTime(new Date()); order.setState(1);
		 //获取到购物车中的总计为订单对象下的总计赋值
		   order.setTotal(cart.getTotal());
		  //为订单对象关联用户
           order.setUser(user);
		 //遍历购物车中的所有购物项
		   for(CartItem cartItem:cart.getCartItems()){
			   //遍历的同时创建订单项
              OrderItem orderItem = new OrderItem();
              orderItem.setItemid(UUIDUtils.getId());
              orderItem.setQuantity(cartItem.getNum());
              orderItem.setProduct(cartItem.getProduct());
              orderItem.setOrder(order);
              orderItem.setTotal(cartItem.getSubTotal());
			   order.getList().add(orderItem);
		   }
		OrderService.saveOrder(order);

		//清空购物车
		cart.clearCart();
		//向request内放置一份order,便于在订单详情页面显示订单信息
		req.getSession().setAttribute("order", order);
		return "/jsp/order_info.jsp";
		}


	//findOrdersByUidWithPage
	public String findOrdersByUidWithPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取数据
		int curNum=Integer.parseInt(request.getParameter("num"));
		User user=(User)request.getSession().getAttribute("loginUser");
		  //调用业务层功能,返回PageModel对象(1_分页参数2_所有订单3_url)
		PageModel pm=OrderService.findOrdersByUidWithPage(user,curNum);
		  //将PageModel对象放入request,转发到order_list.jsp页面
		request.setAttribute("page", pm);
		return "/jsp/order_list.jsp";
	}
	//findOrderByOid
	public String findOrderByOid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//服务端获取oid,根据oid查询订单(订单上携带当前订单上所有的订单项以及商品),
		String oid=request.getParameter("oid");
		Order order=OrderService.findOrderByOid(oid);
		  //将订单放入request内
		request.setAttribute("order", order);
		  //转发到订单详情页面order_info.jsp
		return "/jsp/order_info.jsp";
	}
	//payOrder
	public String payOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//服务端获取到收货人姓名,电话,地址,订单编号,银行信息
		String name=request.getParameter("name");
		String telephone=request.getParameter("telephone");
		String address=request.getParameter("address");
		String oid=request.getParameter("oid");
		String pd_FrpId=request.getParameter("pd_FrpId");
		//根据获取订单编号获取订单信息,更新订单上的收货人姓名,电话,地址,
		Order order = OrderService.findOrderByOid(oid);
		order.setName(name);
		order.setTelephone(telephone);
		order.setAddress(address);
		//更新数据库中当前订单上收货人相关信
		OrderService.updateOrder(order);
		//调用相关的支付代码
				
				// 把付款所需要的参数准备好:
				String p0_Cmd = "Buy";
				//商户编号
				String p1_MerId = "10001126856";
				//订单编号
				String p2_Order = oid;
				//金额
				String p3_Amt = "0.01";
				String p4_Cur = "CNY";
				String p5_Pid = "";
				String p6_Pcat = "";
				String p7_Pdesc = "";
				//接受响应参数的Servlet
				String p8_Url = "http://localhost:8080/store_v4/OrderServlet?method=callBack";
				String p9_SAF = "";
				String pa_MP = "";
				String pr_NeedResponse = "1";
				//公司的秘钥 md5
				String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
					
				//调用易宝的加密算法,对所有数据进行加密,返回电子签名
				String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
						
				StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
				sb.append("p0_Cmd=").append(p0_Cmd).append("&");
				sb.append("p1_MerId=").append(p1_MerId).append("&");
				sb.append("p2_Order=").append(p2_Order).append("&");
				sb.append("p3_Amt=").append(p3_Amt).append("&");
				sb.append("p4_Cur=").append(p4_Cur).append("&");
				sb.append("p5_Pid=").append(p5_Pid).append("&");
				sb.append("p6_Pcat=").append(p6_Pcat).append("&");
				sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
				sb.append("p8_Url=").append(p8_Url).append("&");
				sb.append("p9_SAF=").append(p9_SAF).append("&");
				sb.append("pa_MP=").append(pa_MP).append("&");
				sb.append("pd_FrpId=").append(pd_FrpId).append("&");
				sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
				sb.append("hmac=").append(hmac);

				System.out.println(sb.toString());
				// 使用重定向：
				response.sendRedirect(sb.toString());

				//response.sendRedirect("https://www.yeepay.com/app-merchant-proxy/node?p0_cmd=Buy&p1_MerId=111111&k1=v1&k2=v2");
		//服务端重新定向到易宝支付,同时向易宝支付传递了数据
		return null;
	}
	
	public String callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 验证请求来源和数据有效性
				// 阅读支付结果参数说明
				// System.out.println("==============================================");
				String p1_MerId = request.getParameter("p1_MerId");
				String r0_Cmd = request.getParameter("r0_Cmd");
				String r1_Code = request.getParameter("r1_Code");
				String r2_TrxId = request.getParameter("r2_TrxId");
				String r3_Amt = request.getParameter("r3_Amt");
				String r4_Cur = request.getParameter("r4_Cur");
				String r5_Pid = request.getParameter("r5_Pid");
				String r6_Order = request.getParameter("r6_Order");
				String r7_Uid = request.getParameter("r7_Uid");
				String r8_MP = request.getParameter("r8_MP");
				String r9_BType = request.getParameter("r9_BType");
				String rb_BankId = request.getParameter("rb_BankId");
				String ro_BankOrderId = request.getParameter("ro_BankOrderId");
				String rp_PayDate = request.getParameter("rp_PayDate");
				String rq_CardNo = request.getParameter("rq_CardNo");
				String ru_Trxtime = request.getParameter("ru_Trxtime");

				// hmac
				String hmac = request.getParameter("hmac");
				// 利用本地密钥和加密算法 加密数据
				String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
				boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
						r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
						r8_MP, r9_BType, keyValue);
				if (isValid) {
					// 有效
					if (r9_BType.equals("1")) {
						// 浏览器重定向
						//response.setContentType("text/html;charset=utf-8");
						//response.getWriter().println(	"支付成功！订单号：" + r6_Order + "金额：" + r3_Amt);
						Order order=OrderService.findOrderByOid(r6_Order);
						//跟新订单状态
						order.setState(2);
						OrderService.updateOrder(order);
						request.setAttribute("msg", "支付成功！订单号：" + r6_Order + "金额：" + r3_Amt);
						return "/jsp/info.jsp";
							
					} else if (r9_BType.equals("2")) {
						// 修改订单状态:
						// 服务器点对点，来自于易宝的通知
						System.out.println("收到易宝通知，修改订单状态！");//
						// 回复给易宝success，如果不回复，易宝会一直通知
						response.getWriter().print("success");
					}

				} else {
					throw new RuntimeException("数据被篡改！");
				}
				
				return null;
	}	
	
	
}