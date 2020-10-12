package online.pelago.p4p.shipitinerary.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import online.pelago.p4p.shipitinerary.exceptions.BusinessRequirementViolationException;
import online.pelago.p4p.shipitinerary.exceptions.ElementNotFoundException;

public interface AsyncImportService {

	CompletableFuture<Void> importCsv(byte[] file, String username)
			throws BusinessRequirementViolationException, IOException, ElementNotFoundException;
}
