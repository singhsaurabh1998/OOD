import java.util.*;

interface AutoCompleteEngine {
    List<String> getSuggestions(String prefix);
}

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord;
}

interface RankingStrategy {
    List<String> rank(List<String> words, Map<String, Integer> frequencyMap);
}

class FrequencyRankingStrategy implements RankingStrategy {
    public List<String> rank(List<String> words, Map<String, Integer> freqMap) {
        words.sort((a, b) -> freqMap.getOrDefault(b, 0) - freqMap.getOrDefault(a, 0));
        return words;
    }
}

class RecencyRankingStrategy implements RankingStrategy {
    private final Map<String, Long> lastUsed = new HashMap<>();

    public void updateUsage(String word) {
        lastUsed.put(word, System.currentTimeMillis());
    }

    @Override
    public List<String> rank(List<String> words, Map<String, Integer> freqMap) {
        words.sort((a, b) -> Long.compare(
                lastUsed.getOrDefault(b, 0L),
                lastUsed.getOrDefault(a, 0L)
        ));
        return words;
    }
}

class TrieAutoCompleteEngine implements AutoCompleteEngine {
    private final TrieNode root = new TrieNode();
    private final RankingStrategy strategy;
    private final Map<String, Integer> frequencyMap = new HashMap<>();

    public TrieAutoCompleteEngine(List<String> keyWords, RankingStrategy strategy) {
        this.strategy = strategy;
        for (String w : keyWords)
            insertWord(w);
    }

    private void insertWord(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            node = node.children.computeIfAbsent(ch, c -> new TrieNode());
        }
        node.isEndOfWord = true;
        frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
    }

    public List<String> getSuggestions(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toCharArray()) {
            node = node.children.get(ch);
            if (node == null) return Collections.emptyList();
        }
        List<String> results = new ArrayList<>();
        collectWords(node, prefix, results);
        System.out.println("your suggested words : "+ results);
        return strategy.rank(results, frequencyMap);
    }

    private void collectWords(TrieNode node, String prefix, List<String> results) {
        if (node.isEndOfWord) results.add(prefix);
        for (Map.Entry<Character, TrieNode> e : node.children.entrySet()) {
            collectWords(e.getValue(), prefix + e.getKey(), results);
        }
    }
}

enum LanguageContext {JAVA, PYTHON, JAVASCRIPT}

class KeywordRepository {
    public static List<String> getKeywords(LanguageContext lang) {
        switch (lang) {
            case JAVA:
                return Arrays.asList("public", "private", "protected", "static", "final", "abstract",
                        "class", "interface", "extends", "implements", "package", "import",
                        "int", "double", "float", "boolean", "char", "byte", "short", "long",
                        "if", "else", "while", "for", "do", "switch", "case", "default",
                        "try", "catch", "finally", "throw", "throws", "return", "break", "continue");
            case PYTHON:
                return Arrays.asList("def", "class", "if", "elif", "else", "while", "for", "in", "try", "except",
                        "finally", "with", "as", "import", "from", "return", "yield", "lambda",
                        "and", "or", "not", "is", "None", "True", "False", "pass", "break", "continue");
            case JAVASCRIPT:
                return Arrays.asList("var", "let", "const", "function", "if", "else", "while", "for", "do",
                        "switch", "case", "default", "try", "catch", "finally", "throw", "return",
                        "break", "continue", "class", "extends", "import", "export", "from");
            default:
                return Collections.emptyList();
        }
    }
}

class AutoCompleteFactory {
    public static AutoCompleteEngine getEngine(LanguageContext lang, RankingStrategy strategy) {
        return new TrieAutoCompleteEngine(KeywordRepository.getKeywords(lang), strategy);
    }
}

class AutoCompleteFacade {
    public List<String> getSuggestions(String prefix, LanguageContext lang) {
        AutoCompleteEngine engine = AutoCompleteFactory.getEngine(lang, new RecencyRankingStrategy());
        return engine.getSuggestions(prefix);
    }
}

public class AutoCompleteDemo {
    public static void main(String[] args) {
        AutoCompleteFacade facade = new AutoCompleteFacade();

        System.out.println(facade.getSuggestions("p", LanguageContext.JAVA));
        // [print, printf, private]

        System.out.println(facade.getSuggestions("cl", LanguageContext.PYTHON));
        // [class]
    }
}
