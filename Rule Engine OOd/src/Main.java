import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//chain of responsibility pattern
//rule engine to evaluate rules and execute actions based on conditions
//rules can be added dynamically
//example rules: if salary > 50000 then approve loan, if credit score < 600 then reject loan,
//if user is prime and cart value > 1000 then apply 10% discount
//context holds data for evaluation
//rules implement evaluate and execute methods
//rule engine iterates through rules, evaluates and executes if true

interface Rule {
    boolean evaluate(Context ctx);
    void execute(Context ctx);
}

class Context {
    private final Map<String, Object> data = new HashMap<>();

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }
}

class SalaryRule implements Rule {
    public boolean evaluate(Context ctx) {
        int salary = (int) ctx.get("salary");
        return salary > 50000;
    }

    public void execute(Context ctx) {
        System.out.println("Salary rule passed: APPROVE loan.");
    }
}

class CreditScoreRule implements Rule {
    public boolean evaluate(Context ctx) {
        int score = (int) ctx.get("creditScore");
        return score > 600;
    }

    public void execute(Context ctx) {
        System.out.println("Credit score Passed Accept loan.");
    }
}

class DiscountRule implements Rule {
    public boolean evaluate(Context ctx) {
        boolean isPrime = (boolean) ctx.get("isPrime");
        int cartValue = (int) ctx.get("cartValue");
        return isPrime && cartValue > 1000;
    }

    public void execute(Context ctx) {
        System.out.println("Prime user: 10% discount applied.");
    }
}

class RuleEngine {
    private final List<Rule> rules = new ArrayList<>();

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void run(Context ctx) {
        for (Rule rule : rules) {
            //true if evaluate passes
            if (rule.evaluate(ctx)) {
                rule.execute(ctx);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        RuleEngine engine = new RuleEngine();

        engine.addRule(new SalaryRule());
        engine.addRule(new CreditScoreRule());
        engine.addRule(new DiscountRule());

        Context ctx = new Context();
        ctx.put("salary", 60000);
        ctx.put("creditScore", 720);
        ctx.put("isPrime", true);
        ctx.put("cartValue", 1500);

        engine.run(ctx);
    }
}
