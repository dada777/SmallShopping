package cn.itcast.store.web.servlet;

import cn.itcast.store.domain.Category;
import cn.itcast.store.domain.Product;
import cn.itcast.store.service.CategoryService;
import cn.itcast.store.service.ProductService;
import cn.itcast.store.service.serviceImp.CategoryServiceImp;
import cn.itcast.store.service.serviceImp.ProductServiceImp;
import cn.itcast.store.utils.PageModel;
import cn.itcast.store.utils.UUIDUtils;
import cn.itcast.store.utils.UploadUtils;
import cn.itcast.store.web.base.BaseServlet;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminProductServlet extends BaseServlet {
	public String findAllProductsWithPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int curNum = Integer.valueOf(request.getParameter("num"));
//		调用业务层查全部商品信息返回PageModel
		ProductService productService = new ProductServiceImp();
		PageModel pm =productService.findAllProductsWithPage(curNum);
//				将PageModel放入request
		request.setAttribute("page",pm);
//		转发到
		return "/admin/product/list.jsp";
//		3_ProductService
//				创建PageModel对象
//		关联集合
//				关联URL
//		4_/admin/product/list.jsp获取商品信息和分页数据

	}
	public String saveUI(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		CategoryService categoryService= new CategoryServiceImp();
		List<Category> list = categoryService.findAllCats();
		//根据内容进行添加东西
		request.setAttribute("list",list);
		return "/admin/product/add.jsp";
	}

	public String addProduct(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//存储表单中数据
		Map<String,String> map=new HashMap<String,String>();
		//携带表单中的数据向servcie,dao
		Product product=new Product();
		try {
			//利用req.getInputStream();获取到请求体中全部数据,进行拆分和封装
			DiskFileItemFactory fac=new DiskFileItemFactory();
			ServletFileUpload upload=new ServletFileUpload(fac);
			List<FileItem> list=upload.parseRequest(req);
			//4_遍历集合
			for (FileItem item : list) {
				if(item.isFormField()){
					//5_如果当前的FileItem对象是普通项
					//将普通项上name属性的值作为键,将获取到的内容作为值,放入MAP中
					// {username<==>tom,password<==>1234}
					map.put(item.getFieldName(), item.getString("utf-8"));
				}else{
					//6_如果当前的FileItem对象是上传项

					//获取到原始的文件名称
					String oldFileName=item.getName();

					//获取到要保存文件的名称   1222.doc  123421342143214.doc
					String newFileName=UploadUtils.getUUIDName(oldFileName);

					//通过FileItem获取到输入流对象,通过输入流可以获取到图片二进制数据
					InputStream is=item.getInputStream();
					//获取到当前项目下products/3下的真实路径
					//D:\tomcat\tomcat71_sz07\webapps\store_v5\products\3
					String realPath=getServletContext().getRealPath("/products/3/");
					System.out.println(realPath);
					String dir=UploadUtils.getDir(newFileName); // /f/e/d/c/4/9/8/4
					String path=realPath+dir; //D:\tomcat\tomcat71_sz07\webapps\store_v5\products\3/f/e/d/c/4/9/8/4
					//内存中声明一个目录
					File newDir=new File(path);
					if(!newDir.exists()){
						newDir.mkdirs();
					}
					//在服务端创建一个空文件(后缀必须和上传到服务端的文件名后缀一致)
					File finalFile=new File(newDir,newFileName);
					if(!finalFile.exists()){
						finalFile.createNewFile();
					}
					//建立和空文件对应的输出流
					OutputStream os=new FileOutputStream(finalFile);
					//将输入流中的数据刷到输出流中
					IOUtils.copy(is, os);
					//释放资源
					IOUtils.closeQuietly(is);
					IOUtils.closeQuietly(os);
					//向map中存入一个键值对的数据 userhead<===> /image/11.bmp
					// {username<==>tom,password<==>1234,userhead<===>image/11.bmp}
					map.put("pimage", "/products/3/"+dir+"/"+newFileName);
				}
			}


			//7_利用BeanUtils将MAP中的数据填充到Product对象上
			BeanUtils.populate(product, map);
			product.setPid(UUIDUtils.getId());
			product.setPdate(new Date());
			product.setPflag(0);

			//8_调用servcie_dao将user上携带的数据存入数据仓库,重定向到查询全部商品信息路径
			ProductService ProductService=new ProductServiceImp();
			ProductService.saveProduct(product);

			resp.sendRedirect("/AdminProductServlet?method=findAllProductsWithPage&num=1");


		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
