package cn.itcast.store.service.serviceImp;

import java.util.List;

import cn.itcast.store.dao.OrderDao;
import cn.itcast.store.dao.daoImp.OrderDaoImp;
import cn.itcast.store.domain.Order;
import cn.itcast.store.domain.OrderItem;
import cn.itcast.store.domain.User;
import cn.itcast.store.service.OrderService;
import cn.itcast.store.utils.JDBCUtils;
import cn.itcast.store.utils.PageModel;

public class OrderServiceImp implements OrderService {

	OrderDao OrderDao=new OrderDaoImp();
	@Override
	public List<Order> findAllOrders() throws Exception {
		return OrderDao.findAllOrders();
	}

	@Override
	public List<Order> findAllOrdersWithState(String state) throws Exception {
		return OrderDao.findAllOrdersWithState(state);
	}

	@Override
	public void updateOrder(Order order) throws Exception {
		OrderDao.updateOrder(order);
	}

	
	
	@Override
	public void saveOrder(Order order) {
		try {
			JDBCUtils.startTransaction();
			OrderDao.saveOrder(order);
			for(OrderItem item:order.getList()){
				OrderDao.saveOrderItem(item);
			}
			JDBCUtils.commitAndClose();
		} catch (Exception e) {
			JDBCUtils.rollbackAndClose();
			e.printStackTrace();
		}
			
	}

	@Override
	public PageModel findOrdersByUidWithPage(User user, int curNum) throws Exception {
		// new PageModel //select count(*) from orders where uid=?
		int totalRecords=OrderDao.findTotalRecordsByUid(user);
		PageModel pm=new PageModel(curNum,3 , totalRecords);
		// 关联集合SELECT * FROM orders WHERE uid =?  ORDER BY ordertime DESC LIMIT  ? , 3
		List<Order> list=OrderDao.findOrdersByUidWithPage(user,pm.getStartIndex(),pm.getPageSize());
		pm.setList(list);
		// 关联url
		pm.setUrl("OrderServlet?method=findOrdersByUidWithPage");
		return pm;
	}

	@Override
	public Order findOrderByOid(String oid) throws Exception {
		return OrderDao.findOrderByOid(oid);
	}

}
