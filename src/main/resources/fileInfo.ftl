<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Информация о файле</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <style>
        h3 {
            font-optical-sizing: auto;
            font-weight: 400;
            font-style: normal;
        }
    </style>
</head>
<body>
    <div style="margin-left: 2%; color: red; font-family: Montserrat, serif;">
        <h1 style="font-optical-sizing: auto; font-weight: 300; font-style: normal;">Сведения</h1>
        <hr style="width: 40%; margin-right: 60%; border-color: red;">
        <h3>Владелец: ${fileInfo.get(0)}</h3>
        <h3>Дата добавления: ${fileInfo.get(1)}</h3>
        <h3>Последнее обновление: ${fileInfo.get(2)}</h3>
        <h3>Теги: ${fileInfo.get(3)}</h3>
        <h3>Разрешение на скачивание: ${fileInfo.get(4)}</h3>
    </div>
</body>
</html>