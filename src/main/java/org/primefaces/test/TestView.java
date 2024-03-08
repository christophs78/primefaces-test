package org.primefaces.test;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import lombok.Data;

@Data
@Named
@ViewScoped
public class TestView implements Serializable {

	private TimelineModel<String, ?> model;

	private LocalDateTime start;
	private LocalDateTime end;

	private boolean additionalEntries = false;
	private boolean extendedTimePeriod = false;

	@PostConstruct
	public void init() {
		// set initial start / end dates for the axis of the timeline
		start = LocalDate.now().minusDays(5).atStartOfDay();
		end = LocalDate.now().plusDays(5).atStartOfDay();

		createTimelineModel();
	}

	public void createTimelineModel() {
		// create timeline model
		model = new TimelineModel<>();

		for (int i = 1; i <= 10; i++) {
			int interleave = ((i * 2) - 11) * 2;

			LocalDateTime startDate = LocalDate.now().plusDays(6).plusDays(interleave).atStartOfDay();
			LocalDateTime endDate = startDate.plusDays(14);
			if (extendedTimePeriod) {
				endDate = endDate.plusMonths(1);
				// endDate = LocalDate.of(2999, 12, 31).atStartOfDay();
			}
			TimelineEvent event = TimelineEvent.builder().data("Max " + i + timerange(startDate, endDate)).startDate(startDate)
					.endDate(endDate).build();
			model.add(event);

			if (additionalEntries) {
				startDate = LocalDate.now().minusDays(10).plusDays(interleave).atStartOfDay();
				endDate = startDate.plusDays(12);
				event = TimelineEvent.builder().data("Lewis " + i + timerange(startDate, endDate)).startDate(startDate).endDate(endDate)
						.build();
				model.add(event);
			}
		}
	}

	private String timerange(LocalDateTime start, LocalDateTime end) {
		return " " + start.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE) + " - " + end.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	public void onSelect(TimelineSelectEvent<String> e) {
		TimelineEvent<String> timelineEvent = e.getTimelineEvent();

		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected event:", timelineEvent.getData());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public TimelineModel<String, ?> getModel() {
		return model;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public boolean isAdditionalEntries() {
		return additionalEntries;
	}

	public void setAdditionalEntries(boolean additionalEntries) {
		this.additionalEntries = additionalEntries;
	}

	public boolean isExtendedTimePeriod() {
		return extendedTimePeriod;
	}

	public void setExtendedTimePeriod(boolean extendedTimePeriod) {
		this.extendedTimePeriod = extendedTimePeriod;
	}
}
