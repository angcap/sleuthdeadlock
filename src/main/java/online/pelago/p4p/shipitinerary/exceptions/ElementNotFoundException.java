package online.pelago.p4p.shipitinerary.exceptions;

public class ElementNotFoundException extends Exception {
	private static final long serialVersionUID = -8729169303699924451L;

	public ElementNotFoundException() {
		super("Element is not found.");
	}

	public ElementNotFoundException(String messaggio) {
		super(messaggio);
	}

}
