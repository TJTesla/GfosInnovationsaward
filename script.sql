create table if not exists applicant
(
    id        int auto_increment
        primary key,
    username  varchar(40)  not null,
    password  varchar(100) not null,
    firstname varchar(40)  null,
    lastname  varchar(40)  null,
    gender    int          null,
    constraint applicant_username_uindex
        unique (username)
);

create table if not exists company
(
    id       int auto_increment
        primary key,
    name     varchar(40)  not null,
    password varchar(100) not null,
    email    varchar(40)  null
);

create table if not exists offer
(
    id          int auto_increment
        primary key,
    title       varchar(50) not null,
    description text        null,
    provider    int         null,
    constraint providerFk
        foreign key (provider) references company (id)
);

create table if not exists favorites
(
    applicantId int not null
        primary key,
    offerId     int not null,
    constraint favorites_applicantId_uindex
        unique (applicantId),
    constraint favorites_offerId_uindex
        unique (offerId),
    constraint favorites_offer_fk
        foreign key (offerId) references offer (id),
    constraint favorites_user_fk
        foreign key (applicantId) references applicant (id)
);

create table if not exists status
(
    id          int         not null
        primary key,
    description varchar(40) null,
    constraint status_description_uindex
        unique (description)
);

create table if not exists application
(
    userId     int           not null,
    offerId    int           not null,
    resumePath varchar(100)  null,
    text       text          null,
    status     int default 0 not null comment '0: To be reviewed
1: Currently being reviewed
10: Accepted
11: Declined',
    constraint application_pk
        unique (userId, offerId),
    constraint application_offer_fk
        foreign key (offerId) references offer (id),
    constraint application_status_id_fk
        foreign key (status) references status (id),
    constraint application_user_fk
        foreign key (userId) references applicant (id)
);


