package edu.colorado.fis;

public class DataAccessFault extends Exception {
	private String code;
	private String message;
	private String detail;
	
	public static String SERVICE_FAILURE_CODE = "01";
	public static String SERVICE_FAILURE_MSG = "Service Failure";
	public static String SERVICE_FAILURE_DETAIL_DEFAULT = "The Faculty Information System Data Access is not available at this time.";
	public static String SERVICE_FAILURE_DETAIL_UNABLE_TO_CONNECT = "Unable to access the Faculty Information System data store.";
	public static String INVALID_PARAMETER_CODE = "02";
	public static String INVALID_PARAMETER_MSG = "Invalid Parameter";
	public static String INVALID_PARAMETER_DETAIL = "The parameters provided are not valid.";
    public static String SAVE_FAILURE_CODE = "03";
    public static String SAVE_FAILURE_MSG = "Unable to perform save action";

    DataAccessFault(Exception e){
        super(e);
    }

    DataAccessFault() {
        super();
    }

    DataAccessFault(String message) {
        super(message);
    }

    /**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}
	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

}
