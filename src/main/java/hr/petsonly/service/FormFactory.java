package hr.petsonly.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.petsonly.model.Location;
import hr.petsonly.model.Pet;
import hr.petsonly.model.Reservation;
import hr.petsonly.model.Role;
import hr.petsonly.model.User;
import hr.petsonly.model.form.AddReservationForm;
import hr.petsonly.model.form.EditUserForm;
import hr.petsonly.model.form.PetForm;
import hr.petsonly.model.form.RegistrationForm;
import hr.petsonly.repository.LocationRepository;
import hr.petsonly.repository.PetRepository;
import hr.petsonly.repository.RoleRepository;
import hr.petsonly.repository.ServiceRepository;
import hr.petsonly.repository.UserRepository;

@Service
public class FormFactory {
	
	@Autowired
	private LocationRepository lr;
	@Autowired
	private UserRepository ur;
	@Autowired
	private ServiceRepository sr;
	@Autowired
	private PetRepository pr;
	@Autowired 
	private RoleRepository rr;
	
	public User createUserFromForm(RegistrationForm rf){
		User u = new User();
		u.setName(rf.getName());
		u.setSurname(rf.getSurname());
		u.setUserPid(rf.getUserPid());
		u.setMobilePhone(rf.getMobilePhone());
		u.setPhone(rf.getPhone());
		u.setEmail(rf.getEmail());
		u.setAddress(rf.getAddress());
		u.setPassword(rf.getPassword());
		
		Role role = new Role();
		role.setName("admin");
		u.setRoles(Arrays.asList(role));
		
		Location l = lr.findOne(rf.getLocation());
		u.setLocation(l);
		
		String pattern = u.getName() + u.getSurname();
		Long num = ur.countByUserMnemonic(pattern+"[0-9]*");
		u.setUserMnemonic(pattern + num);
		
		List<Role> roles = new ArrayList<>();
		Role r = rr.findByNameIgnoreCase("ROLE_KORISNIK");
		roles.add(r);
		u.setRoles(roles);
		return u;
	}

	public Reservation createReservationFromForm(AddReservationForm arf){
		Reservation r = new Reservation();
		r.setReservationKey(UUID.randomUUID());
		r.setReservationStatus(1); //KAKO SU NUMERIRANI STATUSI?
		r.setReservationTime(LocalDateTime.now());
		r.setExecutionTime(LocalDateTime.parse(arf.getExecutionTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
		r.setDuration(arf.getDuration());
		r.setSendReminder(arf.getSendReminder().toLowerCase().equals("yes") || arf.getSendReminder().toLowerCase().equals("y"));
		
		r.setService(sr.findOne(UUID.fromString(arf.getService())));
		r.setPet(pr.findOne(arf.getPet()));
		r.setUser(ur.findOne(arf.getUser()));
		if(arf.getEmployee() != null){
			r.setEmployee(ur.findOne(arf.getEmployee()));	
		}
		return r;
	}
	
	public Pet createPetFromForm(PetForm pf){
		Pet p = new Pet();
		p.setPetKey(UUID.randomUUID());
		p.setName(pf.getName());
		p.setAge(pf.getAge());
		p.setBreed(pf.getBreed());
		p.setMicrochip(pf.getMicrochip());
		p.setRemark(pf.getRemark());
		p.setSex(pf.getSex());
		p.setSpecies(pf.getSpecies());
		p.setOwner(ur.findOne(UUID.fromString(pf.getOwner())));
		return p;
	}
	
	public boolean editUserFromForm(User user, EditUserForm ef) {
		if(ef.hasChanges(user)) {
			return false;
		}
		
		user.setMobilePhone(ef.getMobilePhone());
		user.setLocation(lr.findOne(ef.getLocation()));
		return true;
	}
}
