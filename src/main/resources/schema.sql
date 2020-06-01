DROP TABLE IF EXISTS sagas;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS order_items;

CREATE TABLE sagas
(
    id              BIGSERIAL,
    customer_id     BIGSERIAL,
    order_id        BIGSERIAL,
    current_state   VARCHAR(255),
    payment_result  VARCHAR(255),
    stock_result    VARCHAR(255),
    delivery_result VARCHAR(255),
    CONSTRAINT PK_sagas PRIMARY KEY (id)
);

CREATE TABLE orders
(
    order_entity_id BIGSERIAL,
    customer_id     BIGSERIAL,
    order_id        BIGSERIAL,
    location        VARCHAR(255),
    total           DOUBLE PRECISION,
    status          VARCHAR(255),
    CONSTRAINT PK_orders PRIMARY KEY (order_entity_id)
);

CREATE TABLE order_items
(
    item_id   BIGSERIAL,
    item_name VARCHAR(255),
    amount    INT,
    CONSTRAINT PK_order_items PRIMARY KEY (item_id)
);
