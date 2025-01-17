--СОЗДАНИЕ ТАБЛИЦЫ АВТОРЫ

create table if not exists authors (
--не даст внести значения Id вручную
 author_id int2 primary key generated always as identity,
 surname varchar (30) not null,
 name varchar (30) not null
);

--ВНЕСЕНИЕ ДАННЫХ В ТАБЛИЦУ АВТОРЫ

insert into authors (surname, name)
values 	('Толстой', 'Лев'),
		('Уайльд', 'Оскар'),
		('Купер', 'Джеймс Фенимор'),
		('Толстой', 'Алексей'),
		('Сенкевич', 'Генрих'),
		('Стивенсон', 'Роберт Льюис'),
		('Шекспир', 'Уильям'),
		('Ахматова', 'Анна');

--СОЗДАНИЕ ТАБЛИЦЫ ЖАНРЫ

create table if not exists genres (
 genre_id int2 primary key generated always as identity,
 title varchar (30) not null unique
);

--ВНЕСЕНИЕ ДАННЫХ В ТАБЛИЦУ ЖАНРЫ

insert into genres (title )
values 	('роман'),
		('рассказ'),
		('комедия'),
		('приключения'),
		('поэзия');

--СОЗДАНИЕ ТАБЛИЦЫ КНИГИ СО СВЯЗЯМИ

create table if not exists books (
 book_id int2 primary key generated always as identity,
 title varchar (30) not null,
 --ЕСЛИ УДАЛИТЬ ЗАПИСЬ В ТАБЛИЦЕ authors, ТО ОНА БУДЕТ АВТОМАТИЧЕСКИ УДАЛЕНА В таблице books. ВСЯ СТРОЧКА БУДЕТ УДАЛЕНА
 author_id int2 references authors (author_id) on delete cascade,
 --ЕСЛИ УДАЛИТЬ ЗАПИСЬ В ТАБЛИЦЕ genres, ТО ОНА БУДЕТ АВТОМАТИЧЕСКИ УДАЛЕНА В таблице books. ВМЕСТО ЗАПИСИ БУДЕТ NULL
 genre_id int2 references genres (genre_id) on delete set null
);

--ВНЕСЕНИЕ ДАННЫХ В ТАБЛИЦУ КНИГИ

insert into books (title, author_id, genre_id)
values 	('Воскресение', 1, 1),
		('После бала', 1, 2),
		('Севастопольские рассказы', 1, 2),
		('Портрет Дориана Грея', 2, 1),
		('Как важно быть серьёзным', 2, 3),
		('Зверобой', 3, 4),
		('Последний из могикан', 3, 4),
		('Пётр Первый', 4, 1),
		('Крестоносцы', 5, 1),
		('Остров сокровищ', 6, 4),
		('Сон в летнюю ночь', 7, 3),
		('Чётки', 8, 5);

