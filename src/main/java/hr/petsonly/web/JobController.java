package hr.petsonly.web;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petsonly.model.Reservation;
import hr.petsonly.model.details.ReservationDetails;
import hr.petsonly.repository.ReservationRepository;

@Controller
@RequestMapping(value = "/jobs")
public class JobController {
	
	@Autowired
	private ReservationRepository reservationRepository;

	@GetMapping
	public String showAllReservations(Model model) {
		
		List<Reservation> allReservations = reservationRepository.findAll();
		List<ReservationDetails> open = new ArrayList<>();
		List<ReservationDetails> accepted = new ArrayList<>();
		List<ReservationDetails> confirmed = new ArrayList<>();

		for (Reservation res : allReservations) {
			switch (res.getReservationStatus()) {
			case 1:
				open.add(new ReservationDetails(res));
				break;
			case 2:
				accepted.add(new ReservationDetails(res));
				break;
			case 3:
				confirmed.add(new ReservationDetails(res));
				break;
			}
		}
			
		model.addAttribute("open", open);
		model.addAttribute("accepted", accepted);
		model.addAttribute("confirmed", confirmed);
		
		
		return "jobs";
	}
	
	@GetMapping("/{reservationId}")
	public String showReservationDetalils(Model model, @PathVariable UUID reservationId) {
		Reservation reservation = reservationRepository.findOne(reservationId);
		ReservationDetails reservationDetails = new ReservationDetails(reservation);
		model.addAttribute("reservation", reservationDetails);
		
		return "reservation";
	}
	
	@PostMapping("/{reservationId}/accept")
	public String acceptReservation(@PathVariable UUID reservationId) {
		
		Reservation reservation = reservationRepository.findOne(reservationId);
		reservation.setReservationStatus(2); //accepted
		reservationRepository.save(reservation);
		
		return "redirect:/jobs";
	}
	
	@PostMapping("/{reservationId}/confirm")
	public String confirmReservation(@PathVariable UUID reservationId) {
		
		Reservation reservation = reservationRepository.findOne(reservationId);
		reservation.setReservationStatus(3); //confirmed TODO: ovo treba pomocu enuma, ne samo broj
		reservationRepository.save(reservation);
		
		return "redirect:/jobs";
	}
}
