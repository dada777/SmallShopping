package cn.itcast.store.dao.daoImp;

import cn.itcast.store.dao.UserDao;
import cn.itcast.store.domain.User;
import cn.itcast.store.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

public class UserDaoImp implements UserDao {
	@Override
	public void regist(User user) throws SQLException {
		String sql = "INSERT INTO user VALUES (?,?,?,?,?,?,?,?,?,?)";
		 QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		Object[] params = {user.getUid(),user.getUsername()
				,user.getPassword(),user.getName(),user.getEmail(),user.getTelephone(),user.getBirthday()
				,user.getSex(),user.getState(),user.getCode()};
		 qr.update(sql,params);
	}

	@Override
	public User checkActive(String checkActive) throws SQLException {
		String sql="select * from user where code =?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		User user=qr.query(sql, new BeanHandler<User>(User.class),checkActive);
		return user;
	}

	public void updateUser(User user) throws SQLException {
		String sql="UPDATE USER SET username= ? ,PASSWORD=? ,NAME =? ,email =? ,telephone =? , birthday =?  ,sex =? ,state= ? , CODE = ? WHERE uid=?";
		Object[] params={user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode(),user.getUid()};
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		qr.update(sql,params);
	}

	@Override
	public User login(User user) throws SQLException {
		String sql="select * from user where username =? AND password=?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql, new BeanHandler<User>(User.class),user.getUsername(),user.getPassword());


	}

}
