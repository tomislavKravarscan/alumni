package hr.petsonly.web;

import hr.petsonly.model.Reservation;
import hr.petsonly.model.ReservationStatus;
import hr.petsonly.model.User;
import hr.petsonly.model.details.CustomUserDetails;
import hr.petsonly.model.details.LocationDetails;
import hr.petsonly.model.details.ReservationDetails;
import hr.petsonly.model.details.UserDetailsMore;
import hr.petsonly.model.form.EditReservationForm;
import hr.petsonly.model.form.EditUserForm;
import hr.petsonly.repository.UserRepository;
import hr.petsonly.service.FormFactory;
import hr.petsonly.service.ReservationService;
import hr.petsonly.service.email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/users/{id}/jobs")
public class JobController {
	
	private final ReservationService reservationService;

	private final EmailServiceImpl mailService;
	
	private final UserRepository userRepository;
	private FormFactory formFactory;

	@Autowired
	public JobController(EmailServiceImpl mailService, ReservationService reservationService, UserRepository userRepository) {
		this.mailService = mailService;
		this.reservationService = reservationService;
		this.userRepository = userRepository;
	}

	@GetMapping
	@PreAuthorize("hasRole('ZAPOSLENIK') or hasRole('ADMINISTRATOR')")
	public String showAllReservations(Model model, @PathVariable UUID id, @ModelAttribute("errorMessage") String errorMessage) {
		
		User user = userRepository.findOne(id);
		
		List<ReservationDetails> open = reservationService.findAllPendingReservations(user);
		List<ReservationDetails> accepted = reservationService.findAllAcceptedReservations(user);
		List<ReservationDetails> confirmed = reservationService.findAllConfirmedReservations(user);

		
		model.addAttribute("open", open);
		model.addAttribute("accepted", accepted);
		model.addAttribute("confirmed", confirmed);
		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("userId", id);
		
		return "jobs";
	}
	
	@GetMapping("/{reservationId}")
	@PreAuthorize("@webSecurityConfig.checkUserId(authentication, #id) or hasRole('ZAPOSLENIK')")
	public String showReservationDetalils(Model model, @PathVariable UUID reservationId) {
		Reservation reservation = reservationService.findOne(reservationId);
		ReservationDetails reservationDetails = new ReservationDetails(reservation);
		model.addAttribute("reservation", reservationDetails);
		
		return "reservation";
		
	}
	
	@PostMapping("/{reservationId}/accept")
	@PreAuthorize("hasRole('ZAPOSLENIK') or hasRole('ADMINISTRATOR')")
	public String acceptReservation(@PathVariable UUID reservationId, @PathVariable UUID id, RedirectAttributes ra) {
		User employee = userRepository.findOne(id);
		
		if(reservationService.isDeleted(reservationId)){
			ra.addFlashAttribute("errorMessage", "Rezervacija je u međuvremenu pobrisana!");
		}else{
			Reservation reservation = reservationService.findOne(reservationId);
			if(!reservation.getReservationStatus().equals(ReservationStatus.PENDING)){
				ra.addFlashAttribute("errorMessage", "Rezervacija je već prihvaćena!");
			}else{
				reservation.setReservationStatus(ReservationStatus.ACCEPTED); //accepted
				reservation.setEmployee(employee);
				reservationService.save(reservation);
				mailService.sendReservationOffer(reservation);
			}
		}
		
		return "redirect:/users/{id}/jobs";
	}
	
	@PostMapping("/{reservationId}/confirm")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public String confirmReservation(@PathVariable UUID reservationId,  RedirectAttributes ra) {
		
		if(reservationService.isDeleted(reservationId)){
			ra.addFlashAttribute("errorMessage", "Rezervacija je u međuvremenu pobrisana!");
		}else{
			Reservation reservation = reservationService.findOne(reservationId);
			if(!reservation.getReservationStatus().equals(ReservationStatus.ACCEPTED)){
				ra.addFlashAttribute("errorMessage", "Rezervacija je već potvrđena!");
			}else{
				reservation.setReservationStatus(ReservationStatus.CONFIRMED);
				reservationService.save(reservation);
			}
		}
		
	
		return "redirect:/users/{id}/jobs";
	}
	
	@PostMapping("/{reservationId}/archive")
	@PreAuthorize("hasRole('ADMINISTRATOR') or @webSecurityConfig.checkUserId(authentication, #id)")
	public String archiveReservation(@PathVariable UUID reservationId,  RedirectAttributes ra) {
		
		if(reservationService.isDeleted(reservationId)){
			ra.addFlashAttribute("errorMessage", "Rezervacija je u međuvremenu pobrisana!");
		}else{
			Reservation reservation = reservationService.findOne(reservationId);
			if(!reservation.getReservationStatus().equals(ReservationStatus.CONFIRMED)){
				ra.addFlashAttribute("errorMessage", "Rezervacija je već arhivirana!");
			}else{
				reservation.setReservationStatus(ReservationStatus.ARCHIVED);
				reservationService.save(reservation);
			}
		}
		return "redirect:/users/{id}/jobs";
	}
	
	
	
}
