package com.mmtap.modules.pat.controller;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.mmtap.common.ResultGenerator;
import com.mmtap.modules.pat.dao.PatDao;
import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.service.PatService;
import com.mmtap.modules.pat.vo.PatVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/pat")
@Slf4j
public class PatController {

    @Autowired
    private PatDao patDao;

    @Autowired
    private PatService patService;

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

        List pats = null;
        Map map = new HashMap();
        if (file.isEmpty()) {
            return ResultGenerator.fail("上传失败，请选择文件");
        }
        try {
//            boolean isXls = file.getOriginalFilename().toLowerCase().endsWith("xls")?true:false;
//            boolean isXlsX = file.getOriginalFilename().toLowerCase().endsWith("xlsx")?true:false;
            boolean isCsvFile = file.getOriginalFilename().toLowerCase().endsWith("csv")?true:false;
            if (isCsvFile){
                pats = importCsv(file);
            }
            if (null!=pats && pats.size()>0){
                patService.saveImport(pats);
            }else {
                ResultGenerator.fail();
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultGenerator.fail("文件格式异常");
        }
        map.put("success","1");
        return map; //上传插件要求成功必须如此返回
    }

    private List importCsv(MultipartFile file) throws Exception{
        List pats = new ArrayList<Patent>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CsvReader reader = CsvUtil.getReader();
        CsvData data  = reader.read(br);
        List<CsvRow> rows = data.getRows();
        for (int i=1;i<rows.size();i++){
            CsvRow row = rows.get(i);
            log.info(row.getFieldCount()+"列  "+row.toString());
//            for (CsvRow row : rows) {
            //getRawList返回一个List列表，列表的每一项为CSV中的一个单元格（既逗号分隔部分）
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
//            pat.setApplyNo(row.get(2)); //第16列就是第2列  ，导入文件列重复
            pat.setLegalEffectiveDate(row.get(17));
            pat.setLegalEffectiveMeaning(row.get(18));
            log.info(pat.toString());
            pats.add(pat);
        }
        return pats;
    }




//    /**
//     * 导入excel格式文件
//     * @param file
//     * @return
//     */
//    private List importXls(MultipartFile file) throws Exception{
//        List pats = new ArrayList<Patent>();
//        log.info("文件类型:" + file.getContentType() + "   文件名:" + file.getOriginalFilename());
//        boolean isXls = file.getOriginalFilename().toLowerCase().endsWith("xls") ? true : false;
//        boolean isXlsX = file.getOriginalFilename().toLowerCase().endsWith("xlsx") ? true : false;
//        Workbook workbook = null;
////            if(isXlsX || isXls){
//        if (!isXls) {
//            workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
//        } else {
//            workbook = new XSSFWorkbook(file.getInputStream());
//        }
//        int sheets = workbook.getNumberOfSheets();
//        for (int i = 0; i < sheets; i++) {
//            Sheet sheet = workbook.getSheetAt(i);
//            //获取多少行
//            int rows = sheet.getPhysicalNumberOfRows();
//            Patent pat = null;
//            //遍历每一行，注意：第 0 行为标题
//            for (int j = 1; j < rows; j++) {
//                pat = new Patent();
//                //获得第 j 行
//                Row row = sheet.getRow(j);
//                pat.setName(row.getCell(2).getStringCellValue());
//                pat.setApplyNo(row.getCell(3).getStringCellValue());
//                pat.setTongZu(row.getCell(4).getStringCellValue());
//                pat.setYinZheng(row.getCell(5).getStringCellValue());
//                pat.setBeiYin(row.getCell(6).getStringCellValue());
//                pat.setApplyDate(row.getCell(7).getDateCellValue());
//                pat.setPublicNo(row.getCell(8).getStringCellValue());
//                pat.setPublicDate(row.getCell(9).getDateCellValue());
//                pat.setIPCTypeNo(row.getCell(10).getStringCellValue());
//                pat.setApplyPerson(row.getCell(11).getStringCellValue());
//                pat.setInventor(row.getCell(12).getStringCellValue());
//                pat.setPriorityNumber(row.getCell(13).getStringCellValue());
//                pat.setPriorityDay(row.getCell(14).getStringCellValue());
//                pat.setApplyPersonAddress(row.getCell(15).getStringCellValue());
//                pat.setApplyPersonZip(row.getCell(16).getStringCellValue());
//                pat.setLegalEffectiveDate(row.getCell(17).getStringCellValue());
//                pat.setLegalEffectiveMeaning(row.getCell(18).getStringCellValue());
//                log.info(pat.toString());
//                pats.add(pat);
//            }
//        }
//        return pats;
//
//    }
}
