<!DOCTYPE html>
<html>
<head lang="ru">
    <title>Вторая память</title>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <#include "styles/styleRegister.css">
</head>
<body>

<main>
    <form enctype="text/plain" action="http://localhost:4567/SecondMemory/signup" method="POST">
        <div class="container">
            <div class="A">
                <p class="Welcome">Давайте знакомиться!</p>
            </div>
            <div class="B">
                <label for="username">Как вас зовут?</label>
                <input name="username" id="username" type="text" required>
                <p class="nameComment">Чтобы мы знали, как к вам обращаться.</p>
            </div>
            <div class="C">
                <label for="email">Ваша почта:</label>
                <input name="email" id="email" type="email" required>
                <p class="emailComment">Чтобы мы могли уведомить вас о жизни<br>ваших файлов.</p>
            </div>
            <div class="D">
                <label for="password">Придумайте пароль:</label>
                <input name="password" id="password" type="password" pattern="(?=.*[A-Z])(?=.*[@_!#$%^*?&\(\)\/\|\}\{~:\<]).{8,}" required title="Пароль должен соответствовать формату.">
                <p class="passwordComment">Пароль должен состоять не менее чем из 8<br>символов, содержать хотя бы одну заглавную букву<br>и специальный символ (@_!#$%^&*()<>?/|}{~:).<br>Все для вашей безопасности)</p>
            </div>
            <div class="E">
                <label for="repeatPassword">Повторите пароль:</label>
                <input name="repeatPassword" id="repeatPassword" type="password" pattern="(?=.*[A-Z])(?=.*[@_!#$%^*?&\(\)\/\|\}\{~:\<]).{8,}" required title="Пароль должен соответствовать формату.">
                <p class="repeatPasswordComment">Чтобы точно его запомнить.</p>
            </div>
            <div class="F">
                <button type="submit">Зарегистрироваться</button>
            </div>
        </div>
    </form>
</main>
</body>
</html>
