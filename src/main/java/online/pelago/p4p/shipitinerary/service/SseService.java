package online.pelago.p4p.shipitinerary.service;

import online.pelago.p4p.shipitinerary.dto.AsynchronousTask;

public interface SseService {

	void add(AsynchronousTask<?> at);

}
