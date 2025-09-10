abstract class SupportHandler {
    protected SupportHandler nextHandler;

    public void setNextHandler(SupportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handleRequest(String issueLevel);
}

class Agent extends SupportHandler {
    public void handleRequest(String issueLevel) {
        if (issueLevel.equals("LOW")) {
            System.out.println("Agent handled the LOW level issue.");
        } else if (nextHandler != null) {
            nextHandler.handleRequest(issueLevel);
        }
    }
}

class TeamLead extends SupportHandler {
    public void handleRequest(String issueLevel) {
        if (issueLevel.equals("MEDIUM")) {
            System.out.println("TeamLead handled the MEDIUM level issue.");
        } else if (nextHandler != null) {
            nextHandler.handleRequest(issueLevel);
        }
    }
}

class Manager extends SupportHandler {
    public void handleRequest(String issueLevel) {
        if (issueLevel.equals("HIGH")) {
            System.out.println("Manager handled the HIGH level issue.");
        } else {
            System.out.println("Issue could not be handled.");
        }
    }
}

public class ChainOfResponsibilityDemo {
    public static void main(String[] args) {
        // Create handlers
        SupportHandler agent = new Agent();
        SupportHandler teamLead = new TeamLead();
        SupportHandler manager = new Manager();

        // Set chain: Agent → TeamLead → Manager
        agent.setNextHandler(teamLead);
        teamLead.setNextHandler(manager);

        // Test different issues
        agent.handleRequest("LOW");    // handled by Agent
        agent.handleRequest("MEDIUM"); // passed to TeamLead
        agent.handleRequest("HIGH");   // passed to Manager
        agent.handleRequest("CRITICAL"); // not handled
    }
}
