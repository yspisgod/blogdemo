package com.example.myblog.controller;

import com.example.myblog.entity.BlogEssay;
import com.example.myblog.entity.JsonResultSet;
import com.example.myblog.service.BlogEssayService;
import com.example.myblog.webentity.responseEntity.PageCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @auther : Dewey
 * @date : 2018/9/14 10 42
 * @description :  blog文章的操作
 */


@Controller
public class BlogEssayController {

    @Autowired
    BlogEssayService blogEssayServiceImpl;
    /*
        保存文章的操作 这个先放着 后面再做
     */
    @RequestMapping(value = "/save" , method = RequestMethod.POST)
    public ResponseEntity<JsonResultSet> saveEssay(){
        JsonResultSet jsonResultSet = new JsonResultSet();
        return ResponseEntity.ok(jsonResultSet);
    }

    /*
        根据页数获取文章 这里暂且只考虑用户只有我一个人 不然这里需要做人员校验
     */
    @RequestMapping(value = "/query/page/{pageNum}" , method = RequestMethod.GET)
    public ResponseEntity<JsonResultSet> queryByPage(@PathVariable(value = "pageNum")int pageNum , HttpServletRequest request){
        JsonResultSet  jsonResultSet = new JsonResultSet();

        int size = 8;
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        List<BlogEssay> blogEssays = blogEssayServiceImpl.getEssayByPage(size,pageNum,sort);
        if(blogEssays.size()>0){
            jsonResultSet.setStatusCode("0");
            jsonResultSet.setResultData(blogEssays);
        }else {
            jsonResultSet.setStatusCode("1");
            String resultMsg = "找不到数据";
            jsonResultSet.setResultData(request);
        }
        return ResponseEntity.ok(jsonResultSet);
    }

    /*
        获取文章总数 返回文章数和页面数
     */
    @RequestMapping(value = "/getPageNum")
    public ResponseEntity<JsonResultSet> queryPageNum(){
        JsonResultSet jsonResultSet = new JsonResultSet();
        long essayNum = blogEssayServiceImpl.getPageNum();
        PageCount pageCount = new PageCount();
        pageCount.setEssayNum(essayNum);
        int pageNum = 0;
        if(essayNum%8!=0){
            pageNum = (int)essayNum/8 + 1;
        }else {
            pageNum = (int)essayNum/8;
        }
        pageCount.setPageNum(pageNum);
        jsonResultSet.setStatusCode("0");
        jsonResultSet.setResultData(pageCount);
        return ResponseEntity.ok(jsonResultSet);
    }

    @GetMapping(value = "/getBlog/{id}")
    public ResponseEntity<JsonResultSet> getBlog(@PathVariable(value = "id")Long id){
        System.out.println("in to method :"+id);
        JsonResultSet jsonResultSet = new JsonResultSet();
        BlogEssay blogEssay = blogEssayServiceImpl.getEssayById(id);
        if(blogEssay!=null){
            jsonResultSet.setStatusCode("0");
            jsonResultSet.setResultData(blogEssay);
        }else {
            jsonResultSet.setStatusCode("1");
        }
        return ResponseEntity.ok(jsonResultSet);
    }
}