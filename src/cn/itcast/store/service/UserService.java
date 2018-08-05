package cn.itcast.store.service;

import cn.itcast.store.domain.User;

import java.sql.SQLException;

public interface UserService {
	void regist(User user) throws SQLException;

	User login(User user) throws SQLException;

	User checkActive(String activeCode) throws SQLException;


	void updateUser(User user) throws SQLException;
}
