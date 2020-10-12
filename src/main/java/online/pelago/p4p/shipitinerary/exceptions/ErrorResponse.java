package online.pelago.p4p.shipitinerary.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
    private String timestamp;
    private String error;
    private String message;
    private String path;
    private int code;
}
