CREATE TABLE event
(
    Id BIGINT PRIMARY KEY,
    occurred_event VARCHAR(64) NOT NULL,
    event_date  TIMESTAMP NOT NULL,
    water_fill_level INTEGER,
    coffee_fill_level INTEGER
);

INSERT INTO event (Id, occurred_event, event_date, water_fill_level, coffee_fill_level) values (123412, 'Power on','2021-12-01 9:00', 1000, 1000);
