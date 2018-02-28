package com.rocoinfo.common;

import com.rocoinfo.dto.StatusDto;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <dl>
 * <dd>描述:</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：15/8/11 下午3:47</dd>
 * <dd>创建人： weiys</dd>
 * </dl>
 */
@SuppressWarnings("all")
public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @ExceptionHandler({BindException.class})
    public Object validationException(BindException ex) {
        logger.error("校验异常:", ex);
        List<FieldError> errors = ex.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isNotEmpty(errors)) {
            errors.stream().forEach((o) -> sb.append(o.getDefaultMessage()).append(","));
        }
        // 删除最后一个逗号
        sb.delete(sb.lastIndexOf(","), sb.length());
        return StatusDto.buildFailure(sb.toString());
    }

    @ExceptionHandler({Exception.class})
    public Object exception(Exception ex) {
        logger.error("异常:", ex);
        return StatusDto.buildFailure("操作失败！");
    }
}
