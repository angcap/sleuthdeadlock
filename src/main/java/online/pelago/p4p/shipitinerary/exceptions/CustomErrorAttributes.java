package online.pelago.p4p.shipitinerary.exceptions;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring boot catches all web error and exceptions and returns a response with several attributes
 * that includes the trace property in case of exception.
 *
 * The component remove the trace and add the "code" attribute for a bounce of http response statuses.
 */
@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    public static final String TIMESTAMP = "timestamp";

    public static final DateFormat isoDatetimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static final Map<Integer,Integer> http2CustomCodeMap = new HashMap<>();

    static {
        http2CustomCodeMap.put(HttpStatus.UNAUTHORIZED.value(),1000);
        http2CustomCodeMap.put(HttpStatus.FORBIDDEN.value(),1001);
        http2CustomCodeMap.put(HttpStatus.NOT_FOUND.value(),1002);
        http2CustomCodeMap.put(HttpStatus.BAD_REQUEST.value(),1003);
        http2CustomCodeMap.put(HttpStatus.INTERNAL_SERVER_ERROR.value(),1005);
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {

        // Let Spring handle the error first, we will modify later
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);

        Object timestamp = errorAttributes.get(TIMESTAMP);
        if (timestamp == null) {
            errorAttributes.put(TIMESTAMP, isoDatetimeFormatter.format(new Date()));
        } else {
            errorAttributes.put(TIMESTAMP, isoDatetimeFormatter.format((Date) timestamp));
        }
        errorAttributes.remove("trace");

        Integer status = (Integer) errorAttributes.remove("status");
        errorAttributes.put("code", http2CustomCodeMap.get(status));

        return errorAttributes;

    }

}
