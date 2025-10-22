# BookMyShow Database Schema & Management

## Core Tables

### 1. **users**
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_phone (phone)
);
```

### 2. **cities**
```sql
CREATE TABLE cities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    state VARCHAR(100),
    country VARCHAR(100) NOT NULL,
    timezone VARCHAR(50),
    UNIQUE KEY unique_city (name, state, country)
);
```

### 3. **theatres**
```sql
CREATE TABLE theatres (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    city_id INT NOT NULL,
    address TEXT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (city_id) REFERENCES cities(id),
    INDEX idx_city (city_id)
);
```

### 4. **screens**
```sql
CREATE TABLE screens (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    theatre_id BIGINT NOT NULL,
    screen_name VARCHAR(100) NOT NULL,
    total_seats INT NOT NULL,
    screen_type VARCHAR(50) DEFAULT 'REGULAR', -- IMAX, 4DX, REGULAR
    FOREIGN KEY (theatre_id) REFERENCES theatres(id) ON DELETE CASCADE,
    INDEX idx_theatre (theatre_id)
);
```

### 5. **seats**
```sql
CREATE TABLE seats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    screen_id BIGINT NOT NULL,
    row_number INT NOT NULL,
    seat_number INT NOT NULL,
    seat_type VARCHAR(20) NOT NULL, -- REGULAR, PREMIUM, VIP
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (screen_id) REFERENCES screens(id) ON DELETE CASCADE,
    UNIQUE KEY unique_seat (screen_id, row_number, seat_number),
    INDEX idx_screen (screen_id)
);
```

### 6. **movies**
```sql
CREATE TABLE movies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    language VARCHAR(50) NOT NULL,
    duration_minutes INT NOT NULL,
    release_date DATE,
    description TEXT,
    poster_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_language (language),
    INDEX idx_release_date (release_date)
);
```

### 7. **movie_genres**
```sql
CREATE TABLE movie_genres (
    movie_id BIGINT NOT NULL,
    genre VARCHAR(50) NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    PRIMARY KEY (movie_id, genre)
);
```

### 8. **shows**
```sql
CREATE TABLE shows (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    movie_id BIGINT NOT NULL,
    screen_id BIGINT NOT NULL,
    show_time TIMESTAMP NOT NULL,
    base_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'SCHEDULED', -- SCHEDULED, CANCELLED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (screen_id) REFERENCES screens(id),
    INDEX idx_movie (movie_id),
    INDEX idx_screen (screen_id),
    INDEX idx_show_time (show_time),
    INDEX idx_composite (screen_id, show_time, status)
) PARTITION BY RANGE (UNIX_TIMESTAMP(show_time)) (
    PARTITION p_past VALUES LESS THAN (UNIX_TIMESTAMP('2025-01-01')),
    PARTITION p_2025_q1 VALUES LESS THAN (UNIX_TIMESTAMP('2025-04-01')),
    PARTITION p_2025_q2 VALUES LESS THAN (UNIX_TIMESTAMP('2025-07-01')),
    PARTITION p_future VALUES LESS THAN MAXVALUE
);
```

### 9. **bookings**
```sql
CREATE TABLE bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_ref VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    show_id BIGINT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL, -- BOOKED, CANCELLED, PAYMENT_FAILED
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_id VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (show_id) REFERENCES shows(id),
    INDEX idx_user (user_id),
    INDEX idx_show (show_id),
    INDEX idx_booking_time (booking_time),
    INDEX idx_status (status)
) PARTITION BY RANGE (UNIX_TIMESTAMP(booking_time)) (
    PARTITION p_2024 VALUES LESS THAN (UNIX_TIMESTAMP('2025-01-01')),
    PARTITION p_2025_q1 VALUES LESS THAN (UNIX_TIMESTAMP('2025-04-01')),
    PARTITION p_2025_q2 VALUES LESS THAN (UNIX_TIMESTAMP('2025-07-01')),
    PARTITION p_future VALUES LESS THAN MAXVALUE
);
```

### 10. **booking_seats**
```sql
CREATE TABLE booking_seats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    FOREIGN KEY (seat_id) REFERENCES seats(id),
    UNIQUE KEY unique_booking_seat (booking_id, seat_id),
    INDEX idx_booking (booking_id),
    INDEX idx_seat (seat_id)
);
```

### 11. **seat_locks** (Concurrency Control)
```sql
CREATE TABLE seat_locks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    show_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    locked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    FOREIGN KEY (show_id) REFERENCES shows(id),
    FOREIGN KEY (seat_id) REFERENCES seats(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY unique_lock (show_id, seat_id),
    INDEX idx_expires (expires_at),
    INDEX idx_user_lock (user_id, show_id)
);
```

---

## Schema Management Strategy

### Migration Tool
Use **Flyway** or **Liquibase** for versioned migrations:
- Store migrations in `db/migration/V{version}__{description}.sql`
- Example: `V001__init_schema.sql`, `V002__add_seat_locks.sql`

### Naming Conventions
- Tables: plural, lowercase with underscores (`booking_seats`)
- Columns: snake_case (`seat_type`, `booking_time`)
- Indexes: `idx_{column}` or `idx_composite_{purpose}`
- Foreign keys: `fk_{table}_{ref_table}`

### Concurrency Control Strategy

#### 1. **Seat Locking (Pessimistic)**
- When user selects seats, insert into `seat_locks` with 10-minute expiry
- Background job cleans expired locks every minute
- Use `SELECT ... FOR UPDATE` when checking/creating locks

```sql
-- Lock seats for booking
INSERT INTO seat_locks (show_id, seat_id, user_id, expires_at)
VALUES (?, ?, ?, NOW() + INTERVAL 10 MINUTE)
ON DUPLICATE KEY UPDATE
    user_id = IF(expires_at < NOW(), VALUES(user_id), user_id),
    expires_at = IF(expires_at < NOW(), VALUES(expires_at), expires_at);
