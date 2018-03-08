package cn.damei.rest;

import cn.damei.common.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/error")
public class ExceptionRestController extends BaseController {

    @RequestMapping("/null")
    public Object throwNullPointException() {
        throw new NullPointerException("空指针异常");
    }
}
