CREATE TABLE IF NOT EXISTS roles_tb (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL
);

INSERT IGNORE INTO roles_tb(role_id, role_name) VALUES (1, 'ADMIN');
INSERT IGNORE INTO roles_tb(role_id, role_name) VALUES (2, 'BASIC');

CREATE TABLE IF NOT EXISTS users_tb (
    user_id BINARY(16) PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL,
    active_account_id BINARY(16) NULL,
    version BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BINARY(16) NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users_tb(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles_tb(role_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS accounts_tb (
    account_id BINARY(16) PRIMARY KEY,
    description VARCHAR(255) NULL,
    user_id BINARY(16) NULL,
    FOREIGN KEY (user_id) REFERENCES users_tb(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS billing_address_tb (
    account_id BINARY(16) PRIMARY KEY,
    street VARCHAR(255) NULL,
    number BIGINT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts_tb(account_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS stocks_tb (
    stock_id VARCHAR(255) PRIMARY KEY,
    description VARCHAR(255) NULL,
    currency VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS account_stock_tb (
    account_id BINARY(16) NOT NULL,
    stock_id VARCHAR(255) NOT NULL,
    quantity BIGINT NULL,
    PRIMARY KEY (account_id, stock_id),
    FOREIGN KEY (account_id) REFERENCES accounts_tb(account_id) ON DELETE CASCADE,
    FOREIGN KEY (stock_id) REFERENCES stocks_tb(stock_id) ON DELETE CASCADE
);