```

#### 2. **Booking Creation (Optimistic)**
- Add `version` column to critical tables for optimistic locking
- Use `WHERE version = ?` in UPDATE statements

```sql
ALTER TABLE bookings ADD COLUMN version INT DEFAULT 1;

UPDATE bookings 
SET status = 'CONFIRMED', version = version + 1 
WHERE id = ? AND version = ? AND status = 'PENDING';
```

#### 3. **Idempotency**
- Use `booking_ref` (unique) to prevent duplicate bookings
- Payment webhook uses `payment_id` for idempotent processing

---

## Indexing Strategy

### Query Patterns
1. **Find shows by movie, city, date**
    - `idx_composite` on `shows(movie_id, show_time)`
    - `idx_city` on `theatres(city_id)`

2. **Get available seats**
    - `idx_screen` on `seats(screen_id)`
    - `idx_show_seat` on `booking_seats(show_id, seat_id)` – check booked seats

3. **User booking history**
    - `idx_user` on `bookings(user_id, booking_time DESC)`

4. **Lock expiry cleanup**
    - `idx_expires` on `seat_locks(expires_at)`

---

## Partitioning

### Time-Based Partitioning
- Partition `shows` and `bookings` by date (quarterly)
- Archive old partitions to cold storage after 6 months
- Keep hot partitions (current + next 3 months) for fast queries

### Commands
```sql
-- Add new partition quarterly
ALTER TABLE shows ADD PARTITION (
    PARTITION p_2025_q3 VALUES LESS THAN (UNIX_TIMESTAMP('2025-10-01'))
);

-- Drop old partition
ALTER TABLE shows DROP PARTITION p_past;
```

---

## Background Jobs

### 1. **Expired Lock Cleanup**
```sql
DELETE FROM seat_locks WHERE expires_at < NOW();
```
Run every 1 minute via cron or scheduler.

### 2. **Booking Timeout**
```sql
UPDATE bookings 
SET status = 'PAYMENT_FAILED' 
WHERE status = 'PENDING' 
  AND booking_time < NOW() - INTERVAL 15 MINUTE;
