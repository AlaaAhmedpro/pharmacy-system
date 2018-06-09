/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacy.PersonClasses;
import java.util.Objects;
import pharmacy.ExtraClasses.DateTime;
import pharmacy.ExtraClasses.Report;
public class AdminReport extends Staff{

    Report generetedReports;
    public AdminReport(String username, String password, String firstName, String lastName, String phone, String gender, DateTime dateOfBirth) {
        super(username, password, firstName, lastName, phone, gender, dateOfBirth);
    }

    public Report getGeneretedReports() {
        return generetedReports;
    }

    public void setGeneretedReports(Report generetedReports) {
        this.generetedReports = generetedReports;
    }

    @Override
    public String toString() {
        return "AdminReport{" + "generetedReports=" + generetedReports + '}';
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
        final AdminReport other = (AdminReport) obj;
        if (!Objects.equals(this.generetedReports, other.generetedReports)) {
            return false;
        }
        return true;
    }

    
}
