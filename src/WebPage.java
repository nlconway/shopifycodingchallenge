class WebPage {
    private Connector connector;
    private RepositoryFrame repoFrame;
    private String filePath;

    //Initial webpage
    //Will make a connection with the database using the name and filepath given
    //Will login with postgres database authentication details (url, username, password), here hardcoded
    WebPage(String name, String filePath) {
        this.filePath = filePath;

        //Hard coded database credentials
        String url = "jdbc:postgresql://localhost:5432/shopifychallenge";
        String userName = "postgres";
        String password = "password";

        connector = new Connector(name, this.filePath, url, userName, password);
        if (!connector.create()) {
            System.out.print("Fail Connection");
            return;
        }
        //Add a test user
        connector.addUser("Tom", "Pam", "TipTop", "a123a123");

        //Create gui frame
        repoFrame = new RepositoryFrame("Image Repository Example", filePath, connector);

    }

}
