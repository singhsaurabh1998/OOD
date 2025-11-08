import java.util.ArrayList;
import java.util.Comparator;
import java.util.List; /////////////////////////////////////////////

public class DarkStoreManager {
    private static DarkStoreManager instance;
    private final List<DarkStore> darkStores;

    private DarkStoreManager() {
        darkStores = new ArrayList<>();
    }

    public static DarkStoreManager getInstance() {
        if (instance == null) {
            instance = new DarkStoreManager();
        }
        return instance;
    }

    public void registerDarkStore(DarkStore ds) {
        darkStores.add(ds);
    }

    public List<DarkStore> getNearbyDarkStores(double ux, double uy, double maxDistance) {
        List<Pair<Double, DarkStore>> distList = new ArrayList<>();
        for (DarkStore ds : darkStores) {
            double d = ds.distanceTo(ux, uy);
            if (d <= maxDistance) {
                distList.add(new Pair<>(d, ds));
            }
        }
        distList.sort(Comparator.comparing(Pair::getKey));
        List<DarkStore> result = new ArrayList<>();
        for (Pair<Double, DarkStore> p : distList) {
            result.add(p.getValue());
        }
        return result;
    }
}
/////////////////////////////////////////////
// DeliveryPartner

