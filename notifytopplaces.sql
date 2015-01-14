create table if not exists places (
  id integer primary key autoincrement,
  name text,
  typeid integer,
  location text,
  comment text,
  refercomment text,
  openninghours text,
  averageprice text,
  address text,
  cityid integer, 
  hasnotify integer,
  notifycounter text,
  foreign key(typeid) REFERENCES types(id),
  foreign key(cityid) references cities(id)
);

create table if not exists types (
  id integer primary key autoincrement,
  name text
);

create table if not exists cities (
  id integer primary key autoincrement,
  city text,
  provinceid integer,
  foreign key (provinceid) references provinces(id)
);

create table if not exists provinces (
  id integer primary key autoincrement,
  name text  
);

insert into provinces (name) values ('SP');

insert into cities (city, provinceid) values (
  'São Paulo', 1 
);

insert into types (name) values ('Supermercados');

insert into places (name, typeid, location, 
                    comment, refercomment, 
                    openninghours,
                    averageprice,
                    address, cityid, hasnotify, notifycounter) values 
('Abevê Supermercados', 1, '-1,-1',
'Comentário sobre indicação de fontes confiáveis','fonte do comentário',
'8:00,20:00 or 24',
'6,00',
'Rua tal de tals, 73 - Vila dos tals', 1, 0, '');

