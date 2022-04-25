
CREATE TYPE transaction_type AS ENUM ('DEPOSIT', 'WITHDRAW');
-- com.assigment.demo.domain.Transaction definition
CREATE TABLE Transactions (
                            id                  UUID NOT NULL,
                            account_id          UUID NOT NULL,
                            time                TIMESTAMP NOT NULL,
                            transaction_type    transaction_type NOT NULL,
                            amount              DECIMAL NOT NULL,
                            PRIMARY KEY (id),
                            CONSTRAINT fk_account FOREIGN KEY(account_id) REFERENCES accounts(id)
);
