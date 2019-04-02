package com.mmtap.modules.pat.controller;

import com.mmtap.modules.pat.dao.PatDao;
import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.service.PatService;
import com.mmtap.modules.pat.vo.PatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/patents")
public class FrontController {

    @Autowired
    private PatService patService;
    @Autowired
    private PatDao patDao;


    //1 年度数量
    @RequestMapping("/annual")
    public Object annual(PatVo vo){
        Map map = new HashMap();
        map.put("code",200);
        map.put("message","");
        map.put("data",patService.annual(vo));
        return map;
    }
    //2 专利权利人和其拥有专利数量
    @RequestMapping("/patentee")
    public Object person(PatVo vo){
        Map map = new HashMap();
        map.put("code",200);
        map.put("message","");
        List d = patService.patentee(vo);
        List fd = format(d);
        List warpList = new ArrayList();
        Map wapMap = new HashMap();
        wapMap.put("name","专利持有人");
        wapMap.put("data",fd);
        warpList.add(wapMap);
        map.put("data",warpList);
        return map;
    }
    //3 主分类的数量对比
    @RequestMapping("/categories_comparsion")
    public Object category(PatVo vo){
            Map map = new HashMap();
            map.put("code",200);
            map.put("message","");
            List d =patService.category(vo);
            List fd = format(d);

            List warpList = new ArrayList();
            Map wapMap = new HashMap();
            wapMap.put("name","主分类号");
            wapMap.put("data",fd);
            warpList.add(wapMap);
            map.put("data",warpList);
            return map;
    }

    private List format(List d) {
        List l = new ArrayList();
        for (int i=0;i<d.size();i++){
            Map m = new HashMap();
            Object[] o = (Object[])d.get(i);
            m.put("name",o[0].toString());
            m.put("value",o[1]);
            l.add(m);
        }
        return l;
    }

    //4 多重共现网络
    @RequestMapping("/network")
    public Object network(PatVo vo){
        Map map = new HashMap();
        map.put("code",200);
        map.put("message","");
        map.put("data",patService.network(vo));
        return map;
    }

    //5 列表显示
    @RequestMapping("/list")
    public Page<Patent> findList(@RequestParam(defaultValue = "20") int limit ,@RequestParam(defaultValue = "0") int offset,PatVo patVo){
        Pageable pageable = getPageable(limit,offset,"pid");
        Page page = patService.list(pageable,patVo);
        return page;
    }

    @RequestMapping("/detail")
    public Object getById(@RequestParam String id){
        Map map = new HashMap();
        map.put("code",200);
        map.put("message","");
        map.put("data","");
        Optional optional = patDao.findById(id);
        if (optional.isPresent()){
            map.put("data", optional.get());
        }
        return map;
    }

    //6 导出
    @RequestMapping("/export")
    public void exepotExce(PatVo patVo){

    }


    //将前端分页参数进行转换
    public static Pageable getPageable(int limit,
                                       int offset,
                                       String properties) {
        int page = offset / limit;
        int size = limit;
        Sort sort = new Sort(Sort.Direction.ASC, properties);
        return PageRequest.of(page, size, sort);
    }

}
