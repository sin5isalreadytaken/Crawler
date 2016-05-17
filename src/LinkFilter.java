
/**
* description: 过滤器接口
* note: 
* modificationDate: 2014-11-25
*/ 
public interface LinkFilter {
	/**
	 * 过滤器接口
	 * @return 是否满足过滤条件(boolean)
	 * @param 网址(String)
	 * @throws 
	 */
	public boolean accept(String url);
}

