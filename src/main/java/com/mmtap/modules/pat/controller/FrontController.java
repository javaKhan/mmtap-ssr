package com.mmtap.modules.pat.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.MiniTableRenderData;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.style.TableStyle;
import com.deepoove.poi.util.BytePictureUtils;
import com.mmtap.common.ResultGenerator;
import com.mmtap.modules.pat.dao.PatDao;
import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.service.PatService;
import com.mmtap.modules.pat.vo.CatVo;
import com.mmtap.modules.pat.vo.PatVo;
import com.mmtap.modules.pat.vo.RepVo;
import lombok.extern.slf4j.Slf4j;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/patents")
@Slf4j
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
        Map map = getHeaderMap();
        writer.setHeaderAlias(map);
        writer.write(dataList);
        String fileName= new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=excel-"+fileName+".xls");
        ServletOutputStream out=response.getOutputStream();
        writer.flush(out);
        writer.close();
    }

    private Map getHeaderMap() {
        Map map = new HashMap();
        map.put("pid","流水号");
        map.put("industry","行业");
        map.put("name","名称");
        map.put("applyNo","申请号");
        map.put("tongZu","同族");
        map.put("yinZheng","引证");
        map.put("beiYin","被引");
        map.put("applyDate","申请日");
        map.put("publicNo","公开（公告）号");
        map.put("publicDate","公开（公告）日");
        map.put("IPCTypeNo","IPC分类号");
        map.put("applyPerson","申请（专利权）人");
        map.put("inventor","发明人");
        map.put("priorityNumber","优先权号");
        map.put("priorityDay","优先权日");
        map.put("applyPersonAddress","申请人地址");
        map.put("applyPersonZip","申请人邮编");
        map.put("legalEffectiveDate","法律状态生效日");
        map.put("legalEffectiveMeaning","法律状态含义");

        return map;
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

    @RequestMapping("/report/pic")
    public Object  generalPic(PatVo patVo){
        PatVo patVoAll = new PatVo() ;
        patVoAll.setLevel1(patVo.getLevel1());
        patVoAll.setLevel2(patVo.getLevel2());

        Map m = new HashMap();
        //专利数量
        m.put("partArea",patService.annual(patVo));
        m.put("allArea",patService.annual(patVoAll));

        //专利权人和其所拥有的专利数量
        List d = patService.patentee(patVo);
        List fd = format(d);
        m.put("partPerson",fd);
        List da = patService.patentee(patVoAll);
        List fda = format(da);
        m.put("allPerson",fda);

//        分类号的数量对比
        List idl = format(patService.ana_ipc(patVo));
        List idla = format(patService.ana_ipc(patVoAll));
        m.put("partIpc",idl);
        m.put("allIpc",idla);

//        多重共现网络
        m.put("partNet",patService.network(patVo));
        m.put("allNet",patService.network(patVoAll));

        return ResultGenerator.ok(m);
    }

    @RequestMapping("/report/word")
    public void  generalWord(HttpServletResponse response,RepVo repVo){
        try {
            Map dataMap = new HashMap();
            dataMap.put("vo",repVo);

            //8个图
            dataMap.put("partArea", new PictureRenderData(600, 600, ".png", BytePictureUtils.getBufferByteArray(GBI(repVo.getPartArea()))));
            dataMap.put("allArea", new PictureRenderData(600, 600, ".png", BytePictureUtils.getBufferByteArray(GBI(repVo.getAllArea()))));
            dataMap.put("areaMin",patService.findAreaMin(repVo.getProvince(),repVo.getCity(),repVo.getLevel1(),repVo.getLevel2()));
            dataMap.put("areaMax",patService.findAreaMax(repVo.getProvince(),repVo.getCity(),repVo.getLevel1(),repVo.getLevel2()));
            dataMap.put("allAreaMin",patService.findAllAreaMin());
            dataMap.put("allAreaMax",patService.findAllAreaMax());


            /**
             * 申请人部分
             */
            dataMap.put("partPerson", new PictureRenderData(600, 600, ".png", BytePictureUtils.getBufferByteArray(GBI(repVo.getPartPerson()))));
            dataMap.put("allPerson", new PictureRenderData(600, 600, ".png", BytePictureUtils.getBufferByteArray(GBI(repVo.getAllPerson()))));
            List partPersonTop3 = patService.findPartPersonTop3(repVo.getProvince(),repVo.getCity());
            List allPersonTop3 = patService.findAllPersonTop3();
            RowRenderData header = RowRenderData.build("排名",repVo.getCity()+"地区","数量","全球（全国）地区","数量");
            TableStyle style = new TableStyle();
            style.setBackgroundColor("ADADAD");
            style.setAlign(STJc.CENTER);
            header.setStyle(style);
            List<RowRenderData> lineList = formatTable(partPersonTop3,allPersonTop3);
            dataMap.put("personTable",new MiniTableRenderData(header, lineList));


            /**
             * 热门主分类专利明细表
             */
            RowRenderData ipcTopHeader = RowRenderData.build("公开号","法律状态含义","专利名称","专利权人","IPC分类","公开日","国家","地区");
            ipcTopHeader.setStyle(style);
            List dbList = patService.table4IpcTop(repVo);
            List<RowRenderData> ipcTopList = new ArrayList();
            dbList.forEach(item->{
                Object[] temp = (Object[] )item;
                String appNo = Optional.ofNullable(temp[3]).isPresent()?temp[3].toString():"";
                String hy = Optional.ofNullable(temp[11]).isPresent()?temp[11].toString():"";;
                String name =Optional.ofNullable(temp[12]).isPresent()?temp[12].toString():"";;
                String person=Optional.ofNullable(temp[9]).isPresent()?temp[9].toString():"";;
                String ipc = Optional.ofNullable(temp[19]).isPresent()?temp[19].toString():"";;
                String pubDate = Optional.ofNullable(temp[15]).isPresent()?temp[15].toString():"";;
//                String gj=Optional.ofNullable(temp[3]).isPresent()?temp[3].toString():"";;
                String city = Optional.ofNullable(temp[6]).isPresent()?temp[6].toString().replace(";",""):"";
                if (!StringUtils.isEmpty(repVo.getCity())){
                    city = city+repVo.getCity();
                }
                RowRenderData row = RowRenderData.build(appNo,hy,name,person,ipc,pubDate,"CN",city);
                ipcTopList.add(row);
            });
            dataMap.put("ipcTopTable",new MiniTableRenderData(ipcTopHeader, ipcTopList));




            /**
             * IPC图部分
             */
            dataMap.put("partIpc", new PictureRenderData(600, 600, ".png", BytePictureUtils.getBufferByteArray(GBI(repVo.getPartIpc()))));
            dataMap.put("allIpc", new PictureRenderData(600, 600, ".png", BytePictureUtils.getBufferByteArray(GBI(repVo.getAllIpc()))));
            List partIpc = patService.findPartIpcTop(repVo.getProvince(),repVo.getCity());
            List allIpc =patService.findAllIpcTop();
            List<RowRenderData> ipcLines = formatTable(partIpc,allIpc);
            dataMap.put("ipcTable",new MiniTableRenderData(header,ipcLines));

            /**
             * 网络图部分
             */
            dataMap.put("partNet", new PictureRenderData(600, 600, ".png", BytePictureUtils.getBufferByteArray(GBI(repVo.getPartNet()))));
            dataMap.put("allNet", new PictureRenderData(600, 600, ".png", BytePictureUtils.getBufferByteArray(GBI(repVo.getAllNet()))));

            /**
                附录部分
             */
            RowRenderData flHeader = RowRenderData.build("专利名称","申请号","申请日","公开（公告）号","公开（公告）日","IPC分类号","申请（专利权）人","发明人","申请人地址","法律状态生效日","法律状态含");
            TableStyle flStyle = new TableStyle();
            flStyle.setBackgroundColor("ADADAD");
            flStyle.setAlign(STJc.CENTER);
            flHeader.setStyle(flStyle);
            Page fuluPage = patService.findFulu(repVo.getProvince(),repVo.getCity(),repVo.getLevel1(),repVo.getLevel2());
            List<RowRenderData> flLines = formatFulu(fuluPage.getContent());
            dataMap.put("fulu",new MiniTableRenderData(flHeader,flLines));


//            File templateFile = ResourceUtils.getFile("classpath:static/tp/report-tp5.docx");
//            XWPFTemplate template = XWPFTemplate.compile(templateFile);

            String tpPath = System.getProperty("user.dir")+"/config/report-tp5.docx";
            File templateFile = ResourceUtils.getFile(tpPath);
            XWPFTemplate template = XWPFTemplate.compile(templateFile);

            template.render(dataMap);
            String fileName= new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            response.setHeader("Set-Cookie", "fileDownload=true; path=/");
            response.addHeader("Content-Type", "application/vnd.ms-word");
            response.addHeader("Content-Type", "application/x-msword");
            response.setHeader("Content-Disposition","attachment;filename=report-"+fileName+".docx");
            ServletOutputStream out = response.getOutputStream();
            template.write(out);
            out.flush();
            out.close();
            template.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<RowRenderData> formatFulu(List<Patent> content) {
        List res = new ArrayList();
        if(!ObjectUtils.isEmpty(content)){
            for (Patent p :content){
                String pd = Optional.ofNullable(p.getPublicDate()).isPresent()? p.getPublicDate().toString():"";
                String sd = Optional.ofNullable(p.getApplyDate()).isPresent()? p.getApplyDate().toString():"";
                RowRenderData line = RowRenderData.build(p.getName(),p.getApplyNo(),sd,p.getPublicNo(),pd,
                        p.getIPCTypeNo(),p.getApplyPerson(),p.getInventor(),p.getApplyPersonAddress(),
                        p.getLegalEffectiveDate(),p.getLegalEffectiveMeaning());
                res.add(line);
            }
        }
        return res;
    }

    private List<RowRenderData> formatTable(List bl, List al) {
        List res = new ArrayList();
        for(int i=0;i<3;i++){
            String order = String.valueOf(i+1);
            String bn = "",bc="";
            if(bl!=null && bl.size()>i && null!=bl.get(i)){
                Object[] o = (Object[]) bl.get(i);
                bn=o[0].toString();
                bc=String.valueOf(o[1]);
            }
            String an = "",ac = "";
            if(al!=null && al.size()>i && null!=al.get(i)){
                Object[] o = (Object[]) al.get(i);
                an=o[0].toString();
                ac=String.valueOf(o[1]);
            }
            RowRenderData line = RowRenderData.build(order,bn,bc,an,ac);
            res.add(line);
        }
        return  res;
    }

    /**
     * 生成二进制图片
     * @param base64str
     * @return
     * @throws Exception
     */
    private BufferedImage GBI(String base64str) throws Exception {
        if (!StringUtils.isEmpty(base64str)){
            String base64Data =  base64str.split(",")[1];
            byte[] bs = new BASE64Decoder().decodeBuffer(base64Data);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bs);
            BufferedImage bi = ImageIO.read(inputStream);

            return bi;
        }
        return null;
    }
}
