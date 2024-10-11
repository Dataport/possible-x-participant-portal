package eu.possible_x.edc_orchestrator.entities;

public class ExceptionTO {
    private int httpStatusCode;
    private String message;

    public ExceptionTO(int newHttpStatusCode, String newMessage) {
        this.httpStatusCode = newHttpStatusCode;
        this.message = newMessage;
    }
    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getMessage() {
        return message;
    }
}
