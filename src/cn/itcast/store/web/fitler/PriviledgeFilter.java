package cn.itcast.store.web.fitler;

import cn.itcast.store.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PriviledgeFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request  = (HttpServletRequest)servletRequest;
		User user = (User) request.getSession().getAttribute("loginUser");
		if(null==user){
			request.getSession().setAttribute("msg","亲,你他妈还没登陆咯,请登陆在买东西");
			request.getRequestDispatcher("/jsp/info.jsp").forward(request, servletResponse);
		}else{
			filterChain.doFilter(request, servletResponse);
		}

	}

	@Override
	public void destroy() {

	}
}
