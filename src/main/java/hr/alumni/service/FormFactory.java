package hr.alumni.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hr.alumni.model.Comment;
import hr.alumni.model.Post;
import hr.alumni.model.PostType;
import hr.alumni.model.Role;
import hr.alumni.model.User;
import hr.alumni.model.details.CustomUserDetails;
import hr.alumni.model.form.CommentForm;
import hr.alumni.model.form.EditUserForm;
import hr.alumni.model.form.PostForm;
import hr.alumni.model.form.RegistrationForm;
import hr.alumni.repository.RoleRepository;
import hr.alumni.repository.UserRepository;
@Service
public class FormFactory {
	
	private final RoleRepository rr;
	private final PasswordEncoder pe;
	private final UserRepository ur;


	@Autowired
	public FormFactory(RoleRepository rr, PasswordEncoder pe, UserRepository ur) {
		this.rr = rr;
		this.pe = pe;
		this.ur = ur;
	}

	public User createUserFromForm(RegistrationForm rf){
		User u = new User();

		u.setFirstName(rf.getFirstName());
		u.setLastName(rf.getLastName());
		u.setPhone(rf.getPhone());
		u.setEmail(rf.getEmail());
		u.setAddress(rf.getAddress());
		u.setPassword(pe.encode(rf.getPassword()));
		u.setRoles(Arrays.asList(rr.findByName("ROLE_KORISNIK")));
		u.setBirthday(Date.valueOf(rf.getBirthday()));
		u.setGraduation(Date.valueOf(rf.getGraduation()));

		List<Role> roles = new ArrayList<>();
		Role r = rr.findByNameIgnoreCase("ROLE_KORISNIK");
		roles.add(r);
		u.setRoles(roles);
		return u;
	}

	public void editUserFromForm(User user, EditUserForm ef) {

		user.setFirstName(ef.getFirstName());
		user.setLastName(ef.getLastName());
		user.setPhone(ef.getPhone());
		user.setEmail(ef.getEmail());
		user.setAddress(ef.getAddress());
		user.setBirthday(Date.valueOf(ef.getBirthday()));
		user.setGraduation(Date.valueOf(ef.getGraduation()));

	}

	public Comment createCommentFromForm(CommentForm cForm, CustomUserDetails user) {
		Comment comment = new Comment();

		comment.setMessage(cForm.getMessage());
		comment.setUser(ur.findOne(user.getUserId()));

		return comment;
	}

	public Post createPostFromForm(PostForm postForm, CustomUserDetails userInSession) {
		Post post = new Post();

		post.setAddress(postForm.getAddress());
		post.setLongDescription(postForm.getLongDescription());
		post.setShortDescription(postForm.getShortDescription());
		post.setTitle(postForm.getTitle());
		post.setPostType(PostType.valueOf(postForm.getPostType()));

		return post;
	}

	public void editPostFromForm(PostForm postForm, Post post) {

		post.setAddress(postForm.getAddress());
		post.setLongDescription(postForm.getLongDescription());
		post.setShortDescription(postForm.getShortDescription());
		post.setTitle(postForm.getTitle());
		post.setPostType(PostType.valueOf(postForm.getPostType()));

	}

	public PostForm createFormFromPost(Post post) {
		PostForm form = new PostForm();

		form.setPostId(post.getPostId());
		form.setAddress(post.getAddress());
		form.setLongDescription(post.getLongDescription());
		form.setShortDescription(post.getShortDescription());
		form.setTitle(post.getTitle());
		form.setPostType(post.getPostType().toString());

		return form;
	}

}
