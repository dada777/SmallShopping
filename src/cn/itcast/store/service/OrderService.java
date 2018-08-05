package cn.itcast.store.service;

import java.util.List;

import cn.itcast.store.domain.Order;
import cn.itcast.store.domain.User;
import cn.itcast.store.utils.PageModel;

public interface OrderService {

	void saveOrder(Order order);

	PageModel findOrdersByUidWithPage(User user, int curNum)throws Exception;

	Order findOrderByOid(String oid)throws Exception;

	void updateOrder(Order order)throws Exception;
	List<Order> findAllOrders()throws Exception;
	List<Order> findAllOrdersWithState(String state)throws Exception;;
}
