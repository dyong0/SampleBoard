package com.zoel.vo;

import java.util.Date;

public class Guestbook {
	private Long id;
	private String email;
	private String password;
	private String body;
	private Date createdDate;
	private Date modifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Guestbook))
			return false;
		
		Guestbook other = (Guestbook) o;
		
		if(other.email == null || this.email == null
			|| other.password == null || this.password == null
			|| other.body == null || this.body == null
			|| other.createdDate == null || this.createdDate == null
			|| other.modifiedDate == null || this.modifiedDate == null){
			
			return false;
		}
		
		return other.id.equals(this.id) 
				&& other.email.equals(this.email)
				&& other.password.equals(this.password)
				&& other.body.equals(this.body)
				&& other.createdDate.equals(this.createdDate)
				&& other.modifiedDate.equals(this.modifiedDate);
	}
}
