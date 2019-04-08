package com.mmtap.modules.pat.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
//        String temp ="{\"code\":200,\"message\":\"\",\"data\":[    [\"李丽芬;\",\"A01F29/06\"],               [\"浙江省农业科学院;\",\"A01G22/20\"],[\"浙江省农业科学院;\",\"A01D89/00\"],[\"郑州中锣科技有限公司;\",\"A01F25/14\"],[\"黄小翠;\",\"A01F11/06\"],[\"宁波高新区晖云电子科技有限公司;\",\"A01F11/06\"],[\"南京途酷信息科技有限公司;\",\"A01F29/00\"],[\"南京途酷信息科技有限公司;\",\"A01F25/14\"],[\"山东巨明机械有限公司;\",\"A01D41/02\"],[\"金辰波;\",\"A01F15/08\"],[\"张莅茳;\",\"A01F25/14\"],[\"汝阳如宏饲料有限公司;\",\"A01F25/12\"],[\"连云港飞燕食品有限公司;\",\"A01F5/00\"],[\"重庆洁邦电器有限公司;\",\"A01F25/14\"],]}";
//        Object o = JSON.parseObject(temp);
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
    public void exepotExce(HttpServletResponse response,PatVo patVo) throws IOException {
        List dataList = patService.exportExcel(patVo);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.write(dataList);
        String fileName= new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=excel-"+fileName+".xls");
        ServletOutputStream out=response.getOutputStream();
        writer.flush(out);
        writer.close();
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
