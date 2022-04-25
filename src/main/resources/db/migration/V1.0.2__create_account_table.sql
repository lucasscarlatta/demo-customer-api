-- com.test.demo.account definition
CREATE TABLE Accounts (
                      id            UUID NOT NULL PRIMARY KEY REFERENCES users,
                      name          varchar(255) NOT NULL,
                      balance    DECIMAL NOT NULL
);
