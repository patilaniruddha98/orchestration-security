package com.org.security.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.org.security.model.Resource;
import com.org.security.model.ResourcePerm;
import com.org.security.model.Role;
import com.org.security.model.RolePermission;
import com.org.security.model.User;
import com.org.security.repository.AccessRepository;
import com.org.security.repository.PermissionRepository;
import com.org.security.repository.RoleRepository;
import com.org.security.repository.UserRepository;
import com.org.security.request.PasswordResetRequest;
import com.org.security.request.SetUpPasswordRequest;

@Service
public class AccessServiceImpl implements AccessService {

	@Autowired
	AccessRepository accessRepository;

	@Autowired
	PermissionRepository permissionRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	
	@Autowired
	PasswordEncoder encoder;


	public Resource addResource(Resource resource) {

		return accessRepository.save(resource) ;
	}

	public RolePermission grantPermissions(RolePermission rolePermission) {

		return permissionRepository.save(rolePermission);

	}


	public Role addRole(Role role) {

		return roleRepository.save(role) ;
	}

	@Override
	public List<RolePermission> getPermissionsByRoleId(int roleId) {

		return permissionRepository.findByroleId( roleId);
	}


	@Override
	public Role getRoleByRoleID(int roleID) {

		return roleRepository.findByroleID(roleID);
	}


	@Override
	public Role getByRoleName(String roleName) {

		return roleRepository.findByroleName(roleName);
	}

	@Override
	public String updatepermissionsByRoleIDAndResourceId(ResourcePerm resourcePerm) {

		Role role=getByRoleName(resourcePerm.getRoleName());

		System.out.println(resourcePerm.getPermissionList());

		for(RolePermission p:resourcePerm.getPermissionList()) {

			p.setRoleId(role.getRoleID());
		}

		System.out.println(resourcePerm);

		for(RolePermission p:resourcePerm.getPermissionList()) {

			RolePermission rp=permissionRepository.findByRoleIdAndResourceId(p.getRoleId(),p.getResourceId());

			rp.setCanView(p.isCanView());
			rp.setCanEdit(p.isCanEdit());
			rp.setCanAdd(p.isCanAdd());
			rp.setCanDelete(p.isCanDelete());
			System.out.println(rp);
			permissionRepository.save(rp);

		}
		return "success";
	}
	
	@Override
	public User getByEmailId(String emailId) {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmailId(emailId);
		return user;
	}


	@Override
	public String resetPasswordById(String id,PasswordResetRequest passwordResetRequest) throws Exception {
		Optional<User> mydata=userRepository.findById(id);
		if(mydata.isPresent()) {

			if(encoder.matches(passwordResetRequest.getOldPassword(), mydata.get().getPassword())) {
				User user = new User(id,mydata.get().getName(),mydata.get().getEmailId(),mydata.get().getMobile(),mydata.get().getGender(),mydata.get().getDepartment(),mydata.get().getCity(),mydata.get().getHiredDate(),encoder.encode(passwordResetRequest.getNewPassword()));
				userRepository.save(user);
				return "password set succesfully";
			}
			else {
				throw new Exception("previous password not matched");
			}
			
			
		}else {
			throw new Exception("User not found");
		}
	}

	@Override
	public boolean existUserByEmailId(String emailId) {
		
		return userRepository.existsByEmailId(emailId);
	}

	@Override
	public boolean existUserByMobileNumber(String mobile) {
		// TODO Auto-generated method stub
		return userRepository.existsByMobile(mobile);
	}

	@Override
	public User findByMobile(String mobile) {
		// TODO Auto-generated method stub
		return userRepository.findByMobile(mobile);
	}

	@Override
	public String setUpPassword(String id, SetUpPasswordRequest setupPassword) throws Exception {
		
		Optional<User> mydata=userRepository.findById(id);
		if(mydata.isPresent()) {

			if(encoder.matches(setupPassword.getOtp(), mydata.get().getPassword())) {
				User user = new User(id,mydata.get().getName(),mydata.get().getEmailId(),mydata.get().getMobile(),mydata.get().getGender(),mydata.get().getDepartment(),mydata.get().getCity(),mydata.get().getHiredDate(),encoder.encode(setupPassword.getNewPassword()));
				userRepository.save(user);
				return "password set succesfully";
			}
			else {
				throw new Exception("Invalid OPT");
			}
			
			
		}else {
			throw new Exception("User not found");
		}
	}



}
