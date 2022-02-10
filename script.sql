create table applicant
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

create table category
(
    id   int auto_increment
        primary key,
    term varchar(20) not null,
    constraint category_id_uindex
        unique (id),
    constraint category_term_uindex
        unique (term)
);

create table company
(
    id          int auto_increment
        primary key,
    name        varchar(40)  not null,
    password    varchar(100) not null,
    email       varchar(40)  not null,
    phoneno     varchar(20)  null,
    website     varchar(40)  null,
    description text         null
);

create table resumes
(
    id   int auto_increment
        primary key,
    path varchar(100) not null,
    name varchar(40)  not null,
    user int          not null,
    constraint resume_id_uindex
        unique (id),
    constraint resume_path_uindex
        unique (path),
    constraint resumes_user_fk
        foreign key (user) references applicant (id)
);

create table status
(
    id          int         not null
        primary key,
    description varchar(40) null,
    constraint status_description_uindex
        unique (description)
);

create table tags
(
    id  int auto_increment
        primary key,
    tag varchar(40) not null,
    constraint tags_id_uindex
        unique (id),
    constraint tags_tag_uindex
        unique (tag)
);

create table offer
(
    id          int auto_increment
        primary key,
    title       varchar(50) not null,
    description text        null,
    provider    int         null,
    tag         int         null,
    category    int         null,
    constraint offer_cat_fk
        foreign key (category) references category (id),
    constraint offer_tag_fk
        foreign key (tag) references tags (id),
    constraint providerFk
        foreign key (provider) references company (id)
);

create table application
(
    userId   int           not null,
    offerId  int           not null,
    text     text          null,
    status   int default 0 not null comment '0: To be reviewed
1: Currently being reviewed
10: Accepted
11: Declined',
    resumeId int           null,
    constraint application_pk
        unique (userId, offerId),
    constraint application_cv_fk
        foreign key (resumeId) references resumes (id),
    constraint application_offer_fk
        foreign key (offerId) references offer (id),
    constraint application_status_id_fk
        foreign key (status) references status (id),
    constraint application_user_fk
        foreign key (userId) references applicant (id)
);

create table favorites
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

create table title
(
    id   int auto_increment
        primary key,
    term varchar(20) not null,
    constraint title_id_uindex
        unique (id),
    constraint title_term_uindex
        unique (term)
);

create table titleRelation
(
    applicantId int not null,
    titleId     int not null,
    primary key (applicantId, titleId),
    constraint titleRelation_applicant_fk
        foreign key (applicantId) references applicant (id),
    constraint titleRelation_title_fk
        foreign key (titleId) references title (id)
);


