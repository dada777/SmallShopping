package cn.itcast.store.service.serviceImp;

import cn.itcast.store.dao.ProductDao;
import cn.itcast.store.dao.daoImp.ProductDaoImp;
import cn.itcast.store.domain.Product;
import cn.itcast.store.service.ProductService;
import cn.itcast.store.utils.PageModel;

import java.sql.SQLException;
import java.util.List;

public class ProductServiceImp implements ProductService {
	private ProductDao productDao = new ProductDaoImp();
	@Override
	public List<Product> findByHot() throws SQLException {
	List<Product>hotList=productDao.findByHot();
		return hotList;
	}

	@Override
	public List<Product> findByNew() throws SQLException {
	List<Product>newList=productDao.findByNew();
		return newList;
	}

	@Override
	public Product findByPid(String pid) throws SQLException {
		return 	productDao.findByPid(pid);
	}

	@Override
	public PageModel findProductsByCidWithPage(String cid, int curNum) throws Exception {
		//1_创建PageModel对象 目的:计算分页参数 //这里默认尺寸大小为5
		//找到所有的数目
		int totalRecords = productDao.findTotalRecords(cid);
         PageModel pageModel= new PageModel(curNum,5,totalRecords);
		//统计当前分类下商品个数  select count(*) from product where cid=?

		//2_关联集合 select * from product where cid =? limit ? ,?
          //根据PageModle里算好的东西把结果集给挪出来
		List list = productDao.findProductsByCidWithPage(cid,pageModel.getStartIndex(),pageModel.getPageSize());
		//3_关联url
		pageModel.setList(list);
		pageModel.setUrl("ProductServlet?method=findProductsByCidWithPage&cid="+cid);
		return pageModel;
	}

	@Override
	public PageModel findAllProductsWithPage(int curNum) throws Exception {

		int totalRecords = productDao.findTotalRecords();

		PageModel pm = new PageModel(curNum,5,totalRecords);

		List list =productDao.findAllProductsWithPage(pm.getStartIndex(),pm.getPageSize());
        pm.setList(list);
        pm.setUrl("AdminProductServlet?method=findAllProductsWithPage");

		return pm;
	}

	@Override
	public void saveProduct(Product product) throws SQLException {
		productDao.saveProduct(product);
	}


}
