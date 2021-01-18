public class MainDemo {
    private static WebPage webPage;

    public static void main(String[] args) {

        //Pass name and filepath to Webpage creator
        //In actual application should not be hard coded
        String name = "TESTREPO";
        String filePath = "C:\\Users\\Natalie\\Documents\\ShopifyFolder\\";
        webPage = new WebPage(name, filePath);

    }
}