```

### 3. **Archive Old Data**
Move bookings older than 1 year to archive table or S3.

---

## Migration Rollback

For destructive changes:
- Create shadow table, backfill, swap atomically
- Example: renaming column

```sql
-- V005__rename_column.sql
ALTER TABLE users ADD COLUMN full_name VARCHAR(255);
UPDATE users SET full_name = name;
ALTER TABLE users DROP COLUMN name;
```

Rollback script:
```sql
-- U005__rollback_rename.sql (Flyway undo)
ALTER TABLE users ADD COLUMN name VARCHAR(255);
UPDATE users SET name = full_name;
ALTER TABLE users DROP COLUMN full_name;
```

---

## Security

- Use least privilege roles: `app_read`, `app_write`, `migration`
- Encrypt PII columns (`email`, `phone`) at application layer
- Row-level security (RLS) for multi-tenant if needed
- Mask sensitive data in logs

---

## Monitoring

- Enable `slow_query_log` (queries > 1s)
- Track index usage: `SHOW INDEX FROM table_name;`
- Monitor lock wait times: `SHOW ENGINE INNODB STATUS;`
- Alert on high `seat_locks` table size (potential lock leak)

---

This schema balances normalization, performance, and safe concurrent access. Partitioning handles scale, while seat locks prevent double bookings without heavy row-level locking on every seat.

# REST API Design for BookMyShow

Based on the domain models and services in your design, here's a comprehensive REST API structure:

---

## 1. **Authentication & User APIs**

### User Management
```
POST   /api/v1/auth/register          - Register new user
POST   /api/v1/auth/login             - Login user (returns JWT)
POST   /api/v1/auth/logout            - Logout user
GET    /api/v1/users/{userId}         - Get user profile
PUT    /api/v1/users/{userId}         - Update user profile
GET    /api/v1/users/{userId}/bookings - Get user's booking history
```

---

## 2. **Movie APIs**

```
GET    /api/v1/movies                 - List all movies (with filters: language, genre)
GET    /api/v1/movies/{movieId}       - Get movie details
GET    /api/v1/movies/search?q={query} - Search movies by title
GET    /api/v1/movies?city={cityId}&date={date} - Get movies by city and date
```

**Query Parameters:**
- `language` - Filter by language
- `genre` - Filter by genre
- `city` - Filter by city
- `date` - Filter by release/show date

---

## 3. **Theatre & Screen APIs**

```
GET    /api/v1/cities                 - List all cities
GET    /api/v1/theatres?cityId={cityId} - Get theatres by city
GET    /api/v1/theatres/{theatreId}   - Get theatre details
GET    /api/v1/theatres/{theatreId}/screens - Get all screens in a theatre
GET    /api/v1/screens/{screenId}     - Get screen details
```

---

## 4. **Show APIs**

```
GET    /api/v1/shows?movieId={movieId}&cityId={cityId}&date={date}
       - Get shows for a movie in a city on a date
       
GET    /api/v1/shows/{showId}         - Get show details
GET    /api/v1/shows/{showId}/seats   - Get seat layout and availability
GET    /api/v1/theatres/{theatreId}/shows?date={date}
       - Get all shows in a theatre on a date
```

**Response Example:**
```json
{
  "showId": "S001",
  "movieId": "M001",
  "theatreId": "T001",
  "screenId": "SC001",
  "showTime": "2025-01-20T19:00:00Z",
  "basePrice": 250.00,
  "status": "SCHEDULED"
}
```

---

## 5. **Seat Selection & Locking APIs**

```
POST   /api/v1/shows/{showId}/seats/lock - Lock selected seats temporarily
DELETE /api/v1/shows/{showId}/seats/unlock - Unlock seats (manual unlock)
```

**Lock Request Body:**
```json
{
  "userId": "U001",
  "seatIds": [1, 2, 3]
}
```

**Lock Response:**
```json
{
  "lockId": "LOCK12345",
  "expiresAt": "2025-01-20T10:15:00Z",
  "seats": [1, 2, 3]
}
```

---

## 6. **Booking APIs**

```
POST   /api/v1/bookings               - Create a booking (confirm after lock)
GET    /api/v1/bookings/{bookingId}   - Get booking details
PUT    /api/v1/bookings/{bookingId}/cancel - Cancel a booking
GET    /api/v1/bookings?userId={userId}&status={status} 
       - List user bookings (filter by status)
