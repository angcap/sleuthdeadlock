package online.pelago.p4p.shipitinerary.mapper;

import org.springframework.stereotype.Component;

@Component
public class BooleanMapper {

	public Boolean asBoolean(String strBoolean) {
		return Boolean.parseBoolean(strBoolean) || "1".equals(strBoolean) || "t".equalsIgnoreCase(strBoolean)
				|| "y".equalsIgnoreCase(strBoolean);
	}

	public String asString(Boolean aBoolean) {
		return Boolean.TRUE.equals(aBoolean) ? "1" : "0";
	}
}
