<!DOCTYPE html>
<html>
<head lang="ru">
    <title>Вторая память</title>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <#include "styles/styleAuthentication.css">
</head>
<body class="Log">
<div class="overPage">
    <form enctype="text/plain" action="http://localhost:4567/SecondMemory/signin" method="POST">
        <label Class="EmailLabel" for="email">Почта:</label>
        <input name="email" id="email" Class="Email" type="email" required>
        <label Class="PasswordLabel" for="password">Пароль:</label>
        <input name="password" id="password" Class="Password" type="password" minlength="8" required >
        <button type="submit" class="Submit">Войти</button>
    </form>

    <a href="register.html">Зарегистрироваться</a>
</div>
</body>
</html>
