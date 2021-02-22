create table DP_USER (
                         id bigint not null auto_increment,
                         activationCode varchar(255),
                         active bit not null,
                         email varchar(255),
                         password varchar(255),
                         username varchar(255),
                         primary key (id)
);

create table DP_USER_ROLE (
                              user_id bigint not null,
                              roles varchar(255)
);
create table DP_MESSAGE (
                            id bigint not null auto_increment,
                            chatType varchar(255),
                            filename varchar(255),
                            tag varchar(255),
                            text varchar(255),
                            user_id bigint,
                            primary key (id)
);

create table DP_MESSAGE_LIKES (
                                  message_id bigint not null,
                                  user_id bigint not null,
                                  primary key (message_id, user_id)
);

create table DP_STUDENT (
                            id bigint not null auto_increment,
                            group_name varchar(255),
                            team_id bigint,
                            user_id bigint,
                            primary key (id)
);

alter table DP_STUDENT modify group_name varchar(255) not null;
alter table DP_STUDENT alter column group_name set default '';
create table DP_TEAM (
                         id bigint not null auto_increment,
                         createdDate tinyblob,
                         name varchar(255),
                         LEADER_ID bigint not null,
                         primary key (id)
);

alter table DP_TEAM
    add constraint FK_TEAM_LEADER_ID
        foreign key (LEADER_ID)
            references DP_USER (id);

alter table DP_USER
    add fullName varchar(255) not null;

create table DP_USER_SUBSCRIPTIONS (
                                       channel_id bigint not null,
                                       subscriber_id bigint not null,
                                       primary key (channel_id, subscriber_id)
);

create table DP_MARK (
                         id bigint not null auto_increment,
                         mark varchar(255),
                         createdDate tinyblob,
                         STUDENT_ID bigint not null,
                         primary key (id)
);
alter table DP_MARK modify mark bit not null;

alter table DP_MARK
    add constraint FK_MARK_USER_ID
        foreign key (STUDENT_ID)
            references DP_STUDENT (id);

alter table DP_MESSAGE
    add constraint FK_MESSAGE_USER_ID
        foreign key (user_id)
            references DP_USER (id);

alter table DP_MESSAGE_LIKES
    add constraint FK_MESSAGE_LIKES_USER_ID
        foreign key (user_id)
            references DP_USER (id);

alter table DP_MESSAGE_LIKES
    add constraint FK_LIKE_MESSAGE_ID
        foreign key (message_id)
            references DP_MESSAGE (id);

alter table DP_STUDENT
    add constraint FK_STUDENT_ID
        foreign key (team_id)
            references DP_TEAM (id);

alter table DP_STUDENT
    add constraint FK_STUDENT_USER_ID
        foreign key (user_id)
            references DP_USER (id);

alter table DP_USER_ROLE
    add constraint FK_ROLE_USER_ID
        foreign key (user_id)
            references DP_USER (id);

alter table DP_USER_SUBSCRIPTIONS
    add constraint FK_SUBSCRIPTION_USER_ID
        foreign key (subscriber_id)
            references DP_USER (id);

alter table DP_USER_SUBSCRIPTIONS
    add constraint FK_CHANNEL_USER_ID
        foreign key (channel_id)
            references DP_USER (id);
