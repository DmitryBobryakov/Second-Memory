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
        .sidebar{
            background: red;
            position: fixed;
            top: 0;
            left: 0;
            width: 225px;
            height: 100%;
            padding: 20px 0;
            transition: all 0.5s ease;
        }

        #close {
            padding: 1rem 1rem;
            margin-left: 10%;
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

        .item {
            margin-bottom: 1rem;
            padding: 0rem 1rem;
            background-color: white;
            color: red;
            border-radius: 20px;
            cursor: pointer;
            font-family: "Montserrat", serif;
            font-optical-sizing: auto;
            font-weight: 400;
            font-style: normal;
        }

        li {
            width: 60%;
            margin-left: 10%;
        }
    </style>
</head>
<body>
    <div class="sidebar">
        <button id="close">Закрыть</button>
        <ul style="list-style: none; padding: 0;">
            <#list rootDirectories as directory>
                <li>
                    <h2 class="item">${directory}</h2>
                </li>
            </#list>
        </ul>
    </div>
</body>
</html>