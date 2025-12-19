Nice — let’s do this cleanly and get you interview-ready.

Below I’ll:

1. List **primary requirements** (functional + non-functional).
2. Propose a **clean OO design** with responsibilities and chosen design patterns (and why).
3. Give a **UML (PlantUML)** diagram you can paste into any PlantUML viewer.
4. Describe key classes, interfaces, and important behaviors (sequence for “park a vehicle”).

After this you’ll be ready to ask me to implement the core parts (I can produce Java code next).

---

# 1) Primary requirements

## Functional (must-have)

* Add vehicles entering the parking lot and assign them a spot (Park).
* Release a parked vehicle (Unpark) and compute fee.
* Query available spots / occupancy per floor / per spot type.
* Support different vehicle types (Motorcycle, Car, Truck) and multiple spot sizes.
* Support reservations (optional/simple).
* Issue and validate parking tickets (ticket id, entry time, spot).
* Support prioritized allocation rules (handicapped/EV/compact preference).

## Non-functional / constraints

* Thread-safe (concurrent entries/exits).
* Extensible (new spot types, pricing strategies, allocation strategies).
* Simple persistence (tickets/history) for audit and billing.
* Low-latency allocation (should be O(1) or O(log n) ideally).
* Observability: publish events (occupancy change / full lot / alert).

---

# 2) High-level design decisions & patterns

* **Primary data structure**: Each floor holds pools / priority structures (e.g., `TreeSet` or `PriorityQueue`) per spot type for fast allocation.
* **SOLID**:

    * S: Each class has single responsibility (Spot, Vehicle, Ticket, Payment, Allocator).
    * O: Add new allocation or pricing strategies without modifying core logic (`Allocator` and `PricingStrategy` interfaces).
    * L: Subtypes of `Vehicle` and `Spot` follow Liskov — replaceable.
    * I: Clients depend on small focused interfaces (`IParkingLot`, `IParkingAllocator`).
    * D: High-level code depends on abstractions (strategy interfaces), not concrete allocators.
* **Design Patterns**:

    * **Singleton** — `ParkingLotManager` or `ParkingLot` (single logical lot instance).
    * **Factory** — `VehicleFactory` / `SpotFactory` to create different types.
    * **Strategy** — `ParkingSpotAllocator` (Nearest, FirstFit, ReservedFirst), `PricingStrategy` (flat, hourly).
    * **Observer / Pub-Sub** — `NotificationService` to notify admins / users when lot is full or events occur.
    * **Command (optional)** — for actions (ParkCommand, UnparkCommand) to support queueing/retries.
    * **DAO/Repository** — for persistence of tickets/history.

---

# 3) UML (PlantUML) — paste into PlantUML tool

![img_1.png](img_1.png)
---

# 4) Key classes & responsibilities (summary)

### `ParkingLot` (Facade / Singleton)

* Responsibilities: orchestrates park/unpark operations, delegates spot selection to `IParkingAllocator`, applies pricing, persists tickets, emits events.
* Methods: `parkVehicle(Vehicle)`, `unparkVehicle(ticketId)`, `getAvailability()`.

### `Floor`

* Holds the physical spots, keeps per-SpotType structures for fast lookup (e.g., `TreeSet` ordered by spot id or distance).
* Thread-safe operations to mark spot occupied/free.

### `ParkingSpot`

* Holds `id`, `SpotType`, `occupied` flag, metadata (isEV, distanceFromEntrance maybe).

### `Vehicle` (abstract) and concrete `Car`, `Motorcycle`, `Truck`

* Holds registration, optionally owner info, special flags (handicapped).

### `Ticket` / `TicketRepository`

* Ticket: issued at entry, used to unpark. Repository persists tickets and history.

### `IParkingAllocator` (Strategy)

* Implementations decide which `ParkingSpot` to choose:

    * `NearestAllocator` — choose spot closest to entrance.
    * `FirstFitAllocator` — first available spot by type.
    * `ReservedFirstAllocator` — reserve certain spots for VIP/Handicapped.

### `IPricingStrategy`

* Implementations: `HourlyPricing` (base+per-hour), `FlatRatePricing`, `GracePeriodPricing`.

### `EntrancePanel` / `ExitPanel`

* Lightweight UI/API that calls `ParkingLot`. The Entrance uses `VehicleFactory` to create `Vehicle` objects.

### `NotificationService` (Observer)

* Admins/UI can subscribe to occupancy/full events. Optionally notify users (SMS/email).

---

# 5) Sequence: Park a vehicle (simplified)

