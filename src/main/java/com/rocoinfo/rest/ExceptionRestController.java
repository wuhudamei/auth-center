package com.rocoinfo.rest;

import com.rocoinfo.aop.logger.Logger;
import com.rocoinfo.common.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/7/7 下午5:12</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@RestController
@RequestMapping("/error")
public class ExceptionRestController extends BaseController {

    @RequestMapping("/null")
    @Logger(module = "ERROR_MODULE")
    public Object throwNullPointException() {
        throw new NullPointerException("空指针异常");
    }
}
