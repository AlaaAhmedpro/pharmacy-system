package pharmacy.ExtraClasses;

public class Report {
    private int id;
    private String date;
    private int numSales;
    private double totalSales;
    private double salesPercentage;

    public Report(int id, String date, int numSales, double totalSales, double salesPercentage) {
        this.id = id;
        this.date = date;
        this.numSales = numSales;
        this.totalSales = totalSales;
        this.salesPercentage = salesPercentage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumSales() {
        return numSales;
    }

    public void setNumSales(int numSales) {
        this.numSales = numSales;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public double getSalesPercentage() {
        return Double.parseDouble(String.format("%.2f", salesPercentage));
    }

    public void setSalesPercentage(double salesPercentage) {
        this.salesPercentage = salesPercentage;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", numSales=" + numSales +
                ", totalSales=" + totalSales +
                ", salesPercentage=" + salesPercentage +
                '}';
    }
}
