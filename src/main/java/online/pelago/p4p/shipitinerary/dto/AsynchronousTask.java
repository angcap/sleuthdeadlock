package online.pelago.p4p.shipitinerary.dto;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsynchronousTask<T> {
	private SseEmitter sseEmitter;
	private CompletableFuture<T> task;
}
