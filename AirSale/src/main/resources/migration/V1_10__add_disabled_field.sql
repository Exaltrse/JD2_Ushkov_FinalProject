alter table airline
    add disabled boolean default false not null;

alter table airline_plane
    add disabled boolean default false not null;

alter table airport
    add disabled boolean default false not null;

alter table country
    add disabled boolean default false not null;

alter table current_flight
    add disabled boolean default false not null;

alter table current_flight_status
    add disabled boolean default false not null;

alter table discount
    add disabled boolean default false not null;

alter table flight
    add disabled boolean default false not null;

alter table flight_plane
    add disabled boolean default false not null;

alter table passenger
    add disabled boolean default false not null;

alter table passenger_class
    add disabled boolean default false not null;

alter table passenger_passport
    add disabled boolean default false not null;

alter table passport
    add disabled boolean default false not null;

alter table plane
    add disabled boolean default false not null;

alter table plane_seats
    add disabled boolean default false not null;

alter table roles
    add disabled boolean default false not null;

alter table seat_class
    add disabled boolean default false not null;

alter table ticket
    add disabled boolean default false not null;

alter table ticket_status
    add disabled boolean default false not null;

alter table "user"
    add disabled boolean default false not null;
