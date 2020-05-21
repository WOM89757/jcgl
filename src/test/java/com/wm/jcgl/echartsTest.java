package com.wm.jcgl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wm.jcgl.entity.Line;
import com.wm.jcgl.entity.Match;
import com.wm.jcgl.service.MatchService;
import com.wm.sys.entity.Dept;
import com.wm.sys.service.DeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class echartsTest {
    @Resource
    private MatchService matchService;
    @Resource
    private DeptService deptService;

    @Test
    public void getWeekData() {
//            SELECT dept_id,SUM(lNum)AS lnum,SUM(bNum)AS bnum
//            FROM b_match
//            WHERE order_id=5
//            GROUP BY dept_id
        QueryWrapper<Match> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("dept_id","SUM(lNum)AS lNum","SUM(bNum)AS bNum");
        queryWrapper.eq("order_id",5);
        queryWrapper.groupBy("dept_id");
        List<Match> matchList = this.matchService.list(queryWrapper);



        List<Line> list = new ArrayList<Line>();
        for (Match match:matchList) {
            //根据id查询学校名
            if(null!=match.getDeptId()){
                Dept dept  = this.deptService.getById(match.getDeptId());
                if(null!=dept) {
                    match.setSchoolname(dept.getTitle());
                }
            }
                list.add(new Line(match.getSchoolname(),match.getLNum(),match.getBNum()));

        }


        String name[] = new String[list.size()];
        Integer lNum[]= new Integer[list.size()];
        Integer bNum[]= new Integer[list.size()];

        for (int i = 0; i < list.size(); i++) {
            Line l = list.get(i);
            name[i]= l.getName();
            lNum[i]= l.getLNum();
            bNum[i]= l.getBNum();

        }
        System.out.println(name.toString());

//        //根据获取的数据赋值
//        for (Line data : list) {
//
//            linedata.setNames(data.getName());
//            linedata.setlNum(data.getLNum());
//            linedata.setbNum(data.getBNum());
//        }
//        System.out.println(linedata);

    }
    @Test
    public void getDate(){

        //模拟二维数据表数据-多行
        String jsonData="["
                + "{\"y0:最低气温\":null,\"y0:最高气温\":11,\"x:日期\":\"周一\"},"
                + "{\"y0:最低气温\":-2,\"y0:最高气温\":11,\"x:日期\":\"周二\"},"
                + "{\"y0:最低气温\":2,\"y0:最高气温\":15,\"x:日期\":\"周三\"},"
                + "{\"y0:最低气温\":5,\"y0:最高气温\":13,\"x:日期\":\"周四\"},"
                + "{\"y0:最低气温\":3,\"y0:最高气温\":12,\"x:日期\":\"周五\"},"
                + "{\"y0:最低气温\":2,\"y0:最高气温\":13,\"x:日期\":\"周六\"},"
                + "{\"y0:最低气温\":1,\"y0:最高气温\":10,\"x:日期\":\"周日\"}]";
        List<Map<String,Object>> mapList = JSON.parseObject(jsonData,new TypeReference<List<Map<String,Object>>>(){});

        //输出调试信息
        System.out.println("----------------测试数据开始-------------\n\n");
        System.out.println("y0:最低气温 \t y0:最高气温 \t x:日期");
        System.out.println("-----------------------------");
        mapList.forEach((Map<String,Object> map) ->{
            System.out.println(map.get("y0:最低气温")+" \t "+map.get("y0:最高气温")+" \t "+map.get("x:日期"));
        });
        System.out.println("\n\n----------------测试数据结束-------------");
        System.out.println(mapList);
    }

}
