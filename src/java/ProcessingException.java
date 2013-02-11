import clojure.lang.Keyword;

public class ProcessingException extends Exception {

    private final Keyword code;

    public ProcessingException(Keyword code) {
        super(code.toString());
        this.code = code;
    }

    public Keyword getErrorCode() {
        return this.code;
    }

}
