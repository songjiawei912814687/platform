package com.api.controller;

import com.api.core.base.BaseController;
import com.api.core.base.BaseService;
import com.api.domain.input.TradingCenterInput;
import com.api.domain.input.TradingResultInput;
import com.api.domain.output.TradingResultOutput;
import com.api.model.TradingResult;
import com.api.service.TradingResultService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: Young
 * @description:
 * @date: Created in 21:05 2018/11/27
 * @modified by:
 */
@RestController
@RequestMapping("tradingResult")
@Api(value = "公共资源交易成果公告",description = "公共资源交易成果公告操作",tags = {"公共资源交易成果公告接口"})
public class TradingResultController extends BaseController<TradingResultOutput, TradingResult,Integer> {

    @Autowired
    private TradingResultService tradingResultService;

    @Override
    public BaseService<TradingResultOutput, TradingResult, Integer> getService() {
        return tradingResultService;
    }

    @PostMapping("formPost")
    @ApiOperation("新增消息")
    public ResponseResult formPost(@Validated @RequestBody TradingResultInput tradingResultInput) throws IntrospectionException, MethodArgumentNotValidException, IllegalAccessException, InvocationTargetException {
        if(tradingResultInput==null){
            return ResponseResult.error(PARAM_ERROR);
        }
        int result = tradingResultService.addNews(tradingResultInput);
        if(result<0){
            return ResponseResult.error("添加失败");
        }
        return ResponseResult.success("添加成功");
    }

    @GetMapping(value = "selectPage")
    public ResponseResult selectPage(HttpServletRequest request){
        return super.selectPageList(new PageData(request));
    }
}