1. User arrives at `EntrancePanel.scanAndPark(vehicleInfo)`.
2. `EntrancePanel` creates `Vehicle` via `VehicleFactory`.
3. `EntrancePanel` calls `ParkingLot.parkVehicle(vehicle)`.
4. `ParkingLot` calls `allocator.allocateSpot(this, vehicle)` to get a `ParkingSpot`.
5. If spot available:

    * mark `ParkingSpot.assign(vehicle)` (atomic operation).
    * create `Ticket` (ticketId, entryTime, spotId, vehicle).
    * persist via `TicketRepository.save(ticket)`.
    * notify observers via `NotificationService.notify(Event.PARKED)`.
    * return `Ticket` to entrance/user.
6. If none available:

    * optionally add to waitlist or return failure.

---

# 6) Concurrency & performance notes

* **Locking granularity**: synchronize at `ParkingSpot` or per-SpotType pool rather than at whole lot to increase throughput.
* Use `ConcurrentHashMap`, `ConcurrentLinkedQueue` or `PriorityBlockingQueue` for thread-safe pools.
* Use optimistic locking or `compareAndSet` for spot allocation to avoid double-assignments.
* For large lots, partitioning by floor / entrance reduces contention.

---

# 7) Persistence & replay

* Store tickets and occupancy events in durable repo (RDBMS/NoSQL). Use idempotent writes and event sourcing if you want full audit.
* On restart, restore spot states from persisted active tickets.

---

# 8) Extensibility examples (how SOLID + patterns help)

* New pricing model? Add new class implementing `IPricingStrategy` — no change to `ParkingLot`.
* New allocation rule? Add a new `IParkingAllocator`.
* Add EV charging spots? Add `SpotType.ELECTRIC` and possibly `ChargingSpot` subclass.

---

# Next step

**API (REST)**  
Base: `/api/v1`
- `POST /vehicles` body: `{number, type}` -> register vehicle
- `GET /vehicles/{number}`
- `GET /floors` -> list floors with spot summary
- `GET /floors/{floorId}/spots` -> list spots for floor
- `GET /spots/{spotId}` -> spot detail
- `POST /park ` body: `{vehicleNumber}` -> allocate spot & create ticket
- `GET /tickets/{ticketId}`
- `DELETE /api/v1/unpark/{ticketId}` -> unpark (vacate spot, close ticket)
- `GET /tickets?active=true` -> active tickets
- `GET /events?vehicleNumber=...` -> event history (park/unpark)
- `POST /observers` body: `{channel:"SMS"|"WHATSAPP"}` -> register observer (optional)
- `DELETE /observers/{observerId}`

Responses should include clear status + data + error codes (e.g. 404 for unknown ticket, 409 for no spot). Unpark should be idempotent.
# Sample API Request/Response Examples

Based on the **Parking Lot** design with `ParkingLot`, `Floor`, `ParkingSpot`, `Ticket`, and `NotificationService`:

---

## 1. POST `/api/v1/vehicles` — Register Vehicle

**Request:**
```json
POST /api/v1/vehicles
Content-Type: application/json

{
  "number": "KA-01-HH-1234",
  "type": "CAR"
}
```

**Response (201 Created):**
```json
{
  "status": "success",
  "data": {
    "number": "KA-01-HH-1234",
    "type": "CAR",
    "createdAt": "2025-06-15T10:30:00Z"
  }
}
```

**Error (409 Conflict):**
```json
{
  "status": "error",
  "code": 409,
  "message": "Vehicle with number KA-01-HH-1234 already registered"
}
```

---

## 2. GET `/api/v1/vehicles/{number}` — Get Vehicle Details

**Request:**
```
GET /api/v1/vehicles/KA-01-HH-1234
```

**Response (200 OK):**
```json
{
  "status": "success",
  "data": {
    "number": "KA-01-HH-1234",
    "type": "CAR",
    "createdAt": "2025-06-15T10:30:00Z"
  }
}
```

**Error (404 Not Found):**
```json
{
  "status": "error",
  "code": 404,
  "message": "Vehicle not found"
}
```

---

## 3. GET `/api/v1/floors` — List Floors with Spot Summary

**Request:**
```
GET /api/v1/floors
```

**Response (200 OK):**
```json
{
  "status": "success",
  "data": [
    {
      "floorId": 1,
      "totalSpots": 50,
      "availableSpots": 32,
      "spotSummary": {
        "COMPACT": { "total": 20, "available": 15 },
        "MEDIUM": { "total": 20, "available": 12 },
        "LARGE": { "total": 10, "available": 5 }
      }
    },
    {
      "floorId": 2,
      "totalSpots": 50,
      "availableSpots": 40,
      "spotSummary": {
        "COMPACT": { "total": 20, "available": 18 },
        "MEDIUM": { "total": 20, "available": 17 },
        "LARGE": { "total": 10, "available": 5 }
      }
    }
  ]
}
```

