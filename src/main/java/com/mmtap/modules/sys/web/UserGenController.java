package com.mmtap.modules.sys.web;

import com.mmtap.modules.pat.dao.PatDao;
import com.mmtap.modules.pat.model.Patent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserGenController {

    @Autowired
    private PatDao patDao;

    @RequestMapping("/user/login")
//    public Object generalUserLogin(@RequestParam String username,
//                                   @RequestParam String password,
//                                   @RequestParam(defaultValue = "false") String remember){
        public Object generalUserLogin( String username,
                 String password,
                String remember){
        Patent patent = new Patent();
        patent.setName("测试");
        patent.setIndustry("工业");
        patent.setApplyNo("12123123j12312");
        patent.setTongZu("1231231");
        patent.setYinZheng("ksdjfds");
        patent.setBeiYin("kdjfdk");
        patent.setBeiYin("背影");
        patent.setApplyDate(new Date());
        patent.setPublicNo("a12312312312");
        patent.setPublicDate(new Date());
        patent.setIPCTypeNo("982374983274239847329847");
        patent.setApplyPerson("张国立");
        patent.setInventor("里斯本");
        patent.setPriorityNumber("12312312312");
        patent.setPriorityDay("2014-11-03 12:20:33");
        patent.setApplyPersonAddress("北京市区，海淀区，清河收到发多少");
        patent.setApplyPersonZip("skdfjsdkfjsdkfj");
        patent.setLegalEffectiveDate("ksjdfkdsjfkdjs");
        patent.setLegalEffectiveMeaning("生效");

        patDao.save(patent);
        Map m = new HashMap();
        m.put("date","sdfsd");
        return m;
    }
}
