package cn.itcast.store.dao;

import cn.itcast.store.domain.User;

import java.sql.SQLException;

public interface UserDao {
	void regist(User user) throws SQLException;

	User checkActive(String checkActive) throws SQLException;

	void updateUser(User user) throws SQLException;

	User login(User user) throws SQLException;
}
