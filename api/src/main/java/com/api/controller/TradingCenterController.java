package com.api.controller;

import com.api.core.base.BaseController;
import com.api.core.base.BaseService;
import com.api.domain.input.TradingCenterInput;
import com.api.domain.output.TradingCenterOutput;
import com.api.model.TradingCenter;
import com.api.service.TradingCenterService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
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
 * @date: Created in 17:05 2018/11/27
 * @modified by:
 */
@RestController
@RequestMapping("tradingCenter")
@Api(value = "公共资源交易中心",description = "公共资源交易中心操作",tags = {"公共资源交易中心接口"})
public class TradingCenterController  extends BaseController<TradingCenterOutput, TradingCenter,Integer> {

    @Autowired
    private TradingCenterService tradingCenterService;

    @Override
    public BaseService<TradingCenterOutput, TradingCenter, Integer> getService() {
        return tradingCenterService;
    }

    @PostMapping("formPost")
    @ApiOperation("新增消息")
    public ResponseResult formPost(@Validated @RequestBody TradingCenterInput tradingCenterInput) throws IntrospectionException, MethodArgumentNotValidException, IllegalAccessException, InvocationTargetException {
        if(tradingCenterInput.getTradingCenterList()==null||tradingCenterInput.getTradingCenterList().size() ==0){
            return ResponseResult.error(PARAM_ERROR);
        }
        int result = tradingCenterService.addNews(tradingCenterInput);
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
