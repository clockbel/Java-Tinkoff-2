package edu.java.bot.model.url_utils.URLresources;

public class ResourceGitHub implements Resource {
    private final String resourceFromURL;

    public ResourceGitHub(String resourceFromURL) {
        this.resourceFromURL = resourceFromURL;
    }

    private static final String GITHUB_HOST = "github.com";

    @Override
    public String nameHost() {
        return GITHUB_HOST;
    }

    @Override
    public String nameResource() {
        return resourceFromURL;
    }

}
