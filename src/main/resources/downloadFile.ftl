<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
    <h3>References</h3>
    <#list references as reference>
    ${reference_index + 1}. <a href="${reference.url}"> ${reference.title} </a> <br/>
    </#list>
</body>
</html>
