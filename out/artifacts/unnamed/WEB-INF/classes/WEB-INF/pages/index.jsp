<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Prog.kiev.ua</title>
  </head>
  <body>
     <div align="center">
         <!--показать уже загруженную фотку по id -->
        <form action="/view" method="POST"> <!--форма будет отправлять POST запрос по адресу /view -->
            Photo id: <input type="text" name="photo_id">  <!--будет отправлен текст в атрибуте photo_id -->
            <input type="submit" /> <!--кнопка отправить -->
        </form>
         <!--загрузить новую фотку -->
         <!--форма будет отправлять multipart POST запрос по адресу /add_photo -->
        <form action="/add_photo" enctype="multipart/form-data" method="POST">

<span id="table">
    <table border=0 cellspacing=0 cellpadding=3>
        <tr>
            <th><a href="#" onclick="return addline();">add more</a></th>
        </tr>
        <tr id="newline" nomer="[0]">
            <td>Photo: <input type="file" name="photo">
            </td>
        </tr>
    </table>
</span>
            <script>
                var c=0;
                function addline()
                {
                    c++;
                    s=document.getElementById('table').innerHTML;
                    s=s.replace(/[\r\n]/g,'');
                    re=/(.*)(<tr id=.*>)(<\/table>)/gi;
                    re2=/\[\d+\]/gi;
                    s1=s.replace(re,'$2');
                    s2=s1.replace(re2,'['+c+']');
                    s2=s2.replace(/(rmline\()(\d+\))/gi,'$1'+c+')');
                    s=s.replace(re,'$1$2'+s2+'$3');
                    document.getElementById('table').innerHTML=s;
                    return false;
                }
            </script>

            <input type="submit" />

        </form>







             <input type="submit" value="Show all photos" onclick="window.location='/view_all';" />
      </div>



   </body>
</html>
