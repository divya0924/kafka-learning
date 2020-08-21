package pojo;

import java.io.Serializable;

public class CustomObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3323692186258831904L;
	/**
	 * 
	 */
	
	private String id;
	private String name;
	private String gender;

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
