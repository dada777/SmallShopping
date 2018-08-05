package cn.itcast.store.service;

import cn.itcast.store.domain.Product;
import cn.itcast.store.utils.PageModel;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {
	List<Product> findByHot() throws SQLException;

	List<Product> findByNew() throws SQLException;

	Product findByPid(String pid) throws SQLException;

	PageModel findProductsByCidWithPage(String cid, int curNum) throws Exception;

	PageModel findAllProductsWithPage (int curNum) throws Exception;

	void saveProduct(Product product) throws SQLException;
}
