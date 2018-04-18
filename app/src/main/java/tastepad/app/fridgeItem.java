package tastepad.app;

public class fridgeItem {

    private String Name;
    private String Date;

    public fridgeItem() {
    }

    public fridgeItem(String name, String date) {
        Name = name;
        Date = date;
    }


    // getters
    public String getName() {
        return Name;
    }

    public String getDate() {
        return Date;
    }

    // setters
    public void setName(String name) {
        Name = name;
    }

    public void setDate(String date) {
        Date = date;
    }
}



