import java.util.HashMap;
import java.util.Map;

public class TagNode {
    private final String tag;
    private int count;
    private final Map<String, TagNode> children;

    public TagNode(String tag) {
        this.tag = tag;
        this.count = 0;
        this.children = new HashMap<>();//by default it will have no children
    }

    public String getTag() {
        return tag;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }

    public void decrementCount() {
        if (count > 0) count--;
    }

    public Map<String, TagNode> getChildren() {
        return children;
    }

    public TagNode getOrCreateChild(String childTag) {
        if (!children.containsKey(childTag)) {
            children.put(childTag, new TagNode(childTag));
        }
        return children.get(childTag);

    }

    public TagNode getChild(String childTag) {
        return children.get(childTag);
    }
}

