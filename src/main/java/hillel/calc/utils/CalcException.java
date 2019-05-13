package hillel.calc.utils;

public class CalcException extends Exception {

    private String detailMsg;

    public CalcException(String message, String detailMsg) {
        super(message);
        this.detailMsg = detailMsg;
    }

    public CalcException(String message, Throwable cause, String detailMsg) {
        super(message, cause);
        this.detailMsg = detailMsg;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }
}
