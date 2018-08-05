package cn.itcast.store.text;

import cn.itcast.store.domain.User;
import cn.itcast.store.utils.MyBeanUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class test {

	@Test
	public void testUser(){
		User user = new User();
		Map<String,String[]> map=new HashMap<>();
		map.put("username",new String[]{"wangsuli"});
		map.put("password",new String[]{"woainilo"});

	}

}
