package sb.project;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/categories")
    public Categories categories(@RequestParam(value="name", required=false, defaultValue="Видеокарта") String name) {
        return new Categories(counter.incrementAndGet(),
                    name,"");
    }
}