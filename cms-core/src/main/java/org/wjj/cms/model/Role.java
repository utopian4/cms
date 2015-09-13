package org.wjj.cms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_role")
public class Role {
	//角色id
	private int id;
	//角色名称
	private String name;
	//编号
	private RoleType roleSn;
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() { 
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Enumerated(EnumType.STRING)
	@Column(name="role_type")
	public RoleType getRoleSn() {
		return roleSn;
	}
	public void setRoleSn(RoleType roleSn) {
		this.roleSn = roleSn;
	}
	
	
}
