package cn.itcast.store.dao;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.store.domain.Order;
import cn.itcast.store.domain.OrderItem;
import cn.itcast.store.domain.User;

public interface OrderDao {

	void saveOrder(Order order)throws SQLException ;

	void saveOrderItem(OrderItem item)throws SQLException ;

	int findTotalRecordsByUid(User user) throws Exception ;

	List<Order> findOrdersByUidWithPage(User user, int startIndex, int pageSize) throws Exception ;

	Order findOrderByOid(String oid)throws Exception ;

	void updateOrder(Order order)throws Exception ;
	List<Order> findAllOrdersWithState(String state)throws Exception ;
	List<Order> findAllOrders()throws Exception ;

}
