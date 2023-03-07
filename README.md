# *Java-Filmorate*
---
Описание проекта
-
Приложение представляет собой социальную сеть для подбора фильма по предпочтениям и рекомендациям друзей.


1. Функционал приложения:

- Добавлять\редактировать\искать и удалять пользователей и фильмы
- Оценивать фильмы
- Добавлять\удалять друзей
- Просматривать общих друзей
- Просматривать список самых популярных фильмов

2. ER диаграмма базы данных приложения: 
https://dbdiagram.io/d/64065de2296d97641d85e022
![ER_diagram](docs/ER-diagram.png) 


#### Таблица users:
- ***user_id*** - уникальный идентифиакатор пользователя  
- ***email*** - электронная почта пользователя  
- ***login*** - логин пользователя  
- ***name*** - имя  пользователя  
- ***birthday*** - дата рождения пользователя 

#### Таблица films:
- ***film_id*** - уникальный индентифиатор фильма  
- ***name*** - название фильма  
- ***description*** - описание фильма  
- ***release_date*** - дата выхода фильма  
- ***duration*** - продолжительность фильма  
- ***mpa_id*** - идетификатор рейтинга фильма 

#### Таблица friends:
- ***user_id*** - целочисленный идентификатор пользователя
- ***friend_id*** - целочисленный идентификатор пользователя
- ***friendship_status*** - статус дружбы между двумя пользователями

#### Таблица mpa:
- ***mpa_id*** - уникальный идентификатор рейтинга  
- ***name*** - значение рейтинга 

#### Таблица film_audience:
- ***user_id*** - идентификатор пользователя 
- ***film_id*** - идентификатор фильма  

#### Таблица film_genre:
- ***film_id*** - идентификатор фильма  
- ***genre_id*** - идентификатор жанра 

#### Таблица genres:
- ***genre_id*** - идентификатор жанра
- ***name*** - значение рейтинга


### Примеры запросов для некоторых операций:

#### 1. Создание пользователя:
```
INSERT INTO users (email, login, name, birthday)
VALUES ( ?, ?, ?, ? );
```

#### 2. Получение списка всех фильмов/пользователей:
```
SELECT *
FROM films/users;
```

#### 3. Получение информации о пользователе по его id
```
SELECT *
FROM users
WHERE user_id = ?
```

#### 4. Добавление в друзья
```
INSERT INTO friends(user_id, friend_id) VALUES (?, ?)
```

#### 5. Удаление из друзей
```
DELETE
FROM friends
WHERE user_id = ? AND friend_id = ?
```