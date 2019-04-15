package com.mmtap.modules.pat.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mmtap.modules.pat.dao.PatDao;
import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.service.PatService;
import com.mmtap.modules.pat.vo.CatVo;
import com.mmtap.modules.pat.vo.PatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        map.put("data",fd);
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
            map.put("data",fd);
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

    //3.1 主分类号，公开日
    // http://patents.weilaigongzuo.com/patents/analysis_categories
    @RequestMapping("/analysis_categories")
    public Object analysis_categories(){
        Map map = new HashMap();
        map.put("code",200);
        map.put("message","");
        List cat = patService.getTop(6);
        List<CatVo> sl = patService.analysis_categories(cat);

        Map dataMap = formatAC(cat,sl);
        map.put("data",dataMap);
        return map;
    }

    /**
     * 直方图数据格式化
     * @param cat
     * @param sl
     * @return
     */
    private Map formatAC(List cat,List sl) {
        Map dataMap = new HashMap();
        if (ObjectUtils.isEmpty(cat)||ObjectUtils.isEmpty(sl)){
            return dataMap;
        }

        //类型生成
        dataMap.put("categories",cat);
        List<Map> seriesData = new ArrayList();
        //数据生成
        List<CatVo> catVos = createObj(sl); //转成对象处理

        int currentYear = LocalDate.now().getYear();
        int year = currentYear-10; //开始年确定
        while (year<currentYear){
            //这一年==>6个类型的==>具体数量
            Map seriesItem = new HashMap();
            seriesItem.put("name",year);
            final String tempYear = year+"";
            List seriesItemValue = new ArrayList();
            for (Object ipc :cat){
                List<CatVo> item = catVos.stream()
                        .filter(o -> ipc.toString().equals(o.getIpc()) && o.getNian().equals(tempYear))
                        .collect(Collectors.toList());
                if (!ObjectUtils.isEmpty(item)){
                    seriesItemValue.add(item.get(0).getCou());
                }else {
                    seriesItemValue.add(0);
                }
            }
            seriesItem.put("data",seriesItemValue);

            seriesData.add(seriesItem);
            year = year+1;
        }
        dataMap.put("series",seriesData);
        return  dataMap;
    }

    private List<CatVo> createObj(List sl){
        List result = new ArrayList();
        for (int x = 0;x<sl.size();x++){
            if (ObjectUtils.isEmpty(sl.get(x))){
                continue;
            }
            Object[] o = (Object[]) sl.get(x);
            CatVo catVo = new CatVo();
            catVo.setIpc(Optional.ofNullable(o[0]).isPresent()?o[0].toString():"");
            catVo.setNian(Optional.ofNullable(o[1]).isPresent()?o[1].toString():"");
            catVo.setCou(Optional.ofNullable(o[2]).isPresent()? Integer.valueOf(o[2].toString()):0);
            result.add(catVo);

        }
        return result;
    }

    /**
     *
     * @param cat
     * @param se
     * @return
     */
    private Map findItem(String cat,List se){
        Map map = new HashMap();

        return map;
    }


    /**
     * 多重共线网络可视化
     * analysis_categories_network
     * @return
     */
    @RequestMapping("/analysis_categories_network")
    public Object analysis_categories_network(){
        Map map = new HashMap();
        map.put("code",200);
        map.put("message","");
        List sd = patService.analysis_categories_network();
        map.put("data",sd);
        return map;
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
