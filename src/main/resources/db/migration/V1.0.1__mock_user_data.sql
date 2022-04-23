-- demo.user data
INSERT INTO Users(name, surname, customer_id)
VALUES ('Chris', 'Johnson', gen_random_uuid ()),
       ('Daniel', 'Rodriguez', gen_random_uuid ()),
       ('Isabella', 'Smith', gen_random_uuid ()),
       ('Penelope', 'Brown', gen_random_uuid ());
