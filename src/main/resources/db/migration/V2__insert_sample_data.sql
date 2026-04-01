INSERT INTO orders (customer_name, customer_email, product, quantity, price, status, created_at, updated_at) 
VALUES 
('Alice Smith', 'alice@example.com', 'Laptop', 1, 1200.00, 'CONFIRMED', NOW(), NOW()),
('Bob Jones', 'bob@example.com', 'Mouse', 2, 25.50, 'PLACED', NOW(), NOW()),
('Charlie Brown', 'charlie@example.com', 'Keyboard', 1, 85.00, 'SHIPPED', NOW(), NOW());
