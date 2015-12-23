package com.xyt.ygcf.impl;

public interface IHandleHttpResult {

	/** 当success为true的情况下 */
	public void handleJson(String json, final int which);

	/** 当当success为false的情况下 */
	public void handleError(String message, final int which);

	/** 最原始的结果 */
	public void handleResult(String json, final int which);

	/** 解析json异常 */
	public void handleParseJsonException(final int which);

	/** 一般是网络错误、服务器异常 */
	public void handleNetError(String message, final int which);

}
