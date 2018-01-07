package hr.petsonly.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hr.petsonly.model.Pet;
import hr.petsonly.model.User;
import hr.petsonly.model.details.PetDetails;
import hr.petsonly.model.form.PetForm;
import hr.petsonly.repository.PetRepository;
import hr.petsonly.repository.UserRepository;
import hr.petsonly.service.FormFactory;

@Controller
@RequestMapping("/users/{id}/pets")
public class PetController {
	
	@Autowired
	private FormFactory formFactory;
	
	@Autowired
	private PetRepository petRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<PetDetails> showPetList(Model model, @PathVariable UUID id) {
		
		List<Pet> petList = petRepository.findByOwnerId(id.toString());
		System.out.println(Arrays.toString(petList.toArray()));
		
		List<PetDetails> petDetails = new ArrayList<>();
		
		petList.forEach(pet -> {
			petDetails.add(new PetDetails(pet));
		});
		
		return petDetails;
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String showNewPetForm() {
		return "addPet";
	}
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public PetDetails addNewPet(@RequestBody PetForm petForm, Model model, @PathVariable UUID id, BindingResult result) {
		
		User user = userRepository.findOne(id);
		if(result.hasErrors()) {
			System.out.println(result);
			//model.addAttribute("errorMessage", "Neispravni podaci za živinu: " + result.toString());
			return null;
		}
		Pet pet = formFactory.createPetFromForm(petForm);
		user.getPets().add(pet);
		userRepository.save(user);
		PetDetails petDetails = new PetDetails(pet);
		return petDetails;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String showUserList(Model model, @PathVariable UUID id) {
		
		petRepository.delete(id);
		
		return "redirect:/";
	}
	
	
}
