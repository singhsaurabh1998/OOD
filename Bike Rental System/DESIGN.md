# Bike Rental System - Design Documentation

## Architecture Overview

This system follows a **layered architecture** with clear separation of concerns:

```
Main (Client)
    ↓
Facade Layer (RentalSystemFacade)
    ↓
Service Layer (BikeRentalService, InventoryManager)
    ↓
Repository Layer (VehicleRepository, RentalRepository)
    ↓
Model Layer (Vehicle, Bike, Scooter, Customer, Rental)
```

## Design Patterns Used

### 1. **Facade Pattern** 🎭
- **Class**: `RentalSystemFacade`
- **Purpose**: Provides a simplified, unified interface to the complex subsystems
- **Benefits**: 
  - Single entry point for all operations
  - Hides complexity from clients
  - Coordinates between multiple services

### 2. **Factory Pattern** 🏭
- **Class**: `VehicleFactory`
- **Purpose**: Creates vehicle instances (Bike/Scooter) without exposing creation logic
- **Benefits**:
  - Centralized vehicle creation
  - Easy to add new vehicle types
  - Encapsulates instantiation logic

### 3. **Strategy Pattern** 💡
- **Interface**: `PricingStrategy`
- **Implementations**: `HourlyPricingStrategy`
- **Purpose**: Allows dynamic pricing algorithm selection at runtime
- **Benefits**:
  - Easy to add new pricing strategies
  - Pricing logic is interchangeable
  - Follows Open/Closed Principle

### 4. **Repository Pattern** 📦
- **Interfaces**: `VehicleRepository`, `RentalRepository`
- **Implementations**: `InMemoryVehicleRepository`, `InMemoryRentalRepository`
- **Purpose**: Abstracts data access layer
- **Benefits**:
  - Can swap in-memory with database implementation
  - Testability
  - Separation of data access from business logic

## Layer Responsibilities

### 📋 Model Layer (`model` package)
**Responsibility**: Domain entities and business objects

- `Vehicle` - Abstract base class for all vehicles
- `Bike` - Concrete vehicle type with size
- `Scooter` - Concrete vehicle type with fuel type
- `Customer` - Customer entity
- `Rental` - Links customer and vehicle

### 🗄️ Repository Layer (`repository` package)
**Responsibility**: Data access and persistence

- `VehicleRepository` - Interface for vehicle CRUD operations
- `RentalRepository` - Interface for rental CRUD operations
- `InMemoryVehicleRepository` - In-memory implementation
- `InMemoryRentalRepository` - In-memory implementation

### ⚙️ Service Layer (`service` package)
**Responsibility**: Business logic and orchestration

- **`BikeRentalService`**: 
  - Manages rental lifecycle
  - Creates rentals
  - Completes rentals with pricing
  - Queries rental history
  
- **`InventoryManager`**:
  - Manages vehicle availability
  - Thread-safe vehicle allocation
  - Releases vehicles back to inventory

### 🎭 Facade Layer (`facade` package)
**Responsibility**: Unified entry point coordinating all subsystems

- **`RentalSystemFacade`**:
  - Coordinates `InventoryManager` and `BikeRentalService`
  - Simplifies client interaction
  - Handles cross-cutting concerns (logging, validation)

### 🏭 Factory Layer (`factory` package)
**Responsibility**: Object creation

- **`VehicleFactory`**: Creates vehicle instances

### 💡 Strategy Layer (`strategy` package)
**Responsibility**: Pricing algorithms

- **`PricingStrategy`**: Interface for pricing
- **`HourlyPricingStrategy`**: Hourly-based pricing

### 📊 Enums (`enums` package)
**Responsibility**: Type-safe constants

- `VehicleCategory`, `BikeSize`, `FuelType`, `RentalStatus`

## Key Design Decisions

### Why Separate Services?

**Before (Problem):**
- `BikeRentalService` was acting as a facade, mixing coordination and business logic
- Duplicated facade responsibilities between `BikeRentalService` and `RentalSystemFacade`

**After (Solution):**
- **`BikeRentalService`**: Pure business logic (rental management)
- **`InventoryManager`**: Pure business logic (inventory management)
- **`RentalSystemFacade`**: Pure coordination (orchestrates both services)

### Benefits of This Design:

1. **Single Responsibility**: Each class has one reason to change
2. **Open/Closed**: Open for extension, closed for modification
3. **Dependency Inversion**: Depends on abstractions (interfaces), not concretions
4. **Testability**: Each layer can be tested independently
5. **Maintainability**: Clear boundaries make changes easier

## Workflow Example

### Renting a Vehicle:
```
Client → RentalSystemFacade.rentVehicle()
    ↓
    1. InventoryManager.allocateVehicle() → Mark vehicle as rented
    ↓
    2. BikeRentalService.createRental() → Create rental record
    ↓
    Return rental to client
```

### Returning a Vehicle:
```
Client → RentalSystemFacade.returnVehicle()
    ↓
    1. BikeRentalService.completeRental() → Calculate charges, update rental
    ↓
    2. InventoryManager.releaseVehicle() → Mark vehicle as available
    ↓
    Return receipt to client
```

## Thread Safety

- **`InventoryManager`** uses `ReentrantLock` for thread-safe vehicle allocation
- Prevents race conditions when multiple clients try to rent the same vehicle

## Extensibility

### Easy to Add:
- ✅ New vehicle types (e.g., `Car`, `Motorcycle`)
- ✅ New pricing strategies (e.g., `DailyPricingStrategy`, `WeekendPricingStrategy`)
- ✅ New repositories (e.g., `DatabaseVehicleRepository`)
- ✅ Additional services (e.g., `PaymentService`, `NotificationService`)

## SOLID Principles Applied

- **S**ingle Responsibility: Each class has one job
- **O**pen/Closed: Open for extension (new vehicle types, pricing strategies)
- **L**iskov Substitution: Bike/Scooter can substitute Vehicle
- **I**nterface Segregation: Small, focused interfaces
- **D**ependency Inversion: Depends on interfaces, not implementations

