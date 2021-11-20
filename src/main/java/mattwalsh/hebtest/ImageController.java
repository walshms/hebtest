package mattwalsh.hebtest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    @GetMapping(value="/")
    public static String hello() {
        return "howdy!";
    }

}
