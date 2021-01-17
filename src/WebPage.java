public class WebPage {
    private Connector connector;
    private RepositoryFrame repoFrame;
    private String filePath;

    public WebPage(String name, String filePath) {
        this.filePath = filePath;
        connector = new Connector(name, this.filePath, "jdbc:postgresql://localhost:5432/shopifychallenge", "postgres", "password");
        if (!connector.create()) {
            System.out.print("Fail Connection");
            return;
        }
        //Add a test user
        connector.addUser("Tom", "Pam", "TipTop", "a123a123");
        repoFrame = new RepositoryFrame("Image Repository Example", filePath, connector);

    }

}
