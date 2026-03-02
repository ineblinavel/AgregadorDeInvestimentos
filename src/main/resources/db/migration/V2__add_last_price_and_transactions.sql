ALTER TABLE stocks_tb ADD COLUMN last_price DOUBLE NULL;

CREATE TABLE IF NOT EXISTS transactions_tb (
    transaction_id BINARY(16) PRIMARY KEY,
    account_id BINARY(16) NOT NULL,
    stock_id VARCHAR(255) NOT NULL,
    quantity BIGINT NOT NULL,
    price DOUBLE NOT NULL,
    type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts_tb(account_id) ON DELETE CASCADE
);
