package edu.java.bot.model.url_utils.URLresources;

public class ResourceStackOverflow implements Resource {
    private final String resourceFromURL;
    private static final String STACKOVERFLOW_HOST = "github.com";

    public ResourceStackOverflow(String resourceFromURL) {
        this.resourceFromURL = resourceFromURL;
    }

    @Override
    public String nameHost() {
        return STACKOVERFLOW_HOST;
    }

    @Override
    public String nameResource() {
        return resourceFromURL;
    }
}
