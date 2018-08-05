package cn.itcast.store.dao.daoImp;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.store.dao.OrderDao;
import cn.itcast.store.domain.Order;
import cn.itcast.store.domain.OrderItem;
import cn.itcast.store.domain.Product;
import cn.itcast.store.domain.User;
import cn.itcast.store.utils.JDBCUtils;
import cn.itcast.store.utils.MyBeanUtils;

public class OrderDaoImp implements OrderDao {

	@Override
	public List<Order> findAllOrdersWithState(String state) throws Exception {
		String sql="select * from orders where state = ?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql, new BeanListHandler<Order>(Order.class),state);
		
	}

	@Override
	public List<Order> findAllOrders() throws Exception {
		String sql="select * from orders ";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql, new BeanListHandler<Order>(Order.class));
	}

	@Override
	public void updateOrder(Order order) throws Exception {
		String sql="UPDATE orders SET ordertime =? , total =? ,state= ?, address=? ,name=? ,telephone =? WHERE oid=?";
		Object[] params={order.getOrderTime(),order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getOid()};
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		qr.update(sql,params);
		
	}

	@Override
	public int findTotalRecordsByUid(User user) throws Exception {
		String sql="select count(*) from orders where uid=?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		Long num=(Long)qr.query(sql, new ScalarHandler(),user.getUid());
		return num.intValue();
	}

	@Override
	public List<Order> findOrdersByUidWithPage(User user, int startIndex, int pageSize) throws Exception {
		String sql="SELECT * FROM orders WHERE uid =?  ORDER BY orderTime DESC LIMIT  ? , ?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		List<Order> list=qr.query(sql, new BeanListHandler<Order>(Order.class),user.getUid(),startIndex,pageSize);
		
		for(Order order:list){
			sql="SELECT * FROM  orderitem o , product p WHERE o.pid=p.pid  AND oid=?";
			List<Map<String, Object>> list01 = qr.query(sql, new MapListHandler(),order.getOid());
			for(Map<String, Object> map:list01){
				
				//System.out.println(map);
				
				Product product=new Product();
				OrderItem OrderItem=new OrderItem();
				
				try {
					//BeanUtils会自动将map上属于product对象中的数据填充到product对象上
					//BeanUtils会自动将map上属于OrderItem对象中的数据填充到OrderItem对象上
					BeanUtils.populate(product, map);
					BeanUtils.populate(OrderItem, map);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				OrderItem.setProduct(product);
				order.getList().add(OrderItem);
			}
		}
		return list;
	}
	
	
	
	/*public List<Order> findOrdersByUidWithPage02(User user, int startIndex, int pageSize) throws Exception {
		String sql="SELECT * FROM orders WHERE uid =?  ORDER BY ordertime DESC LIMIT  ? , ?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		List<Order> list=qr.query(sql, new BeanListHandler<Order>(Order.class),user.getUid(),startIndex,pageSize);
		
		for(Order order:list){
			sql="select * from orderitem where oid=?";
			List<OrderItem> orderItems=qr.query(sql, new BeanListHandler<OrderItem>(OrderItem.class),order.getOid());
			for(OrderItem orderItem:orderItems){
				sql="select * from product where pid=?";
				qr.query(sql, new BeanListHandler<Product>(Product.class),orderItem.getProduct().getPid())
			}
			
		}
		return list;
	}*/
	
	

	@Override
	public Order findOrderByOid(String oid) throws Exception {
		String sql = "SELECT * FROM order WHERE oid = ?" ;
		QueryRunner qr=new QueryRunner();
        Order order=qr.query(sql,new BeanHandler<Order>(Order.class),oid);

        sql="SELECT *FROM orderitem o ,product p WHERE o.pid=p.pid and oid=?";
      List<Map<String,Object>>list=  qr.query(sql,new MapListHandler(),oid);
        for(Map<String,Object>map:list){
        	OrderItem orderItem = new OrderItem();
        	Product product = new Product();
			// 由于BeanUtils将字符串"1992-3-3"向user对象的setBithday();方法传递参数有问题,手动向BeanUtils注册一个时间类型转换器
			// 1_创建时间类型的转换器
			DateConverter dt = new DateConverter();
			// 2_设置转换的格式
			dt.setPattern("yyyy-MM-dd");
			// 3_注册转换器
			ConvertUtils.register(dt, java.util.Date.class);

        	BeanUtils.populate(orderItem,map);
        	BeanUtils.populate(product,map);

        	orderItem.setProduct(product);
        	order.getList().add(orderItem);

		}
       return order;


	}

	@Override
	public void saveOrder(Order order) throws SQLException {
		String sql="INSERT INTO orders VALUES(?,?,?,?,?,?,?,?)";
		Object[] params={order.getOid(),order.getOrderTime(),order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid()};
		QueryRunner qr=new QueryRunner();
		qr.update(JDBCUtils.getConnection(),sql,params);
	}

	@Override
	public void saveOrderItem(OrderItem item)  throws SQLException {
		String sql="INSERT INTO orderitem VALUES(?,?,?,?,?)";
		Object[] params={item.getItemid(),item.getQuantity(),item.getTotal(),item.getProduct().getPid(),item.getOrder().getOid()};
		QueryRunner qr=new QueryRunner();
		qr.update(JDBCUtils.getConnection(),sql,params);
	}

	
	
	public static  void testMapListHandler() throws Exception{
		String sql="select * from category";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		List<Map<String, Object>> list = qr.query(sql, new MapListHandler());
		for(Map<String, Object> map:list){
			System.out.println(map);
		}
		
		
	} 
	
	
	public static void main(String[] args) throws Exception {
		//User u=new User();
		//u.setUid("72DE6CD3E5CC476893F3C68B189ABC42");
		//new OrderDaoImp().findOrdersByUidWithPage(u, 0, 1);
		testMapListHandler();
	}
	
	
	
}
