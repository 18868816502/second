package com.beihui.market.util;


import java.util.TreeMap;

/**
 * 
* @Description: webapi客户端请求参数
* @author <a href="mailto:chenghushou@163.com">ChengYiShou</a>
* @date 2017年1月9日 下午10:14:14 
* @version %I%, %G%
 */
public class FwsHttpParames extends TreeMap<String, Object> {
	private static final long serialVersionUID = 6493805389124566577L;

	/**
	 * 
	* @Description: 添加参数 
	* @author <a href="mailto:chenghushou@163.com">ChengYiShou</a>
	* @date 2017年1月9日 下午10:14:28 
	* @return BpsHttpParames
	* @throws
	 */
	public FwsHttpParames add(String key, Object value) {
		if(value != null){
			this.put(key, value);
		}
		return this;
	}
}
