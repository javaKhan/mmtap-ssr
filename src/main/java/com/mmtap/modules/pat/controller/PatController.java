package com.mmtap.modules.pat.controller;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.mmtap.common.ResultGenerator;
import com.mmtap.modules.pat.dao.IPCDao;
import com.mmtap.modules.pat.dao.PatDao;
import com.mmtap.modules.pat.model.IPC;
import com.mmtap.modules.pat.model.PatType;
import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.service.PatService;
import com.mmtap.modules.pat.service.PatTypeService;
import com.mmtap.modules.pat.vo.ReverseItem;
import com.mmtap.modules.pat.vo.ReverseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pat")
@Slf4j
public class PatController {

    @Autowired
    private PatDao patDao;

    @Autowired
    private PatService patService;

    @Autowired
    private PatTypeService patTypeService;

    @Autowired
    private IPCDao ipcDao;


    /*
     * 前端使用
     */



    /*
        服务端使用
     */
    @RequestMapping("/manage/all")
    public Page<Patent> findAll(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return patDao.findAll(pageable);
    }

    /**
     * 导入Excel
     */
    @PostMapping("/upload")
    public Object importExcel(@RequestParam("file") MultipartFile file){
        Map map = new HashMap();
        if (file.isEmpty()) {
            return ResultGenerator.fail("上传失败，请选择文件");
        }
        try {
            boolean isCsvFile = file.getOriginalFilename().toLowerCase().endsWith("csv")?true:false;
            if (isCsvFile){
                List pats = new ArrayList<Patent>();
                List patTypeList = new ArrayList();

                DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
                CsvReader reader = CsvUtil.getReader();
                CsvData data  = reader.read(br);
                List<CsvRow> rows = data.getRows();
                for (int i=1;i<rows.size();i++){
                    CsvRow row = rows.get(i);
                    log.info(row.getFieldCount()+"列  "+row.toString());
                    Patent pat = new Patent();
                    pat.setName(row.get(1));
                    pat.setIndustry("");
                    pat.setApplyNo(row.get(2));
                    pat.setTongZu(row.get(3));
                    pat.setYinZheng(row.get(4));
                    pat.setBeiYin(row.get(5));
                    if (StringUtils.isNotEmpty(row.get(6))){
                        pat.setApplyDate(dateFormat.parse(row.get(6)));
                    }
                    pat.setPublicNo(row.get(7));
                    if (StringUtils.isNotEmpty(row.get(8))){
                        pat.setPublicDate(dateFormat.parse(row.get(8)));
                    }
                    pat.setIPCTypeNo(row.get(9));
                    pat.setApplyPerson(row.get(10));
                    pat.setInventor(row.get(11));
                    pat.setPriorityNumber(row.get(12));
                    pat.setPriorityDay(row.get(13));
                    pat.setApplyPersonAddress(row.get(14));
                    pat.setApplyPersonZip(row.get(15));
                    pat.setLegalEffectiveDate(row.get(17));
                    pat.setLegalEffectiveMeaning(row.get(18));
                    log.info(pat.toString());
                    pats.add(pat);
                    //类型规约
                    if(StringUtils.isNotEmpty(pat.getIPCTypeNo())){
                        String[] patTypes = pat.getIPCTypeNo().split("[;]");
                        for (int m=0;m<patTypes.length;m++){
                            PatType pt = new PatType();
                            pt.setPat(pat.getApplyNo());
                            pt.setIpcType(patTypes[m]);
                            patTypeList.add(pt);
                        }
//                        patTypeService.saveTypes(patTypeList);
                    }
//                    patService.savePatent(pat);

                    if(i%2000==0){
                        patTypeService.saveTypes(patTypeList);
                        patService.saveImport(pats);
                        pats = new ArrayList<Patent>();
                        patTypeList = new ArrayList();
                    }
                }
                patTypeService.saveTypes(patTypeList);
                patService.saveImport(pats);
                pats = new ArrayList<Patent>();
                patTypeList = new ArrayList();

            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultGenerator.fail("文件格式异常");
        }
        map.put("success","1");
        return map; //上传插件要求成功必须如此返回
    }

    @RequestMapping("/reverse")
    public Object reverse(){
        //{"code":"A","name":"农业","data":[{"code",name:"","data":["it":"","":]}]}
        List resList = new ArrayList();
        List<ReverseItem> res = patService.queryReverse();
        List<ReverseItem> items = res.stream().filter(item->StringUtils.isNotEmpty(item.getIpc()) && StringUtils.isNotEmpty(item.getIt())).collect(Collectors.toList());
        List<IPC> ipcs = ipcDao.findAll();
        final Map ipcMap = ipcs.stream().filter(ipc -> !"".equals(ipc.getCode()) && StringUtils.isNotEmpty(ipc.getName()))
                .collect(Collectors.groupingBy(IPC::getCode));
        for (Object key :ipcMap.keySet()){
            IPC ipc = (IPC) ((List)ipcMap.get(key)).get(0);
            ipcMap.put(key,ipc);
        }



        if (!ObjectUtils.isEmpty(items)){
            //一级结点
            Map levelOne = items.stream().collect(Collectors.groupingBy(ReverseItem::getIt));
            levelOne.forEach((k1,v1)->{
                ReverseVo vo = new ReverseVo();
                vo.setCode(k1+"");
                IPC ipc1 = (IPC)ipcMap.get(k1);
                vo.setName(ipc1.getName());

                //二级结点
                List ipcList = new ArrayList();
                List<ReverseItem> v1List = (List<ReverseItem>)v1;
                Map leveTwo = v1List.stream().collect(Collectors.groupingBy(ReverseItem::getIpc));

                leveTwo.forEach((k2,v2)->{
                    ReverseVo item = new ReverseVo();
                    IPC ipc2 = (IPC)ipcMap.get(k2);
                    item.setCode(k2+"");
                    String name2 = "";
                    if(null!=ipc2 && StringUtils.isNotEmpty(ipc2.getName())){
                        name2 = ipc2.getName();
                    }
                    item.setName(name2);
                    List<ReverseItem> v2List = (ArrayList)v2;
                    int currentYear = LocalDate.now().getYear();
                    for (int m=0;m<10;m++){
                        int indexYear = currentYear-m;
                        Long isHave = v2List.stream().filter(i->i.getNian()==indexYear).count();
                        if (isHave==0){
                            ReverseItem fix = new ReverseItem();
                            fix.setNian(indexYear);
                            fix.setSl(BigInteger.valueOf(0));
                            v2List.add(fix);
                        }
                    }
                    v2List.sort(Comparator.comparing(ReverseItem::getNian));
                    item.setData(v2List);
                    ipcList.add(item);
                });
                ipcList.sort(Comparator.comparing(ReverseVo::getCode));
                vo.setData(ipcList);
                resList.add(vo);
            });
        }
        resList.sort(Comparator.comparing(ReverseVo::getCode));
        return resList;
    }
}
