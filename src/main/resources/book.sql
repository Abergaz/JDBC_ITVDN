create table Books
(
    bookId MEDIUMINT   not null auto_increment,
    name   VARCHAR(30) not null,
    price  DOUBLE,
    primary key (bookId)
);
INSERT INTO Books (name, price)
VALUES ('Inferno', 45.0);
INSERT INTO Books (name, price)
VALUES ('Harry Potter', 45.5);
INSERT INTO Books (name, price)
VALUES ('Spartacus', 55.0);
INSERT INTO Books (name, price)
VALUES ('Green mile', 20.6);
INSERT INTO Books (name, price)
VALUES ('Solomon key', 5.0);