import twitter4j.HashtagEntity;

public class TestHashtagEntity implements HashtagEntity {
    private final String text;

    public TestHashtagEntity(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return this.text;
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
