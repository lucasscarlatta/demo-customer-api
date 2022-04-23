-- demo.user definition
CREATE TABLE Users (
                      id            UUID DEFAULT gen_random_uuid (),
                      name          varchar(255) NOT NULL,
                      surname       varchar(255) NOT NULL,
                      customer_id    UUID NOT NULL,
                      PRIMARY KEY (id),
                      UNIQUE (customer_id)
);
