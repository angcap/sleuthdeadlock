package online.pelago.p4p.shipitinerary.service.impl;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import online.pelago.p4p.shipitinerary.exceptions.BusinessRequirementViolationException;
import online.pelago.p4p.shipitinerary.exceptions.ElementNotFoundException;
import online.pelago.p4p.shipitinerary.service.AsyncImportService;
import online.pelago.p4p.shipitinerary.service.ImportExportService;

@Service
public class AsyncImportServiceImpl implements AsyncImportService {

	@Autowired
	@Lazy
	private ImportExportService importService;
	
	@Override
	@Async("taskExecutor")
	public CompletableFuture<Void> importCsv(byte[] file, String username)
			throws BusinessRequirementViolationException, IOException, ElementNotFoundException {
		this.importService.importCsv(file, username);
		return CompletableFuture.completedFuture(null);
	}

}
