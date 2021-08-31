package com.org.security.model;


public class Permission {
	
	private int resource_id;
	private int role_id;

	private boolean can_add;
	private boolean can_edit;
	private boolean can_view;
	private boolean can_delete;
	public int getResource_id() {
		return resource_id;
	}
	public void setResource_id(int resource_id) {
		this.resource_id = resource_id;
	}
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public boolean isCan_add() {
		return can_add;
	}
	public void setCan_add(boolean can_add) {
		this.can_add = can_add;
	}
	public boolean isCan_edit() {
		return can_edit;
	}
	public void setCan_edit(boolean can_edit) {
		this.can_edit = can_edit;
	}
	public boolean isCan_view() {
		return can_view;
	}
	public void setCan_view(boolean can_view) {
		this.can_view = can_view;
	}
	public boolean isCan_delete() {
		return can_delete;
	}
	public void setCan_delete(boolean can_delete) {
		this.can_delete = can_delete;
	}
	public Permission(int resource_id, int role_id, boolean can_add, boolean can_edit, boolean can_view,
			boolean can_delete) {
		super();
		this.resource_id = resource_id;
		this.role_id = role_id;
		this.can_add = can_add;
		this.can_edit = can_edit;
		this.can_view = can_view;
		this.can_delete = can_delete;
	}
	public Permission() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
