<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Prog.kiev.ua</title>
    </head>
    <body>
        <div align="center"> <!--при переходе на эту страничку нужно указать в model.addAttribute("photo_id", id)-->

            <c:forEach items="${id_set}" var="photo_id">
                <tr>

                    <h1>Your photo id is: ${photo_id}</h1><!--значение будет подставлено в поля photo_id, получим стору с ID фотки-->

                    <!--кнопка Delete Photo при нажатии на которую будет сделан get запрос по адресу  /delete/"id_фотки"-->
                    <input type="submit" value="Delete Photo" onclick="window.location='/delete/${photo_id}';" />
                    <!--кнопка Upload New при нажатии на которую будет сделан get запрос по адресу "/"-->
                    <input type="submit" value="Upload New" onclick="window.location='/';" />

                    <!--вывод фото на экран, будет сделан get запрос по адресу /photo/id" -->
                    <br/><br/><img src="/photo/${photo_id}" />

                </tr>
            </c:forEach>

        </div>
    </body>
</html>