---

## 4. GET `/api/v1/floors/{floorId}/spots` — List Spots for Floor

**Request:**
```
GET /api/v1/floors/1/spots
```

**Response (200 OK):**
```json
{
  "status": "success",
  "data": {
    "floorId": 1,
    "spots": [
      {
        "spotId": 101,
        "spotType": "COMPACT",
        "isOccupied": false,
        "currentVehicle": null
      },
      {
        "spotId": 102,
        "spotType": "COMPACT",
        "isOccupied": true,
        "currentVehicle": "KA-01-HH-1234"
      },
      {
        "spotId": 103,
        "spotType": "MEDIUM",
        "isOccupied": false,
        "currentVehicle": null
      }
    ]
  }
}
```

---

## 5. GET `/api/v1/spots/{spotId}` — Spot Detail

**Request:**
```
GET /api/v1/spots/102
```

**Response (200 OK):**
```json
{
  "status": "success",
  "data": {
    "spotId": 102,
    "floorId": 1,
    "spotType": "COMPACT",
    "isOccupied": true,
    "currentVehicle": "KA-01-HH-1234",
    "distanceFromEntrance": 25
  }
}
```

**Error (404):**
```json
{
  "status": "error",
  "code": 404,
  "message": "Spot not found"
}
```

---

## 6. POST `/api/v1/park` — Park Vehicle (Allocate Spot & Create Ticket)

**Request:**
```json
POST /api/v1/tickets
Content-Type: application/json

{
  "vehicleNumber": "KA-01-HH-1234"
}
```

**Response (201 Created):**
```json
{
  "status": "success",
  "data": {
    "ticketId": "TKT-1718450400-102",
    "vehicleNumber": "KA-01-HH-1234",
    "spotId": 102,
    "floorId": 1,
    "entryTime": "2025-06-15T10:40:00Z",
    "status": "ACTIVE"
  }
}
```

**Error (409 Conflict — No Spot Available):**
```json
{
  "status": "error",
  "code": 409,
  "message": "No available parking spot for vehicle type CAR"
}
```

**Error (409 — Vehicle Already Parked):**
```json
{
  "status": "error",
  "code": 409,
  "message": "Vehicle KA-01-HH-1234 already has an active ticket"
}
```

---

## 7. GET `/api/v1/tickets/{ticketId}` — Get Ticket Details

**Request:**
```
GET /api/v1/tickets/TKT-1718450400-102
```

**Response (200 OK):**
```json
{
  "status": "success",
  "data": {
    "ticketId": "TKT-1718450400-102",
    "vehicleNumber": "KA-01-HH-1234",
    "spotId": 102,
    "floorId": 1,
    "entryTime": "2025-06-15T10:40:00Z",
    "exitTime": null,
    "status": "ACTIVE"
  }
}
```

---

## 8. DELETE `/api/v1/unpark/{ticketId}` — Unpark Vehicle

**Request:**
```
DELETE /api/v1/tickets/TKT-1718450400-102
```

**Response (200 OK):**
```json
{
  "status": "success",
  "data": {
    "ticketId": "TKT-1718450400-102",
    "vehicleNumber": "KA-01-HH-1234",
    "spotId": 102,
    "floorId": 1,
    "entryTime": "2025-06-15T10:40:00Z",
    "exitTime": "2025-06-15T12:15:00Z",
    "duration": "1h 35m",
    "fee": 50.0,
    "status": "CLOSED"
  }
}
```

**Error (404):**
```json
{
  "status": "error",
  "code": 404,
  "message": "Ticket not found"
}
```

**Idempotent (Already Closed):**
```json
{
  "status": "success",
  "data": {
    "ticketId": "TKT-1718450400-102",
    "status": "CLOSED",
    "message": "Ticket already closed"
  }
}
```

---

## 9. GET `/api/v1/tickets?active=true` — List Active Tickets

**Request:**
```
GET /api/v1/tickets?active=true
```

**Response (200 OK):**
```json
{
  "status": "success",
  "data": [
    {
      "ticketId": "TKT-1718450500-103",
      "vehicleNumber": "MH-12-AB-5678",
      "spotId": 103,
      "floorId": 1,
      "entryTime": "2025-06-15T11:00:00Z",
      "status": "ACTIVE"
    },
    {
      "ticketId": "TKT-1718450600-201",
      "vehicleNumber": "DL-03-CD-9012",
      "spotId": 201,
      "floorId": 2,
      "entryTime": "2025-06-15T11:10:00Z",
      "status": "ACTIVE"
    }
  ]
}
```

---

## 10. GET `/api/v1/events?vehicleNumber=...` — Event History

