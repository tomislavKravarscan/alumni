package hr.petsonly.model.form;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

@Component
public class AddReservationForm {
	
	@NotNull
	private String service;
	
	@NotNull
	private UUID pet;
	
	@NotNull
	private UUID employee;
	
	@NotNull
	private String executionTime;
	
	@NotNull
	private String executionDate;
	
	@NotNull
	private String duration;
		
	@NotNull
	private String sendReminder;

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public UUID getPet() {
		return pet;
	}

	public void setPet(UUID pet) {
		this.pet = pet;
	}

	public UUID getEmployee() {
		return employee;
	}

	public void setEmployee(UUID employee) {
		this.employee = employee;
	}

	public String getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getSendReminder() {
		return sendReminder;
	}

	public void setSendReminder(String sendReminder) {
		this.sendReminder = sendReminder;
	}
	
	public String getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(String executionDate) {
		this.executionDate = executionDate;
	}

	@Override
	public String toString() {
		return "AddReservationForm [service=" + service + ", pet=" + pet + ", employee=" + employee
				+ ", executionTime=" + executionTime + ", executionDate=" + executionDate + ", duration=" + duration
				+ ", sendReminder=" + sendReminder + "]";
	}
}
