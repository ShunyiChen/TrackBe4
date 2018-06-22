package com.maxtree.automotive.dashboard.domain;

public class RoleMember {

	public int getRoleMemberUniqueId() {
		return roleMemberUniqueId;
	}

	public void setRoleMemberUniqueId(int roleMemberUniqueId) {
		this.roleMemberUniqueId = roleMemberUniqueId;
	}

	public int getUserUniqueId() {
		return userUniqueId;
	}

	public void setUserUniqueId(int userUniqueId) {
		this.userUniqueId = userUniqueId;
	}

	public int getRoleUniqueId() {
		return roleUniqueId;
	}

	public void setRoleUniqueId(int roleUniqueId) {
		this.roleUniqueId = roleUniqueId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
    public String toString() {
        return String.format(
                "Usr[roleMemberUniqueId=%d, userUniqueId='%d',roleUniqueId='%d']",
                roleMemberUniqueId, userUniqueId, roleUniqueId);
    }
	
	private int roleMemberUniqueId; // Role member uniqueId
	private int userUniqueId; // User unique Id
	private int roleUniqueId; // Role unique Id
	private User user; // User who has this role member
	private Role role; // User role
}
