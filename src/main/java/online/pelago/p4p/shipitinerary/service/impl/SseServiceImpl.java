package online.pelago.p4p.shipitinerary.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import online.pelago.p4p.shipitinerary.dto.AsynchronousTask;
import online.pelago.p4p.shipitinerary.dto.SseCompletedTaskWithOutputResponseDto;
import online.pelago.p4p.shipitinerary.service.SseService;

@Service
@Slf4j
@SuppressWarnings("rawtypes")
public class SseServiceImpl implements SseService {
	
	private static final String COMPLETED = "Completed";
	private static final String ERROR = "Error";
	
	private List<AsynchronousTask> pendingRequests = new ArrayList<>();
	
	@Override
	public void add(AsynchronousTask at) {
		this.pendingRequests.add(at);
	}
	
	@Scheduled(fixedRate = 5000)
	void checkPendingRequests() {
		if (this.pendingRequests.isEmpty()) {
			return;
		}
		this.pendingRequests.stream().forEach(st -> {
			try {
				if (st.getTask().isCompletedExceptionally()) {
					this.completeWithError(st);
				} else if (st.getTask().isDone()) {
					this.completeSuccessfully(st);
				} else {
					st.getSseEmitter().send("Pending");
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}

		});

		this.pendingRequests = this.pendingRequests.stream()
				.filter(st -> !st.getTask().isDone() && !st.getTask().isCompletedExceptionally())
				.collect(Collectors.toList());
	}

	private void completeWithError(AsynchronousTask st) throws IOException {
		try {
			st.getTask().get();
		}catch (Exception e) {
			st.getSseEmitter().send(new SseCompletedTaskWithOutputResponseDto(ERROR, e.getCause().getMessage()));
			st.getSseEmitter().complete();

		}
	}

	private void completeSuccessfully(AsynchronousTask st) throws IOException {
		try {
			if(st.getTask().get() instanceof Void || st.getTask().get() == null) {
				st.getSseEmitter().send(COMPLETED);
			} else {
				st.getSseEmitter().send(new SseCompletedTaskWithOutputResponseDto(COMPLETED, st.getTask().get()));
			}			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			st.getSseEmitter().send(COMPLETED);
		} 
		st.getSseEmitter().complete();
	}

}
