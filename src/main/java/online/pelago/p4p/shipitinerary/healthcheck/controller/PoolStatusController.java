package online.pelago.p4p.shipitinerary.healthcheck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import online.pelago.p4p.shipitinerary.healthcheck.service.PoolStatusService;

@CrossOrigin(maxAge = 3600)
@RestController
public class PoolStatusController {

	@Autowired
	private PoolStatusService hcs;
	
	@GetMapping("/pool/status")
	public void poolstatus() {
		hcs.checkStatus();
	}
}
