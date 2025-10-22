package observer;

import models.Order;

public interface OrderObserver {
    void update(Order order);
}
