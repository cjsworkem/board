package com.bitc.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bs")
public class BsController {

    @RequestMapping("/test1")
    public String bsTest1() throws Exception{
        return "bs/test1";
    }
    @RequestMapping("/test1-1")
    public String bsTest11() throws Exception{
        return "bs/test1-1";
    }
    @RequestMapping("/test2")
    public String bsTest2() throws Exception{
        return "bs/test2";
    }
    @RequestMapping("/test3")
    public String bsTest3() throws Exception{
        return "bs/test3";
    }
    @RequestMapping("/test4")
    public String bsTest4() throws Exception{
        return "bs/test4";
    }
    @GetMapping("/test5")
    public String bsTest5() throws Exception{
        return "bs/test5";
    }
    @RequestMapping("/test6")
    public String bsTest6() throws Exception{
        return "bs/test6";
    }
    @RequestMapping ("/test7")
    public  String bsTest7() throws  Exception{
        return "bs/test7";
    }
}
