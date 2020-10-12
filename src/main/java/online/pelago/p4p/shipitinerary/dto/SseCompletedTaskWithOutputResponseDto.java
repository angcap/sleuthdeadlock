package online.pelago.p4p.shipitinerary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SseCompletedTaskWithOutputResponseDto {

	private String status;
	private Object result;
}
