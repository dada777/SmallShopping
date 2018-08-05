package cn.itcast.store.dao;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.store.domain.Category;

public interface CategoryDao {

	List<Category> findAllCats()throws SQLException;

	void saveCat(Category c)throws SQLException;

}