```

**Create Booking Request:**
```json
{
  "userId": "U001",
  "showId": "S001",
  "seatIds": [1, 2, 3],
  "paymentId": "PAY123"
}
```

**Create Booking Response:**
```json
{
  "bookingId": "BKG001",
  "bookingRef": "BMS12345",
  "userId": "U001",
  "showId": "S001",
  "seats": [
    {"seatId": 1, "row": 5, "number": 10, "type": "PREMIUM", "price": 300},
    {"seatId": 2, "row": 5, "number": 11, "type": "PREMIUM", "price": 300}
  ],
  "totalAmount": 600,
  "status": "BOOKED",
  "bookingTime": "2025-01-20T10:10:00Z"
}
```

---

## 7. **Payment APIs**

```
POST   /api/v1/payments/initiate      - Initiate payment
POST   /api/v1/payments/callback      - Payment gateway callback (webhook)
GET    /api/v1/payments/{paymentId}   - Get payment status
```

**Payment Initiate Request:**
```json
{
  "bookingId": "BKG001",
  "amount": 600,
  "paymentMethod": "UPI"
}
```

**Payment Response:**
```json
{
  "paymentId": "PAY123",
  "status": "SUCCESS",
  "transactionId": "TXN987654",
  "amount": 600,
  "paymentTime": "2025-01-20T10:12:00Z"
}
```

---

## 8. **Admin APIs** (Theatre Owners/Admins)

```
POST   /api/v1/admin/theatres         - Add a new theatre
POST   /api/v1/admin/theatres/{theatreId}/screens - Add screen to theatre
POST   /api/v1/admin/shows            - Create a new show
PUT    /api/v1/admin/shows/{showId}   - Update show (cancel, reschedule)
DELETE /api/v1/admin/shows/{showId}   - Delete show
```

---

## 9. **Search & Discovery APIs**

```
GET    /api/v1/search?q={query}&type={movie|theatre}&city={cityId}
       - Unified search across movies and theatres
       
GET    /api/v1/trending/movies        - Get trending movies
GET    /api/v1/recommendations?userId={userId} - Personalized recommendations
```

---

## 10. **Notification APIs** (Internal/Webhook)

```
POST   /api/v1/notifications/email    - Trigger email notification
POST   /api/v1/notifications/sms      - Trigger SMS notification
```

These are typically **internal APIs** called by `NotificationService` via event listeners.

---

## 🔐 **Security & Middleware**

### Headers
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
X-Request-ID: <unique-request-id> (for tracing)
```

### Common Middleware
- **Authentication Filter** - Verify JWT
- **Rate Limiter** - Prevent abuse
- **CORS** - Allow frontend origins
- **Request Logger** - Log all API calls

---

## 📊 **Pagination & Filtering**

For list endpoints, support:
```
?page=1&size=20&sort=showTime,desc
```

**Example Response:**
```json
{
  "data": [...],
  "pagination": {
    "currentPage": 1,
    "totalPages": 5,
    "totalItems": 100,
    "pageSize": 20
  }
}
```

---

## ⚠️ **Error Response Format**

```json
{
  "error": {
    "code": "SEAT_ALREADY_BOOKED",
    "message": "The selected seat is already booked.",
    "timestamp": "2025-01-20T10:10:00Z",
    "path": "/api/v1/bookings"
  }
}
```

---

## 🔄 **Idempotency**

For critical operations (booking, payment):
- Use `Idempotency-Key` header
- Prevent duplicate bookings on retry

```
POST /api/v1/bookings
Idempotency-Key: unique-client-generated-key
```

---

## 🎯 **Key Design Principles**

✅ **RESTful** - Use HTTP methods correctly (GET, POST, PUT, DELETE)  
✅ **Versioned** - `/api/v1/` for backward compatibility  
✅ **Stateless** - Each request is independent  
✅ **Consistent** - Use same patterns across endpoints  
✅ **Documented** - Use Swagger/OpenAPI for API docs

---

Would you like:
1. **OpenAPI/Swagger spec** for these endpoints?
2. **Spring Boot controller examples** implementing these APIs?
3. **Postman collection** for testing?