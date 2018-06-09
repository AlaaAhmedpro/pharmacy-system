package pharmacy.ExceptionClasses;

public class MissingDataException extends Exception{

    public MissingDataException(String string) {
        super(string);
    }

    public MissingDataException(String ops, String enter_the_password_) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
