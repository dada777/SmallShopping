package cn.itcast.store.dao;

import cn.itcast.store.dao.daoImp.ProductDaoImp;
import cn.itcast.store.domain.Product;
import cn.itcast.store.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    ProductDao productDao = new ProductDaoImp();

	List<Product> findByHot() throws SQLException;

	List<Product> findByNew() throws SQLException;

	Product findByPid(String pid) throws SQLException;
	int findTotalRecords(String cid)throws Exception;
	List findProductsByCidWithPage(String cid, int startIndex, int pageSize)throws Exception;
    List findAllProductsWithPage(int startIndex, int pageSize) throws Exception;
	int findTotalRecords() throws SQLException;

	void saveProduct(Product product) throws SQLException;
}
