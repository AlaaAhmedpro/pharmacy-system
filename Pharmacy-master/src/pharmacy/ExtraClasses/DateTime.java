package pharmacy.ExtraClasses;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import pharmacy.ExceptionClasses.InvalidInputException;
public class DateTime implements Serializable{
    //--------(Attributes)----------//
    private int seconds, minutes, hours;
    private int day, month, year;
    //---------(Methods)-------------//
    private String format(int value) {
        if (value < 10 && value >= 0)
            return "0" + value;
        else
            return Integer.toString(value);
    } 
    //Constructor Methods
    public DateTime() throws InvalidInputException {
        this.setTime(
            LocalTime.now().getSecond(), LocalTime.now().getMinute(), LocalTime.now().getHour()
        );
        this.setDate(
            LocalDate.now().getDayOfMonth(), LocalDate.now().getMonthValue(), LocalDate.now().getYear()
        );
    }
    public DateTime(int s, int m, int h, int D, int M, int Y) throws InvalidInputException{
        this.setTime(s, m, h);
        this.setDate(D, M, Y);
    }
    //Mutator Methods (Setter)
    public void setTime(int s, int m, int h) throws InvalidInputException{
        if (s > 59 || s < 0) throw new InvalidInputException("Invalid Second input");
        if (m > 59 || m < 0) throw new InvalidInputException("Invalid Minute input");
        if (h > 23 || h < 0) throw new InvalidInputException("Invalid Hours input");
        this.seconds = s;
        this.minutes = m;
        this.hours = h;
    }
    public void setDate(int D, int M, int Y) throws InvalidInputException {
        if (D > 31 || D < 1) throw new InvalidInputException("Invalid Day input");
        if (M > 12 || M < 1) throw new InvalidInputException("Invalid Month input");
        if (Y < 0) throw new InvalidInputException("Invalid Year input");
        this.day = D;
        this.month = M;
        this.year = Y;
    }
    //Accessor Methods (Getter)
    public int getSecond() {
        return this.seconds;
    }
    public int getMinute() {
        return this.minutes;
    }
    public int getHour() {
        return this.hours;
    }
    public int getDay() {
        return this.day;
    }
    public int getMonth() {
        return this.month;
    }
    public int getYear() {
        return this.year;
    }
    //Addition Methods
    public String getDate() {
        return format(getDay()) + "/" + format(getMonth()) + "/" + getYear();
    }
    public String getTime() {
        return format(getHour()) + ":" + format(getMinute()) + ":" + format(getSecond());
    }

    @Override
    public String toString() {
        return getTime() + " " + getDate();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DateTime other = (DateTime) obj;
        if (this.day != other.day) {
            return false;
        }
        if (this.month != other.month) {
            return false;
        }
        if (this.year != other.year) {
            return false;
        }
        return true;
    }
    
    
    
}