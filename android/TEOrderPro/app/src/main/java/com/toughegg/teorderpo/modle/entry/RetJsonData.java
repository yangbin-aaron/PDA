package com.toughegg.teorderpo.modle.entry;

public class RetJsonData {
	private String message;
	private boolean result;
	private String jsonData;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	@Override
	public String toString() {
		return "RetJsonData [message=" + message + ", result=" + result + ", jsonData=" + jsonData + "]";
	}

}
