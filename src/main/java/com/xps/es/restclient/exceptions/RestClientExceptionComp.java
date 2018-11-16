package com.xps.es.restclient.exceptions;


public enum RestClientExceptionComp implements IRestClientException{

	LOAD_CONFIG_FAIL("ESR-1000","加载REST-CLIENT配置失败", ExceptionLevel.ERROR),
	HTTP_HOST_NOT_CONFIG("ESR-1001","httpHost未配置", ExceptionLevel.ERROR),
	HTTP_HOST_CONFIG_ERROR("ESR-1002","httpHost配置错误", ExceptionLevel.ERROR),
	NOT_EMPTY("ESR-1003","{0}的值不能为空!", ExceptionLevel.FAIL);


	private String code;
	private String message;
	private IRestClientException.ExceptionLevel level;

	private RestClientExceptionComp(String message) {
		this.code = "0000";
		this.message = message;
		this.level = IRestClientException.ExceptionLevel.ERROR;
	}
	private RestClientExceptionComp(String code, String message) {
		this.code = code;
		this.message = message;
		this.level = IRestClientException.ExceptionLevel.ERROR;
	}
	private RestClientExceptionComp(String code, String message, IRestClientException.ExceptionLevel level) {
		this.code = code;
		this.message = message;
		this.level = level;
	}
	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public ExceptionLevel getLevel() {
		return this.level;
	}


	@Override
	public String toString() {
		return "level:"+this.level.toString()+"code:"+this.code+",message:"+this.message;
	}
}
