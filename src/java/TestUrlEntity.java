import twitter4j.URLEntity;

import java.net.MalformedURLException;
import java.net.URL;

import static com.google.common.base.Throwables.propagate;

public class TestUrlEntity implements URLEntity {

    private final URL url;
    private final URL expandedUrl;
    private final String displayUrl;

    public TestUrlEntity(String url, String expandedUrl, String displayUrl) {
        try {
            this.url = new URL(url);
            this.expandedUrl = new URL(expandedUrl);
            this.displayUrl = displayUrl;
        } catch (MalformedURLException e) {
            throw propagate(e);
        }
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public URL getExpandedURL() {
        return expandedUrl;
    }

    @Override
    public String getDisplayURL() {
        return displayUrl;
    }

    @Override
    public int getStart() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getEnd() {
        throw new UnsupportedOperationException();
    }
}
