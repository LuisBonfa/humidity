
create table model(
    id uuid primary key default uuid_generate_v4(),
    model varchar(64) not null unique,
    status varchar(30) not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

create table area(
    id uuid primary key default uuid_generate_v4(),
    user_id uuid not null references "user"(id),
    name varchar(64) not null,
    description varchar(256) not null,
    status varchar(30) not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    unique(user_id, name)
);

create table device(
    id uuid primary key default uuid_generate_v4(),
    user_id uuid not null references "user"(id),
    model_id uuid not null references "model"(id),
    area_id uuid references "area"(id),
    name varchar(64) not null,
    status varchar(30) not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

create table humidity(
    id uuid primary key default uuid_generate_v4(),
    area_id uuid not null references "area"(id),
    device_id uuid not null references "device"(id),
    value integer not null,
    description varchar(64) not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()

);

insert into version (file_name, version, create_date)
values ('2019-08-30-create-tables.sql', '1.0.0', now());