**Request:**
```
GET /api/v1/events?vehicleNumber=KA-01-HH-1234
```

**Response (200 OK):**
```json
{
  "status": "success",
  "data": [
    {
      "eventId": 1001,
      "ticketId": "TKT-1718450400-102",
      "eventType": "PARKED",
      "occurredAt": "2025-06-15T10:40:00Z"
    },
    {
      "eventId": 1002,
      "ticketId": "TKT-1718450400-102",
      "eventType": "UNPARKED",
      "occurredAt": "2025-06-15T12:15:00Z"
    }
  ]
}
```

---

## 


---

## Summary

All responses follow consistent structure:
- `status`: `"success"` or `"error"`
- `data`: response payload
- `code` + `message` for errors
- HTTP status codes: `200 OK`, `201 Created`, `404 Not Found`, `409 Conflict`
**DB schema (relational)**  
Normalization: spots fixed; tickets point to spot; events append\-only for audit.

```sql
-- vehicles registered (optional pre-registration)
CREATE TABLE vehicles (
  number VARCHAR(32) PRIMARY KEY,
  type VARCHAR(16) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- parking floors
CREATE TABLE parking_floors (
  floor_id INT PRIMARY KEY,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- parking spots
CREATE TABLE parking_spots (
  spot_id INT PRIMARY KEY,
  floor_id INT NOT NULL,
  spot_type VARCHAR(16) NOT NULL,
  is_occupied BOOLEAN NOT NULL DEFAULT FALSE,
  current_vehicle_number VARCHAR(32) NULL,
  CONSTRAINT fk_spot_floor FOREIGN KEY (floor_id) REFERENCES parking_floors(floor_id),
  CONSTRAINT fk_spot_vehicle FOREIGN KEY (current_vehicle_number) REFERENCES vehicles(number)
);

-- tickets (active while not closed)
CREATE TABLE tickets (
  ticket_id VARCHAR(64) PRIMARY KEY,
  vehicle_number VARCHAR(32) NOT NULL,
  spot_id INT NOT NULL,
  floor_id INT NOT NULL,
  entry_time TIMESTAMP NOT NULL,
  exit_time TIMESTAMP NULL,
  status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
  CONSTRAINT fk_ticket_vehicle FOREIGN KEY (vehicle_number) REFERENCES vehicles(number),
  CONSTRAINT fk_ticket_spot FOREIGN KEY (spot_id) REFERENCES parking_spots(spot_id),
  CONSTRAINT fk_ticket_floor FOREIGN KEY (floor_id) REFERENCES parking_floors(floor_id)
);

-- event log for observer notifications / audit
CREATE TABLE parking_events (
  event_id BIGSERIAL PRIMARY KEY,
  ticket_id VARCHAR(64) NOT NULL,
  event_type VARCHAR(16) NOT NULL, -- PARKED / UNPARKED
  occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_event_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id)
);

-- observers registry (optional)
CREATE TABLE observers (
  observer_id BIGSERIAL PRIMARY KEY,
  channel VARCHAR(32) NOT NULL, -- SMS / WHATSAPP
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- indexes for performance
CREATE INDEX idx_spots_floor_type_free ON parking_spots(floor_id, spot_type, is_occupied);
CREATE INDEX idx_tickets_vehicle_active ON tickets(vehicle_number, status);
CREATE INDEX idx_events_ticket ON parking_events(ticket_id);
```

**Key points**
- `is_occupied` + `current_vehicle_number` enable fast allocation queries.
- `tickets.exit_time` set on unpark; status flips to `CLOSED`.
- Event log supports asynchronous notification or replay.
- Floor denormalization in ticket (`floor_id`) avoids join for reporting.
- Consider partitioning `parking_events` if high volume.
  If you make `Vehicle` abstract, here's the recommended structure:

**Abstract Vehicle class:**
<!-- replace lines 5 to 12 -->
```java
public abstract class Vehicle {
    private final String number;
    private final VehicleType type;

    protected Vehicle(String number, VehicleType type) {
        this.number = number;
        this.type = type;
    }
```

**Subclass examples:**

```java
public class Car extends Vehicle {
    public Car(String number) {
        super(number, VehicleType.CAR);
    }
}
```

```java
public class Motorcycle extends Vehicle {
    public Motorcycle(String number) {
        super(number, VehicleType.MOTORCYCLE);
    }
}
```

```java
public class Truck extends Vehicle {
    public Truck(String number) {
        super(number, VehicleType.TRUCK);
    }
}
```

Key changes:
- Constructor is now `protected` (only subclasses can call it)
- Each subclass passes its specific `VehicleType` to the parent constructor
- Subclasses can add their own specific fields/methods if needed