package cn.itcast.store.dao.daoImp;

import cn.itcast.store.dao.ProductDao;
import cn.itcast.store.domain.Product;
import cn.itcast.store.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

public class ProductDaoImp implements ProductDao {
	public List<Product> findByHot() throws SQLException {
		String sql = "SELECT * FROM product WHERE is_hot=? and pflag = ? order by pdate desc limit ?";
		QueryRunner qr= new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql,new BeanListHandler<Product>(Product.class),1,0,9);
	}

	public List<Product> findByNew() throws SQLException {
		String sql = "SELECT * FROM product WHERE  pflag=? order by pdate desc limit ?";
		QueryRunner qr= new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql,new BeanListHandler<Product>(Product.class),0,9);
	}

	@Override
	public Product findByPid(String pid) throws SQLException {
		String sql = "SELECT * FROM product WHERE pid = ?";
		QueryRunner qr= new QueryRunner(JDBCUtils.getDataSource());

		return qr.query(sql,new BeanHandler<Product>(Product.class),pid);
	}


	@Override
	public int findTotalRecords(String cid) throws Exception {
		String sql="select count(*) from product where cid =?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		Long num=(Long)qr.query(sql, new ScalarHandler(),cid);
		return num.intValue();
	}

	@Override
	public List findProductsByCidWithPage(String cid, int startIndex, int pageSize) throws Exception {
		String sql = "SELECT * FROM product WHERE cid  = ? limit ?,?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql,new BeanListHandler<Product>(Product.class),cid,startIndex,pageSize);





//		String sql="select * from product where cid=? limit ? , ?";
//		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
//		return qr.query(sql, new BeanListHandler<Product>(Product.class),cid,startIndex,pageSize);
	}

	@Override
	public List findAllProductsWithPage(int startIndex, int pageSize) throws Exception {
		String sql = "SELECT * FROM product limit ?,?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql,new BeanListHandler<Product>(Product.class),startIndex,pageSize);
	}

	@Override
	public int findTotalRecords() throws SQLException {
		String sql  ="SELECT count(*) FROM product";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		Long count = (Long) qr.query(sql, new ScalarHandler());
		return count.intValue();
	}

	@Override
	public void saveProduct(Product product) throws SQLException {
		String sql = "INSERT INTO product VALUES (?,?,?,?,?,?,?,?,?,?)";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		Object[]params={product.getPid(),product.getPname(),product.getMarket_price(),product.getShop_price(),product.getPimage(),product.getPdate(),product.getIs_hot(),product.getPdesc(),product.getPflag(),product.getCid()};
           qr.update(sql,params);
	}

	public int findTotalRecordByCid(String cid) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql ="SELECT count(*) FROM product WHERE cid=?";
		Long count = (Long) queryRunner.query(sql, new ScalarHandler(), cid);
		return count.intValue();
	}

}
