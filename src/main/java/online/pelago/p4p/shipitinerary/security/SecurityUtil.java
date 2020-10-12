package online.pelago.p4p.shipitinerary.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
	
	private SecurityUtil() {
		//private constructor
	}

	public static void setSecurityContext(final Authentication a) {
		SecurityContext ctx = SecurityContextHolder.createEmptyContext();
		ctx.setAuthentication(a);
		SecurityContextHolder.setContext(ctx);
	}
}
