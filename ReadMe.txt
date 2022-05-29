Добавленна одна новая функция.
Пополнение счёта на аккаунте.
Пример запроса:
curl -X POST 'http://localhost:8080/account/setMoney'
-H 'Content-Type: application/json'
-d '{"money": 500}'