package ua.kiev.prog;

/*
1. Сделать кнопку при нажатии на которую выведется список всех загруженных фотографий с их id.
2. Сделать возможность выбрать из списка часть фото и удалить одним нажатием на кнопку.
3. Решить задачу про архиватор с помощью Spring MVC.
 */

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/")
public class MyController {

    //хранилище фоток
    private Map<Long, byte[]> photos = new HashMap<Long, byte[]>();

    //начальная страничка по запросу в корень
    @RequestMapping("/")
    public String onIndex() {
        return "index"; //отправляем пользователя на страничку index.jsp
    }

    //показать фото по id из формф
    @RequestMapping(value = "/view", method = RequestMethod.POST)
    //@RequestParam("photo_id") парсит пришедший атрибут "photo_id" и ложит в переменную long id
    public ResponseEntity<byte[]> onView(@RequestParam("photo_id") long id) {
        return photoById(id); //вызывает метод вернуть фото по id и возвращает результат его работы
    }

    //показатть все фото в галерее
    @RequestMapping("/view_all")
    public String viewAll(Model model) {
        Set<Long> id_set = photos.keySet();
        model.addAttribute("id_set", id_set);
        return "gallery";
    }

    // добавить одно или несколько фото
    @RequestMapping(value = "/add_photo", method = RequestMethod.POST)
    // @RequestParam парсит пришедший атрибут "photo" и ложит в переменную MultipartFile photo
    public String onAddPhoto(Model model, @RequestParam("photo") MultipartFile[] photosArr) {
        System.out.println(photosArr);
        System.out.println(photosArr.length);
        Set<Long> id_set = new HashSet<>(); //список id фоток
        boolean addPhoto = false; //добавлино ли хоть одно новое фото с запроса
        for (MultipartFile photo : photosArr) {
            try {
                long id = System.currentTimeMillis(); //генерируем id
                byte[] photoArr = photo.getBytes(); //файл не добавлен прирываем итерацию
                if(photoArr.length==0){
                    break;
                }
                id_set.add(id);
                System.out.println(id);
                photos.put(id, photoArr); //ложим id и фото в хранилище
                addPhoto = true;
            } catch (IOException e) {
                throw new PhotoErrorException(); //если что-то не так бросаем исключение
            }
        }
        if (!addPhoto) {//если ничего не передали кидаем исключение
            throw new PhotoErrorException();
        }
        model.addAttribute("id_set", id_set); //в ответ ложим список id добавленных фото
        return "result"; //отправляем пользователя на страничку
    }

    //удалить фото по id
    @RequestMapping("/delete/{photo_id}")
    public String onDelete(@PathVariable("photo_id") long id) {
        if (photos.remove(id) == null)
            throw new PhotoNotFoundException();
        else
            return "index";
    }

    //удалить или скачать выбранные фотографии
    @RequestMapping("/delete_or_load")
    public String onDeleteAll(Model model,
                              HttpServletResponse response,
                              @RequestParam(value = "photos_id", required = false) long[] photosId,
                              @RequestParam(value = "removeOrLoad") String removeOrLoad) {
        if (photosId != null) { //если пользователь что-то отметил
            if(removeOrLoad.equals("Remove selected")) { //если удалить фото
                for (long id : photosId) {
                    photos.remove(id);
                }
            }
            else{ //если загрузить фото
                try {
                    byte[] photo = null; //фото или архив с фото
                    String name = ""; //имя файла
                    if(photosId.length > 1){ //если фотографий больше чем одна отправляем архив
                        ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
                        ZipOutputStream zipOs = new ZipOutputStream(byteOs);
                        for (long id : photosId) { //проходим по списку выбраных id
                            byte[] ph = photos.get(id); //получаем фото по id
                            ZipEntry zipEntry = new ZipEntry(id+".jpeg");//Имя файла - имя файла в архиве
                            zipOs.putNextEntry(zipEntry);
                            zipOs.write(ph, 0, ph.length);
                            zipOs.closeEntry();
                        }
                        zipOs.close();
                        photo = byteOs.toByteArray();
                        byteOs.close();
                        name = "photos.zip";
                        response.setContentType("application/zip");
                    }
                    else { //если одна фотография отправляем просто фото без архива
                        long id = photosId[0];
                        name = id +".jpeg";
                        photo = photos.get(id);
                        response.setContentType("image/jpeg");
                    }
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
                    InputStream is = new ByteArrayInputStream(photo);
                    OutputStream os = response.getOutputStream();
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = is.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                    }
                    os.flush();
                    os.close();
                    is.close();
                }
                catch (IOException e){
                    throw new PhotoNotFoundException();
                }
            }
        }
        return viewAll(model);
    }

    //показать фото по id записанном в запросе
    @RequestMapping("/photo/{photo_id}")
    //@PathVariable("photo_id") парсит из запроса то что пришло вместо "photo_id" и ложит в переменную long id
    public ResponseEntity<byte[]> onPhoto(@PathVariable("photo_id") long id) {
        return photoById(id); //вызывает метод вернуть фото по id и возвращает результат его работы
    }

    //вернуть фото по id, принимает на вход id
    private ResponseEntity<byte[]> photoById(long id) {
        byte[] bytes = photos.get(id); //вытаскиваем фотку из map по ID шнику
        if (bytes == null) //если такого фото нет бросаем исключение
            throw new PhotoNotFoundException();
        //если фото есть создаем http заголовок
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); //указываем тип содержимого

        //возвращаем сущность-ответ в которую ложим фотку, заголовок, статус ответа
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    }
}
