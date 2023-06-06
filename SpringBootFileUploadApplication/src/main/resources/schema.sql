CREATE TABLE account_details
(
    id                              INTEGER GENERATED BY DEFAULT AS IDENTITY,
    account_number                  VARCHAR(255),
    first_name                      VARCHAR(255),
    last_name                       VARCHAR(255),
    account_type                    VARCHAR(255),
    email_id                        VARCHAR(255),
    person_address                  VARCHAR(255),
    zip_code                        VARCHAR(255),
    PRIMARY KEY (id)
);