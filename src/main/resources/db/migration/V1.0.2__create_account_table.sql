-- com.test.demo.account definition
CREATE TABLE Accounts (
                      id            UUID NOT NULL,
                      customer_id   UUID NOT NULL,
                      name          varchar(255) NOT NULL,
                      balance       DECIMAL NOT NULL,
                      PRIMARY KEY (id),
                      CONSTRAINT fk_user FOREIGN KEY(customer_id) REFERENCES users(id)
);
