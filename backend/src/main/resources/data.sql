TRUNCATE TABLE consumption RESTART IDENTITY CASCADE;
TRUNCATE TABLE metering_points RESTART IDENTITY CASCADE;
TRUNCATE TABLE customers RESTART IDENTITY CASCADE;

INSERT INTO customers (first_name, last_name, username, password)
VALUES
    ('Mari', 'Mets', 'marimets', 'marimets'),
    ('Madis', 'Mets', 'madismets', 'madismets');

INSERT INTO metering_points (customer_id, address)
VALUES
    (1, 'Tartu mnt 123'),
    (1, 'Tallinna mnt 123'),
    (2, 'Pärnu mnt 123');

INSERT INTO consumption (metering_point_id, amount, amount_unit, consumption_time)
VALUES
    (1, 50, 'kWh', NOW() - INTERVAL '1 year'),
    (2, 30, 'kWh', NOW() - INTERVAL '1 year'),
    (2, 35, 'kWh', NOW() - INTERVAL '11 months'),
    (2, 55, 'kWh', NOW() - INTERVAL '10 months'),
    (2, 123, 'kWh', NOW() - INTERVAL '9 months'),
    (3, 70, 'kWh', NOW() - INTERVAL '10 months'),
    (3, 90, 'kWh', NOW() - INTERVAL '7 months');
