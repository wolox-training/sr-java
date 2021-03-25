package wolox.training.factory;

public interface DataTestConstants {
    static final String GENRE = "Mystery";
    static final String AUTHOR = "Dan Brown";
    static final String IMAGE = "https://upload.wikimedia.org/wikipedia/en/b/bb/Inferno-cover.jpg";
    static final String TITLE = "Inferno";
    static final String SUBTITLE = "Robert Langdon series";
    static final String PUBLISHER = "Doubleday";
    static final String YEAR = "2013";
    static final String PAGES = "642";
    static final String ISBN = "978-0-385-53785-8";
    static final String NAME = "Sebastian Rincón";
    static final String USERNAME = "srincon";
    static final String PASSWORD = "81943301Se/";
    static final String BIRTHDATE = "1997-06-05";

    static final String AUTH_USERNAME = "jsrincon";
    static final String AUTH_PASSWORD = "81943301Se/";

     static final String BOOK_CONTENT_WITHOUT_ID ="{  \"author\": \"Dan Brown\",  \"genre\": \"Mystery\",  \"id\": null,  \"image\""+
            ": \"https://upload.wikimedia.org/wikipedia/en/b/bb/Inferno-cover.jpg\",  \"isbn\": \"978-0-385-53785-8\""+
            ",  \"pages\": \"642\",  \"publisher\": \"Doubleday\",  \"subtitle\": \"Robert Langdon series\",  \"title\":"+
            " \"Inferno\",  \"year\": \"2013\"}";

     static final String  BOOK_CONTENT ="{  \"author\": \"Dan Brown\",  \"genre\": \"Mystery\",  \"id\": 1,  \"image\""+
            ": \"https://upload.wikimedia.org/wikipedia/en/b/bb/Inferno-cover.jpg\",  \"isbn\": \"978-0-385-53785-8\""+
            ",  \"pages\": \"642\",  \"publisher\": \"Doubleday\",  \"subtitle\": \"Robert Langdon series\",  \"title\":"+
            " \"Inferno\",  \"year\": \"2013\"}";

     static final String  USER_CONTENT_WITHOUT_ID = "{\"id\": null,\"username\": \"srincon\",\"password\": \"81943301Se/\", \"name\": \"Sebastian Rincón\", \"birthdate\": \"2021-03-16T15:00:01.460Z\", \"books\":[]}";
     static final String  USER_CONTENT = "{\"id\": 1,\"username\": \"srincon\", \"password\": \"81943301Se/\",\"name\": \"Sebastian Rincón\", \"birthdate\": \"2021-03-16T15:00:01.460Z\", \"books\":[]}";
     static final String  PASSWORD_WRONG_CONTENT = "{\"password\": \"81943301Se\", \"verifiedPassword\": \"81943301Se/\", \"oldPassword\": \"81943301Se/\"}";
     static final String  PASSWORD_OLD_WRONG_CONTENT = "{\"password\": \"81943301Se\", \"verifiedPassword\": \"81943301Se/\", \"oldPassword\": \"81943301Se/\"}";
     static final String  PASSWORD_CONTENT = "{\"password\": \"81943301Se*\", \"verifiedPassword\": \"81943301Se*\", \"oldPassword\": \"81943301Se/\"}";

}
