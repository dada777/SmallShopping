package cn.itcast.store.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.Category;
import cn.itcast.store.service.CategoryService;
import cn.itcast.store.service.serviceImp.CategoryServiceImp;

import cn.itcast.store.utils.JedisUtils;
import cn.itcast.store.web.base.BaseServlet;
import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;

public class CategoryServlet extends BaseServlet {
	
	public String findAllCats(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Jedis jedis = JedisUtils.getJedis();
        String jsonStr = jedis.get("allCats");

        if(null==jsonStr ||"".equals(jsonStr)){
			CategoryService categoryService = new CategoryServiceImp() ;
			List<Category>list = categoryService.findAllCats();
			jsonStr = JSONArray.fromObject(list).toString();
			System.out.println("redis中没有数据 . --准备插入一段数据	");
            jedis.set("allCats",jsonStr);

        }else{
			System.out.println("redis中有数据 ");
		}
		response.setContentType("application/jso;charset=utf-8");
		response.getWriter().print(jsonStr);


		JedisUtils.closeJedis(jedis);

		return null;

//		response.setContentType("application/json;charset=utf-8");
//		String jsonStr="";
//		Jedis j = JedisUtils.getJedis();
//		jsonStr = j.get("allCats");
//		if(null==jsonStr||"".equals(jsonStr)){
//			System.out.println("缓存中没有数据");
//			//查询所有分类
//			CategoryService CategoryService=new CategoryServiceImp();
//			List<Category> list = CategoryService.findAllCats();
//			//将集合中的所有分类信息转换为JSON格式的字符串数据
//			jsonStr=JSONArray.fromObject(list).toString();
//			j.set("allCats", jsonStr);
//
//		}else{
//			System.out.println("缓存中有数据");
//		}
//		//将字符串数据响应到客户端
//		response.getWriter().println(jsonStr);
//		JedisUtils.closeJedis(j);
//		return null;
	}

}