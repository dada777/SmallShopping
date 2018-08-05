package cn.itcast.store.service.serviceImp;

import cn.itcast.store.dao.UserDao;
import cn.itcast.store.dao.daoImp.UserDaoImp;
import cn.itcast.store.domain.User;
import cn.itcast.store.service.UserService;

import java.sql.SQLException;

public class UserServiceImp implements UserService {
	UserDao userDao=new UserDaoImp();
	@Override
	public void regist(User user) throws SQLException {
		userDao.regist(user);
	//	System.out.println("开始注册就酱");
		//发送录取邮件哦-
	}

	@Override
	public User login(User user) throws SQLException {
		return userDao.login(user);
	}

	@Override
	public User checkActive(String checkActive) throws SQLException {
		User user=userDao.checkActive(checkActive);
		return user;
	}

	public void updateUser(User user) throws SQLException{
		userDao.updateUser(user);
	}
}
