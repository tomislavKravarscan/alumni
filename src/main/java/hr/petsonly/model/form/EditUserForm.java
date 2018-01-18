package hr.petsonly.model.form;

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import hr.petsonly.model.form.validation.ValidEmail;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import hr.petsonly.model.User;
import hr.petsonly.model.form.validation.PasswordMatches;

@Component
@PasswordMatches
public class EditUserForm {

	@NotNull
	@NotEmpty(message = "{euform.firstname.empty}")
	private String name;

	@NotNull
	@NotEmpty(message = "{rform.lastname.empty}")
	private String surname;

	@NotNull
	@NotEmpty(message = "{euform.mobilephone.empty}")
	private String mobilePhone;

	@NotNull
	@NotEmpty(message = "{euform.phone.empty}")
	private String phone;

	@NotNull
	@ValidEmail(message = "{euform.email.invalid}")
	private String email;

	@NotNull
	private UUID location;

	@NotNull
	@NotEmpty(message = "{euform.address.empty}")
	private String address;

	@Size(min = 8, max = 30)
	@NotNull
	@NotEmpty(message = "{euform.password.empty}")
	private String password;

	@Size(min = 8, max = 30)
	@NotNull
	@NotEmpty(message = "{euform.password2.empty}")
	private String password2;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UUID getLocation() {
		return location;
	}

	public void setLocation(UUID location) {
		this.location = location;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public boolean hasChanges(User user) {
		return !name.equals(user.getName())
				|| !surname.equals(user.getSurname())
				|| !mobilePhone.equals(user.getMobilePhone())
				|| !phone.equals(user.getPhone())
				|| !email.equals(user.getEmail())
				|| !location.equals(user.getLocation().getLocationId())
				|| !address.equals(user.getAddress())
				|| !password.equals(user.getPassword());
	}
}
