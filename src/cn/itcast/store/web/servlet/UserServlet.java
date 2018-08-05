package cn.itcast.store.web.servlet;

import cn.itcast.store.domain.User;
import cn.itcast.store.service.UserService;
import cn.itcast.store.service.serviceImp.UserServiceImp;
import cn.itcast.store.utils.CookUtils;
import cn.itcast.store.utils.MailUtils;
import cn.itcast.store.utils.MyBeanUtils;
import cn.itcast.store.utils.UUIDUtils;
import cn.itcast.store.web.base.BaseServlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UserServlet extends BaseServlet {
	 private   UserService userService = new UserServiceImp();

	public String registUI(HttpServletRequest req, HttpServletResponse resp){

		return "/jsp/register.jsp";
	}

	public String userRegist(HttpServletRequest req, HttpServletResponse resp){
		//接受参数
		User user = new User();
		Map<String,String[]>map = req.getParameterMap();
		user.setUid(UUIDUtils.getId());
		user.setCode(UUIDUtils.getCode());
		user.setState(0);
		MyBeanUtils.populate(user,map);
		System.out.println(user);

		//UserService userService = new UserServiceImp();
		try{
		    userService.regist(user);
		    req.setAttribute("msg","用户注册成功,请激活你的邮箱--");
		    //把激活码扔给他的code连接里
			MailUtils.sendMail(user.getEmail(),user.getCode());

		} catch (Exception e){
			req.setAttribute("msg","用户注册失败啦----");
		}
		return "/jsp/info.jsp";
	}
	public String active(HttpServletRequest req, HttpServletResponse resp)throws Exception{
		String activeCode = req.getParameter("code");
		//数据库里查是否有这个激活码 有就把他的状态设置为1 同时清空激活码

			User user=userService.checkActive(activeCode);
			if(user!=null) {
				req.setAttribute("msg", "恭喜你激活成功,请登录");
				user.setCode("");
				user.setState(1);
				userService.updateUser(user);
			}else{
				req.setAttribute("msg","激活失败,请重新激活啦");
			    return "jsp/info.jsp";
			}

		    return "jsp/login.jsp";
	}

	public String loginUI(HttpServletRequest req,HttpServletResponse resp){
    //登陆按钮 调整到 登陆的界面
		return "jsp/login.jsp";
	}

	//接受参数查看是否有用户
	public String login(HttpServletRequest req,HttpServletResponse resp) throws IOException, SQLException {
		User user01=MyBeanUtils.populate(User.class, req.getParameterMap());

		User user02 = userService.login(user01);
		if(null!=user02){
            //登陆成功 存在账户
			int active_id = user02.getState();
			//未激活账户
 			if(active_id==0){
				req.setAttribute("msg", "账户未激活,请激活你的账户!");
				return "/jsp/login.jsp";
			}
			//放到Session中..
			req.getSession().setAttribute("loginUser",user02);
			String autoLogin = req.getParameter("autoLogin");
			if("yes".equals(autoLogin)){
				Cookie ck=new Cookie("autoLogin",user02.getUsername()+"#"+user02.getPassword());
				ck.setPath("/store_7");
				ck.setMaxAge(23423424);
				resp.addCookie(ck);
			}
			String remUser=req.getParameter("remUser");
			if("yes".equals(remUser)){
				//用户选中记住账户..先放着不搞他
				Cookie ck=new Cookie("remUser",user02.getUsername());
				ck.setPath("/store_v4");
				ck.setMaxAge(23423424);
				resp.addCookie(ck);

			}
			resp.sendRedirect(req.getContextPath()+"/index.jsp");

		}else{
			req.setAttribute("msg", "用户名和密码不匹配!");
			return "/jsp/login.jsp";
		}

		return null;
	}

	public String LogOut(HttpServletRequest req,HttpServletResponse resp) throws IOException {
		req.getSession().invalidate();
		Cookie ck=CookUtils.getCookieByName("autoLogin", req.getCookies());
		if(null!=ck){
			ck.setMaxAge(0);
			ck.setPath("/store_7");
			resp.addCookie(ck);
		}
		resp.sendRedirect(req.getContextPath()+"/jsp/index.jsp");
        return null;
	}


}
