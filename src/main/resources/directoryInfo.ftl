<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Содержимое папки</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <style>
        body {
            margin: 0;
        }

        #header {
            background-color: red;
            padding: 1rem 3rem;
            display: flex;
            justify-content: space-between;
        }

        #logout {
            padding: 1rem 2rem;
            background-color: white;
            color: red;
            font-family: "Montserrat", serif;
            font-optical-sizing: auto;
            font-weight: 500;
            font-style: normal;
            border-radius: 20px;
            border: none;
            cursor: pointer;
        }

        #welcome {
            color: red;
            font-family: "Montserrat", serif;
            font-optical-sizing: auto;
            font-weight: 400;
            font-style: normal;
        }

        h2 {
            color: red;
            font-family: "Montserrat", serif;
            font-optical-sizing: auto;
            font-weight: 400;
            font-style: normal;
        }

        .file {
            width: 80%;
            display: flex;
            justify-content: space-between;
            margin-bottom: 1rem;
            padding: 0rem 1rem;
            border-style: solid;
            border-width: 2px;
            border-radius: 20px;
            border-color: red;
            cursor: pointer;
        }
    </style>
</head>
<body>
<header>
    <div id="header">
        <img src="" alt="Icon">
        <button id="logout">Выйти</button>
    </div>
</header>
<main>
    <div style="width: 80%; margin: 0 auto; margin-top: 3rem;">
        <h1 id="welcome">Добрый день, username!</h1>
        <h3><button type="button" style="color: red" id="скачать" onclick="location.href='/files/upload/${id}/${bucketName}'">назад</button></h3>
        <div style="width: 80%; display: flex; justify-content: space-between;">
            <h2>Имя файла:</h2>
            <h2>Дата Обновления:</h2>
            <h2>Владелец:</h2>
            <h2></h2>
            <h2>Теги:</h2>
            <h2></h2>
            <h2></h2>
            <h2></h2>
        </div>
        <ul style="list-style: none; padding: 0; margin-top: 0;">
            <#list directoryInfo as file>
            <li>
                <div class="file">
                    <h2>${file.get(0)}</h2>
                    <h2>${file.get(1)}</h2>
                    <h2>${file.get(2)}</h2>
                    <h2>${file.get(3)}</h2>
                    <h2><button style="color: red" value="опции" onclick="location.href='/files/delite/${id}/${bucketName}/${file.get(0)}'">опции</button></h2>
                </div>
            </li>
        </#list>
        </ul>
    </div>
</main>
</body>
</html>