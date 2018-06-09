package pharmacy.PersonClasses;
import java.io.Serializable;
import pharmacy.ExceptionClasses.InvalidInputException;
import pharmacy.ExtraClasses.DateTime;
public abstract class Person implements Serializable{
    //--------(Attributes)----------//
    protected int ID;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String Phone;
    protected String gender;
    protected DateTime dateOfBirth;
    //---------(Methods)-------------//
    
    //Method for generating new ID for new user
    private int generateID(){
        return pharmacy.MainClasses.PharmacyController.PeopleTable.getMaxId() + 1;
    }
    
    //Constructors
    public Person() throws InvalidInputException {
        ID = generateID();
        username = "";
        password = "";
        firstName = "";
        lastName = "";
        gender = "";
        Phone = "";
        dateOfBirth = new DateTime();
    }
  
    public Person(String username, String password, String firstName, String lastName, String phone, String gender, DateTime dateOfBirth) {
        ID = generateID();
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.Phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
    
    //Mutator Methods (Setter)
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(DateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public void setDateOfBirth(int Day, int Month, int Year) throws InvalidInputException {
        dateOfBirth.setDate(Day, Month, Year);
    }
   
    //Accessor Methods (Getter)

    public int getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return Phone;
    }

    public String getGender() {
        return gender;
    }

    public DateTime getDateOfBirth() {
        return dateOfBirth;
    }
    
    //Addition Methods
    @Override
    public String toString() {
        return this.ID + ", " + this.username + ", " + this.firstName + ", " + this.gender;
    }
    @Override
    public boolean equals(Object p){
        if (!(p instanceof Person)) return false;
        if (((Person)p).getID() == this.getID())
            return true;
        else 
            return false;
    }

 }