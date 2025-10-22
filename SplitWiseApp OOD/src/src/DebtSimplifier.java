import java.util.*;

// Forward declarations equivalent - not needed in Java due to automatic resolution

class DebtSimplifier {
    public static Map<String, Map<String, Double>> simplifyDebts(
        Map<String, Map<String, Double>> groupBalances) {
        
        // Calculate net amount for each person
        Map<String, Double> netAmounts = new HashMap<>();
        
        // Initialize all users with 0
        for (Map.Entry<String, Map<String, Double>> userBalance : groupBalances.entrySet()) {
            netAmounts.put(userBalance.getKey(), 0.0);
        }
        
        // Calculate net amounts
        // We only need to process each balance once (not twice)
        // If groupBalances[A][B] = 200, it means B owes A 200
        // So A should receive 200 (positive) and B should pay 200 (negative)
        for (Map.Entry<String, Map<String, Double>> userBalance : groupBalances.entrySet()) {
            String creditorId = userBalance.getKey();
            for (Map.Entry<String, Double> balance : userBalance.getValue().entrySet()) {
                String debtorId = balance.getKey();
                double amount = balance.getValue();
                
                // Only process positive amounts to avoid double counting
                if (amount > 0) {
                    netAmounts.put(creditorId, netAmounts.get(creditorId) + amount);  // creditor receives
                    netAmounts.put(debtorId, netAmounts.get(debtorId) - amount);    // debtor pays
                }
            }
        }
        
        // Divide users into creditors and debtors
        List<AbstractMap.SimpleEntry<String, Double>> creditors = new ArrayList<>(); // those who should receive money
        List<AbstractMap.SimpleEntry<String, Double>> debtors = new ArrayList<>();   // those who should pay money
        
        for (Map.Entry<String, Double> net : netAmounts.entrySet()) {
            if (net.getValue() > 0.01) { // creditor
                creditors.add(new AbstractMap.SimpleEntry<>(net.getKey(), net.getValue()));
            } else if (net.getValue() < -0.01) { // debtor
                debtors.add(new AbstractMap.SimpleEntry<>(net.getKey(), -net.getValue())); // store positive amount
            }
        }
        
        // Sort for better optimization (largest amounts first)
        creditors.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        debtors.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        
        // Create new simplified balance map
        Map<String, Map<String, Double>> simplifiedBalances = new HashMap<>();
        
        // Initialize empty maps for all users
        for (Map.Entry<String, Map<String, Double>> userBalance : groupBalances.entrySet()) {
            simplifiedBalances.put(userBalance.getKey(), new HashMap<>());
        }
        
        // Use greedy algorithm to minimize transactions
        int i = 0, j = 0;
        while (i < creditors.size() && j < debtors.size()) {
            String creditorId = creditors.get(i).getKey();
            String debtorId = debtors.get(j).getKey();
            double creditorAmount = creditors.get(i).getValue();
            double debtorAmount = debtors.get(j).getValue();
            
            // Find the minimum amount to settle
            double settleAmount = Math.min(creditorAmount, debtorAmount);
            
            // Update simplified balances
            // debtorId owes creditorId the settleAmount
            simplifiedBalances.get(creditorId).put(debtorId, settleAmount);
            simplifiedBalances.get(debtorId).put(creditorId, -settleAmount);
            
            // Update remaining amounts
            creditors.get(i).setValue(creditors.get(i).getValue() - settleAmount);
            debtors.get(j).setValue(debtors.get(j).getValue() - settleAmount);
            
            // Move to next creditor or debtor if current one is settled
            if (creditors.get(i).getValue() < 0.01) {
                i++;
            }
            if (debtors.get(j).getValue() < 0.01) {
                j++;
            }
        }
        
        return simplifiedBalances;
    }
}

